package edu.cmu.courses.rmi;

/**
 * A <code>MarshalException</code> is thrown, if something goes
 * wrong when marshalling the remote call header, arguments or
 * return value
 *
 * @author Jian Fang
 * @author Fangyu Gao
 */
public class MarshalException extends RemoteException {
    /**
     * Constructor of <code>MarshalException</code> with the
     * specified message
     *
     * @param message the specified message
     */
    public MarshalException(String message){
        super(message);
    }

    /**
     * Constructor of <code>MarshalException</code> with the
     * specified message and the cause
     *
     * @param message the specified message
     * @param e the cause
     */
    public MarshalException(String message, Exception e){
        super(message, e);
    }
}
