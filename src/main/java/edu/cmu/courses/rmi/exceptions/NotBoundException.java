package edu.cmu.courses.rmi.exceptions;

/**
 * A <code>NotBoundException</code> is thrown, if someone wants to lookup
 * or unbind a name in the registry that hasn't bound.
 *
 * @author Jian Fang
 * @author Fangyu Gao
 */
public class NotBoundException extends Exception {

    /**
     * Constructor of <code>NotBoundException</code>
     */
    public NotBoundException(){
        super();
    }

    /**
     * Constructor of <code>NotBoundException</code> with cause
     * message
     *
     * @param message the cause message
     */
    public NotBoundException(String message){
        super(message);
    }
}
