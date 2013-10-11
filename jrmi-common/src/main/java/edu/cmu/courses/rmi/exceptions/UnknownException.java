package edu.cmu.courses.rmi.exceptions;

/**
 * A <code>UnknownException</code> is thrown, if an exception raised
 * during the execution of a remote call that we have no idea about
 * what is wrong
 *
 * @author Jian Fang
 * @author Fangyu Gao
 */
public class UnknownException extends RemoteException {
    /**
     * Constructor of <code>UnknownException</code> with the
     * specified message
     *
     * @param message the specified message
     */
    public UnknownException(String message){
        super(message);
    }

    /**
     * Constructor of <code>UnknownException</code> with the
     * specified message and the cause
     *
     * @param message the specified message
     * @param e the cause
     */
    public UnknownException(String message, Exception e){
        super(message, e);
    }
}
