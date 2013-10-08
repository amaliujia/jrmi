package edu.cmu.courses.rmi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * The <code>StubClassLoader</code> is a subclass of
 * Java ClassLoader class. It use reflection to create
 * class at run time. And it can download Stub class from
 * server if it doesn't exist locally.
 *
 * @author Jian Fang
 * @author Fangyu Gao
 */
public class StubClassLoader extends ClassLoader{
    /**
     * The host name of stub server.
     */
	String host;
	
    /**
     * The port number of stub downloading service.
     */
	int port;
	
    /**
     * The name of stub to download
     */
	String stubClassName;
	
    /**
     * The default constructor
     */
	public StubClassLoader(){
		
	}
	
    /**
     * Set host name, port number and stub class name
     * 
     * @param host, host name
     * @param port, port number
     * @param stubClassName, name of stub class
     */
	public void setStubClassLoader(String host,int port, String stubClassName){
		this.host = host;
		this.port = port;
		this.stubClassName = stubClassName;
	}
	
    /**
     * Download stub class and generate the create the class
     * at runtime.
     */
	public Class getStubClass()
			throws UnknownHostException, IOException{
    	Socket socket = new Socket(host, port);
    	ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
    	ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
    	out.writeObject(stubClassName);
        
        byte data;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int count = 0;
        while((data = (byte) in.read()) != -1) {
    		System.out.println(data*10000);
    		System.out.println(++count);
        	buffer.write(data);
        }
        out.close();
        in.close();
        byte[] classData = buffer.toByteArray();
        buffer.close();
        return defineClass(stubClassName, classData, 0, classData.length);
    }
}
