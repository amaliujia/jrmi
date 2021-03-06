package edu.cmu.courses.rmi;

import edu.cmu.courses.rmi.exceptions.RemoteException;
import edu.cmu.courses.rmi.exceptions.UnknownException;
import edu.cmu.courses.rmi.utils.Util;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * The <code>RemoteStub</code> class is the superclass of all stubs for
 * remote object.
 *
 * @author Jian Fang
 * @author Fangyu Gao
 */
public abstract class RemoteStub implements Serializable{
    /**
     * Port for downloading stub class
     */
    public static final int STUB_PORT = 15213;
    public static final String STUB_SUFFIX = "_Stub";
    public static final String HTTP_URI = "/stub/";

    /**
     * Reference of the remote object
     */
    protected RemoteRef ref;

    /**
     * Constructor of <code>RemoteStub</code>
     */
    public RemoteStub(){
        ref = null;
    }

    /**
     * Constructor of <code>RemoteStub</code> with the
     * reference of remote object
     *
     * @param ref remote object reference
     */
    public RemoteStub(RemoteRef ref){
        setRef(ref);
    }

    /**
     * Set the reference
     *
     * @param ref the object's reference
     */
    public void setRef(RemoteRef ref){
        this.ref = ref;
    }

    /**
     * Invoke the remote object method, using <code>RemoteRef</code>
     * invoke method.
     *
     * @param method the method to be invoked
     * @param params the parameters of the method
     * @return method's return object
     * @throws edu.cmu.courses.rmi.exceptions.RemoteException if any exception raised
     */
    protected Object invoke(Method method, Object[] params)
        throws RemoteException {
        if(ref == null){
            throw new RemoteException("No reference in remote object");
        } else {
            try {
                return ref.invoke(method, Util.computeMethodHash(method), params);
            } catch (RemoteException e) {
                throw e;
            } catch (Exception e){
                throw new UnknownException("Unknown exception", e);
            }
        }
    }
}
