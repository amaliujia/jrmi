package edu.cmu.courses.rmi.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class StubServerWorker implements Runnable{
    /**
     * Logger
     */
    private static Logger LOG = LogManager.getLogger(RemoteServerWorker.class);
    /**
     * The socket of the request
     */
    private Socket socket;

    /**
     * The constructor of <code>RemoteServerWorker</code> with the
     * request socket
     *
     * @param socket the request socket
     */
    public StubServerWorker(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
    	try{
	    	ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
	    	ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
	    	String stubClassName = (String) in.readObject();
	    	System.out.println(System.getProperty("user.dir") + "\\target\\classes\\"
	    			+ stubClassName.replace('.', '\\') + ".class");
	    	FileReader reader = new FileReader(
	    			System.getProperty("user.dir") + "\\target\\classes\\"
	    			+ stubClassName.replace('.', '\\') + ".class");
	    	byte data;
;	    	//InputStream input = socket.getInputStream();
			int count = 0;
	    	while((data =(byte) reader.read()) != -1) {
	    		System.out.println(data*10000);
	    		System.out.println(++count);
	    		out.writeByte(data);
	    		out.flush();
	    	}
	    	//out.writeByte(-1);
	    	socket.close();
	    	reader.close();
    	}catch(IOException ex) {
    		LOG.error("Stub Server error", ex);
    	} catch (ClassNotFoundException e) {
    		LOG.error("Stub Server error", e);
			e.printStackTrace();
		}
    }
}
