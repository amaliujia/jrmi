package edu.cmu.courses.rmi;

/**
 * A <code>NoSuchStubException</code> is thrown, if the stub of a
 * remote object can't find
 *
 * @author Jian Fang
 * @author Fangyu Gao
 */
public class NoSuchStubException extends Exception {
    /**
     * Constructor of <code>NoSuchStubException</code> with the
     * specified message
     *
     * @param message the specified message
     */
    public NoSuchStubException(String message){
        super(message);
    }

    /**
     * Constructor of <code>NoSuchStubException</code> with the
     * specified message and cause
     *
     * @param message the specified message
     * @param e the cause
     */
    public NoSuchStubException(String message, Exception e){
        super(message, e);
    }
}
