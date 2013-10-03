package edu.cmu.courses.rmi;

import java.io.*;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * The <code>Connection</code> class is used to handle
 * the communication between client and server. This class
 * can be used in both side
 *
 * @author Jian Fang
 * @author Fangyu Gao
 */
public class Connection {
    /**
     * The socket object used for connection
     */
    private Socket socket;

    /**
     * The object output stream
     */
    private ObjectOutputStream out;

    /**
     * The object input stream
     */
    private ObjectInputStream in;

    /**
     * The constructor of <code>Connection</code>, build
     * with target host and port
     *
     * @param host target host address
     * @param port target port
     * @throws ConnectionException If creating the socket and setting the
     *         input/output stream failed
     */
    public Connection(String host, int port)
        throws ConnectionException{
        try{
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e){
            throw new ConnectionException("Creating connection failed", e);
        }
    }

    /**
     * The constructor of <code>Connection</code>, build with
     * existing socket
     *
     * @param socket the existing socket
     * @throws ConnectionException If setting the
     *         input/output stream failed
     */
    public Connection(Socket socket)
            throws ConnectionException{
        try{
            this.socket = socket;
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e){
            throw new ConnectionException("Creating connection failed", e);
        }
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
            throw new ConnectionException("Connection failed when " +
                    "executing remote invocation", e);
        }
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
                out.writeByte(ConnectionConstants.RETURN);
                out.writeByte(ConnectionConstants.EXCEPTION_RETURN);
                out.writeObject(new UnmarshalException("Invalid remote call code"));
                return;
            }
            RemoteRef ref = (RemoteRef)in.readObject();
            Remote obj = RemoteRefTable.findObject(ref);
            if(obj == null){
                out.writeByte(ConnectionConstants.RETURN);
                out.writeByte(ConnectionConstants.EXCEPTION_RETURN);
                out.writeObject(new NoSuchObjectException("Can't find object " + ref.id));
                return;
            }

            Method method = RemoteRefTable.findObjectMethod(obj, in.readLong());
            if(method == null){
                out.writeByte(ConnectionConstants.RETURN);
                out.writeByte(ConnectionConstants.EXCEPTION_RETURN);
                out.writeObject(new UnmarshalException("Can unmarshal the specific method"));
                return;
            }
            dispatchMethod(obj, method);
            out.flush();
        } catch (IOException ex){
            throw new ConnectionException("Connection failed when dispatching the " +
                    "remote call request", ex);
        }
    }

    /**
     * Marshal value to an ObjectOutputStream.
     *
     * @param type the object's type information
     * @param value the object
     * @param out the writing handler
     * @exception IOException if write failed
     */
    public static void marshalValue(Class<?> type, Object value, ObjectOutputStream out)
            throws IOException{
        if(type.isPrimitive()){
            if (type == int.class) {
                out.writeInt(((Integer) value).intValue());
            } else if (type == boolean.class) {
                out.writeBoolean(((Boolean) value).booleanValue());
            } else if (type == byte.class) {
                out.writeByte(((Byte) value).byteValue());
            } else if (type == char.class) {
                out.writeChar(((Character) value).charValue());
            } else if (type == short.class) {
                out.writeShort(((Short) value).shortValue());
            } else if (type == long.class) {
                out.writeLong(((Long) value).longValue());
            } else if (type == float.class) {
                out.writeFloat(((Float) value).floatValue());
            } else if (type == double.class) {
                out.writeDouble(((Double) value).doubleValue());
            } else {
                throw new Error("Unrecognized primitive type: " + type);
            }
        } else {
            out.writeObject(value);
        }
    }

    /**
     * Close the connection
     *
     * @throws ConnectionException if close failed throw <code>ConnectionExceptio</code>
     */
    public void close()
            throws ConnectionException{
        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            throw new ConnectionException("Failed to close connection", e);
        }
    }

    /**
     * Unmarshal value from an ObjectInputStream
     *
     * @param type the object's type information
     * @param in the reading handler
     * @return the object
     * @throws IOException if read failed
     * @throws ClassNotFoundException if readObject can't find the object's
     *                                class
     */
    public static Object unmarshalValue(Class <?> type, ObjectInputStream in)
            throws IOException, ClassNotFoundException{
        if (type.isPrimitive()) {
            if (type == int.class) {
                return Integer.valueOf(in.readInt());
            } else if (type == boolean.class) {
                return Boolean.valueOf(in.readBoolean());
            } else if (type == byte.class) {
                return Byte.valueOf(in.readByte());
            } else if (type == char.class) {
                return Character.valueOf(in.readChar());
            } else if (type == short.class) {
                return Short.valueOf(in.readShort());
            } else if (type == long.class) {
                return Long.valueOf(in.readLong());
            } else if (type == float.class) {
                return Float.valueOf(in.readFloat());
            } else if (type == double.class) {
                return Double.valueOf(in.readDouble());
            } else {
                throw new Error("Unrecognized primitive type: " + type);
            }
        } else {
            return in.readObject();
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
            out.writeByte(ConnectionConstants.RETURN);
            out.writeByte(ConnectionConstants.EXCEPTION_RETURN);
            out.writeObject(new UnmarshalException("Failed to unmarshal method parameter", e));
            return;
        }
        try {
            Object result = method.invoke(obj, params);
            out.writeByte(ConnectionConstants.RETURN);
            out.writeByte(ConnectionConstants.NORMAL_RETURN);
            if(method.getReturnType() != void.class)
                out.writeObject(result);
        } catch (Exception e) {
            out.writeByte(ConnectionConstants.RETURN);
            out.writeByte(ConnectionConstants.EXCEPTION_RETURN);
            out.writeObject(e);
        }
    }
}
