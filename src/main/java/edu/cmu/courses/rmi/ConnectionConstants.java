package edu.cmu.courses.rmi;

/**
 * The <code>ConnectionConstants</code> contains the connection
 * protocol's number
 *
 * @author Jian Fang
 * @author Fangyu Gao
 */
public class ConnectionConstants {
    /**
     * Used when start a remote call
     */
    public static final byte CALL = 0x0;

    /**
     * Used when a remote call finished
     */
    public static final byte RETURN = 0x1;

    /**
     * Used when successfully finished the remote call
     */
    public static final byte NORMAL_RETURN = 0x2;

    /**
     * Used when exceptions raised during remote call execution
     */
    public static final byte EXCEPTION_RETURN = 0x3;
}
