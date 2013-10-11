package edu.cmu.courses.rmi.exceptions;

import java.io.IOException;

/**
 * A <code>RemoteException</code> is the superclass for a bunch of
 * communicate-related exceptions which may occur during the execution
 * of remote procedure call
 *
 * @author Jian Fang
 * @author Fangyu Gao
 */
public class RemoteException extends IOException {
    /**
     * The cause of the remote exception
     */
    public Throwable detail;

    /**
     * Construct a <code>RemoteException</code>
     */
    public RemoteException(){
        initCause(null);
    }

    /**
     * Construct a <code>RemoteException</code> with a specific message
     *
     * @param message the detail message
     */
    public RemoteException(String message){
        super(message);
        initCause(null);
    }

    /**
     * Construct a <code>RemoteException</code> with a specific message
     * and cause
     *
     * @param message the detail message
     * @param cause the cause
     */
    public RemoteException(String message, Throwable cause){
        super(message);
        this.detail = cause;
    }

    /**
     * Return the detail message for the exception
     *
     * @return the detail message
     */
    public String getMessage(){
        if(detail == null){
            return super.getMessage();
        } else {
            return super.getMessage() + "; nested exception is: \n\t" +
                    detail.toString();
        }
    }

    /**
     * Return the cause of the exception
     *
     * @return the cause
     */
    public Throwable getCause(){
        return detail;
    }

}
