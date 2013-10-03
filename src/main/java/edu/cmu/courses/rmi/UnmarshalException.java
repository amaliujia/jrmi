package edu.cmu.courses.rmi;

/**
 * A <code>UnmarshalException</code> is thrown, if something goes
 * wrong when unmarshalling the remote call header, arguments or
 * return value
 *
 * @author Jian Fang
 * @author Fangyu Gao
 */
public class UnmarshalException extends RemoteException {
    /**
     * Constructor of <code>UnmarshalException</code> with the
     * specified message
     *
     * @param message the specified message
     */
    public UnmarshalException(String message){
        super(message);
    }

    /**
     * Constructor of <code>UnmarshalException</code> with the
     * specified message and the cause
     *
     * @param message the specified message
     * @param e the cause
     */
    public UnmarshalException(String message, Exception e){
        super(message, e);
    }
}
