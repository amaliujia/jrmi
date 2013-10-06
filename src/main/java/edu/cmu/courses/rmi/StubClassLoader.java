package edu.cmu.courses.rmi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class StubClassLoader extends ClassLoader{
	String host;
	int port;
	String stubClassName;
	public StubClassLoader(){
		
	}
	
	public void setStubClassLoader(String host,int port, String stubClassName){
		this.host = host;
		this.port = port;
		this.stubClassName = stubClassName;
	}
	public Class getStubClass()
			throws UnknownHostException, IOException{
    	Socket socket = new Socket(host, port);
    	ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
    	ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
    	out.writeByte(ConnectionConstants.CALL);
        out.writeChars(stubClassName);
        
        int data;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        while((data = in.read()) != -1) {
        	buffer.write(data);
        }
        out.close();
        in.close();
        buffer.close();
        byte[] classData = buffer.toByteArray();
        return defineClass(stubClassName, classData, 0, classData.length);
    }
}
