package edu.cmu.courses.rmi.server;

import edu.cmu.courses.rmi.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Socket;

public class DispatchConnection extends RemoteConnection {

    public DispatchConnection(Socket socket)
            throws ConnectionException {
        super(socket);
    }
    /**
     * Dispatch the remote invocation request. First find the right remote object,
     * then get the right method, finally invoke the method
     *
     * @throws Exception if exceptions raised
     */
    public void dispatch()
            throws Exception{
        try{
            if(in.readByte() != ConnectionConstants.CALL){
                exceptionReturn(new UnmarshalException("Invalid remote call code"));
                return;
            }
            RemoteRef ref = (RemoteRef)in.readObject();
            Remote obj = RemoteRefTable.findObject(ref);
            if(obj == null){
                exceptionReturn(new NoSuchObjectException("Can't find object " + ref.getId()));
                return;
            }

            Method method = RemoteRefTable.findObjectMethod(obj, in.readLong());
            if(method == null){
                exceptionReturn(new UnmarshalException("Can unmarshal the specific method"));
                return;
            }
            dispatchMethod(obj, method);
        } catch (IOException ex){
            throw new ConnectionException("RemoteConnection failed when dispatching the " +
                    "remote call request", ex);
        }
    }

    /**
     * Build the parameters array of method, and invoke the method
     *
     * @param obj the remote object
     * @param method the method to be invoked
     * @throws IOException if exceptions raised
     */
    private void dispatchMethod(Remote obj, Method method)
            throws IOException{

        Class<?>[] types = method.getParameterTypes();
        Object[] params = new Object[types.length];
        try{
            for(int i = 0; i < types.length; i++){
                params[i] = unmarshalValue(types[i], in);
            }
        } catch(Exception e){
            exceptionReturn(new UnmarshalException("Failed to unmarshal method parameter", e));
            return;
        }
        try {
            Object result = method.invoke(obj, params);
            if(method.getReturnType() != void.class)
                normalReturn(method.getReturnType(), result);
            else
                normalReturn();
        } catch (Exception e) {
            exceptionReturn(e);
        }
    }

    private void normalReturn()
            throws IOException{
        out.writeByte(ConnectionConstants.RETURN);
        out.writeByte(ConnectionConstants.NORMAL_RETURN);
        out.flush();
    }

    private void normalReturn(Class<?> type, Object result)
            throws IOException{
        out.writeByte(ConnectionConstants.RETURN);
        out.writeByte(ConnectionConstants.NORMAL_RETURN);
        marshalValue(type, result, out);
        out.flush();
    }

    private void exceptionReturn(Exception e)
            throws IOException {
        out.writeByte(ConnectionConstants.RETURN);
        out.writeByte(ConnectionConstants.EXCEPTION_RETURN);
        out.writeObject(e);
        out.flush();
    }
}
