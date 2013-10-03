package edu.cmu.courses.rmi;

/**
 * A <code>NoSuchObjectException</code> is thrown, if someone want to
 * invoke a method on a non-exist remote object.
 *
 * @author Jian Fang
 * @author Fangyu Gao
 */
public class NoSuchObjectException extends RemoteException {
    /**
     * Constructor of <code>NoSuchObjectException</code> with the
     * specified message
     *
     * @param message the specified message
     */
    public NoSuchObjectException(String message){
        super(message);
    }
}
