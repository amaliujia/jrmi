package edu.cmu.courses.rmi;

import edu.cmu.courses.rmi.exceptions.ConnectionException;
import edu.cmu.courses.rmi.exceptions.MarshalException;
import edu.cmu.courses.rmi.exceptions.UnmarshalException;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * The <code>InvokeConnection</code> make connection
 * to server and read the return value.
 *
 * @author Jian Fang
 * @author Fangyu Gao
 */
public class InvokeConnection extends RemoteConnection {
    public InvokeConnection(String host, int port)
            throws ConnectionException {
        super(host, port);
    }

    /**
     * Execute the remote invocation. First, tell server we start a remote invocation,
     * then tell server the remove object reference, next we tell server the method name,
     * finally we marshal all parameters and send to the server. Then, we call
     * <code>remoteInvokeReturn</code> to wait for server's return
     *
     * @param ref
     * @param method
     * @param methodHash
     * @param params
     * @return
     */
    public Object invoke(RemoteRef ref, Method method, long methodHash, Object[] params)
            throws Exception{
        try {
            Class<?>[] types = method.getParameterTypes();
            out.writeByte(ConnectionConstants.CALL);
            out.writeObject(ref);
            out.writeLong(methodHash);
            try{
                if(params != null){
                    for(int i = 0; i < params.length; i++){
                        marshalValue(types[i], params[i], out);
                    }
                }
            } catch (IOException e){
                throw new MarshalException("Failed to marshal parameters", e);
            }
            out.flush();
            return remoteInvokeReturn(method);
        } catch (IOException e) {
            throw new ConnectionException("RemoteConnection failed when " +
                    "executing remote invocation", e);
        }
    }

    /**
     * Get return information from server, if the return is ConnectionConstants.NORMAL_RETURN
     * which indicates the remote invocation returned without exceptions, we unmarshal the
     * return object, return it. If the return is ConnectionConstants.EXCEPTION_RETURN, we
     * get the exception object, combine the stack trace, throw this exception
     *
     * @param method the method of remote invocation
     * @return return the object of remote invocation returned
     * @throws Exception If remote invocation raised exceptions
     */
    private Object remoteInvokeReturn(Method method)
            throws Exception{
        if(in.readByte() != ConnectionConstants.RETURN)
            throw new UnmarshalException("Return code is invalid");
        switch(in.readByte()){
            case ConnectionConstants.NORMAL_RETURN:
                break;
            case ConnectionConstants.EXCEPTION_RETURN:
                Object ex;
                try{
                    ex = in.readObject();
                } catch (Exception e){
                    throw new UnmarshalException("Failed to unmarshal exception return", e);
                }
                if(ex instanceof Exception){
                    exceptionReceived((Exception) ex);
                } else {
                    throw new UnmarshalException("Exception return don't give exception object");
                }
            default:
                throw new UnmarshalException("Return type code invalid");
        }
        Class<?> returnType = method.getReturnType();
        if(returnType == void.class)
            return null;
        return unmarshalValue(returnType, in);
    }

    /**
     * Combine the remote exception's stacktrace with local stacktrace
     *
     * @param ex the remote exception
     * @throws Exception throws the exception with combined stacktrace
     */
    private void exceptionReceived(Exception ex)
            throws Exception{
        StackTraceElement[] serverTrace = ex.getStackTrace();
        StackTraceElement[] clientTrace = (new Throwable()).getStackTrace();
        StackTraceElement[] combinedTrace =
                new StackTraceElement[serverTrace.length + clientTrace.length];
        System.arraycopy(serverTrace, 0, combinedTrace, 0,
                serverTrace.length);
        System.arraycopy(clientTrace, 0, combinedTrace, serverTrace.length,
                clientTrace.length);
        ex.setStackTrace(combinedTrace);
        throw ex;
    }
}
