package edu.cmu.courses.rmi;

import java.io.*;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * The <code>RemoteConnection</code> class is used to handle
 * the communication between client and server. This class
 * can be used in both side
 *
 * @author Jian Fang
 * @author Fangyu Gao
 */
public abstract class RemoteConnection {
    /**
     * The socket object used for connection
     */
    protected Socket socket;

    /**
     * The object output stream
     */
    protected ObjectOutputStream out;

    /**
     * The object input stream
     */
    protected ObjectInputStream in;

    /**
     * The constructor of <code>RemoteConnection</code>, build
     * with target host and port
     *
     * @param host target host address
     * @param port target port
     * @throws ConnectionException If creating the socket and setting the
     *         input/output stream failed
     */
    public RemoteConnection(String host, int port)
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
     * The constructor of <code>RemoteConnection</code>, build with
     * existing socket
     *
     * @param socket the existing socket
     * @throws ConnectionException If setting the
     *         input/output stream failed
     */
    public RemoteConnection(Socket socket)
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
     * Marshal value to an ObjectOutputStream.
     *
     * @param type the object's type information
     * @param value the object
     * @param out the writing handler
     * @exception IOException if write failed
     */
    public void marshalValue(Class<?> type, Object value, ObjectOutputStream out)
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
    public Object unmarshalValue(Class <?> type, ObjectInputStream in)
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
}
