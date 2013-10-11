package edu.cmu.courses.rmi.exceptions;

/**
 * A <code>IllegalStubException</code> is thrown, if the stub
 * is not the child class of <code>RemoteStub</code> or the stub
 * don't have the proper constructor
 *
 * @author Jian Fang
 * @author Fangyu Gao
 */
public class IllegalStubException extends RemoteException{
    /**
     * Constructor of <code>IllegalStubException</code> with the
     * specified message
     *
     * @param message the specified message
     */
    public IllegalStubException(String message){
        super(message);
    }

    /**
     * Constructor of <code>NoSuchStubException</code> with the
     * specified message and cause
     *
     * @param message the specified message
     * @param e the cause
     */
    public IllegalStubException(String message, Exception e){
        super(message, e);
    }
}
