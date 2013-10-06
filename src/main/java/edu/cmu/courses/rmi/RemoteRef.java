package edu.cmu.courses.rmi;

import java.io.*;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicLong;

import edu.cmu.courses.rmiexample.client.HelloWorldClient;

/**
 * <code>RemoteRef</code> represents the reference of remote
 * object
 *
 * @author Jian Fang
 * @author Fangyu Gao
 */
public class RemoteRef implements Serializable {
    /**
     * The counter for remote object
     */
    protected static AtomicLong idCounter = new AtomicLong();
    /**
     * Host machine contains the remote object
     */
    protected String host;

    /**
     * Host machine's port for communication
     */
    protected int port;

    /**
     * The remote object id
     */
    protected long id;

    /**
     * The remote object's class name
     */
    protected String className;

    /**
     * Constructor of <code>RemoteRef</code>, generate a object id
     * for the remote object
     *
     * @param host the remote host
     * @param port the remote host's port
     */
    public RemoteRef(String host, int port, String className){
        this(host, port, className, idCounter.getAndIncrement());
    }

    public RemoteRef(String host, int port, String className, long id){
        this.host = host;
        this.port = port;
        this.className = className;
        this.id = id;
    }

    /**
     * Invoke a remote method. We use this function to communicate
     * with remote server, marshal the method and parameters, then
     * unmarshal the result. If something goes wrong throws an exception
     *
     * @param method the method to be invoked
     * @param methodHash the hash value of the method
     * @param params the parameter list
     * @return result of the invocation
     * @throws Exception if any exception occurs when invoking remote method
     */
    public Object invoke(Method method, long methodHash, Object[] params)
        throws Exception{
        InvokeConnection connection = new InvokeConnection(host, port);
        Object result = connection.invoke(this, method, methodHash, params);
        connection.close();
        return result;
    }

    /**
     * Compare two <code>RemoteRef</code> objects. This function will be called
     * when searching the object references' table
     *
     * @param obj the object to be compared
     * @return if true, the two object are same, if false, the two object are different
     */
    public boolean equals(Object obj){
        if(obj instanceof RemoteRef){
            return (this.id == ((RemoteRef)obj).id &&
                    this.host == ((RemoteRef) obj).host &&
                    this.port == ((RemoteRef) obj).port &&
                    this.className == ((RemoteRef) obj).className);
        }
        return false;
    }

    /*
     * Initialize a local instance of stub
     * 
     */
    public RemoteStub localise()
            throws UnknownHostException, IOException{
        String stubClassName = className + "_Stub";
        try{
        	StubClassLoader classLoader = new StubClassLoader();
        	//System.out.println(System.getProperty("user.dir"));
        	Class c = null;
        	try{
        		c = classLoader.loadClass(stubClassName);
        	}
        	catch(ClassNotFoundException e) {
        		classLoader.setStubClassLoader(host, Stub.STUB_PORT, stubClassName);
        		c = classLoader.getStubClass();
        	}
            //Class c = Class.forName(stubClassName);
            Object stub = c.newInstance();
            if(stub instanceof RemoteStub){
                ((RemoteStub) stub).setRef(this);
                return (RemoteStub)stub;
            } else{
                throw new IllegalStubException("The stub class isn't the child class of RemoteStub");
            }
        //} catch (ClassNotFoundException e){
          //  throw new NoSuchStubException("Can't find "+stubClassName, e);
        } catch (InstantiationException e) {
            throw new IllegalStubException("The stub don't have proper constructor", e);
        } catch (IllegalAccessException e) {
            throw new IllegalStubException("Cannot access the stub constructor", e);
        }
    }


    public long getId(){
        return id;
    }

    public String toString(){
        return id + "[" + className + "@" + host + ":" + port + "]";
    }
}
