package edu.cmu.courses.rmi;

/**
 * A <code>AlreadyBoundException</code> is someone wants to
 * bind an already bound object in the registry
 *
 * @author Jian Fang
 * @author Fangyu Gao
 */
public class AlreadyBoundException extends Exception {
    /**
     * Constructor of <code>AlreadyBoundException</code>
     */
    public AlreadyBoundException(){
        super();
    }

    /**
     * Constructor of <code>AlreadyBoundException</code> with
     * cause message
     *
     * @param message the cause message
     */
    public AlreadyBoundException(String message){
        super(message);
    }
}
