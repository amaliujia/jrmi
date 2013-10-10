package edu.cmu.courses.rmi.exceptions;

/**
 * A <code>ConnectionException</code> is thrown, if the remote host
 * refused our connection for a remote call
 *
 * @author Jian Fang
 * @author Fangyu Gao
 */
public class ConnectionException extends RemoteException {
    /**
     * Constructor of <code>ConnectionException</code> with the
     * specified message
     *
     * @param message the specified message
     */
    public ConnectionException(String message){
        super(message);
    }

    /**
     * Constructor of <code>ConnectionException</code> with the
     * specified message and the cause
     *
     * @param message the specified message
     * @param e the cause
     */
    public ConnectionException(String message, Exception e){
        super(message, e);
    }
}
