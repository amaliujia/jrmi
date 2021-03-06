package edu.cmu.courses.rmi;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;


/**
 * The <code>RemoteStubLoader</code> is a subclass of
 * Java ClassLoader class. It use reflection to create
 * class at run time. And it can download Stub class from
 * server if it doesn't exist locally.
 *
 * @author Jian Fang
 * @author Fangyu Gao
 */
public class RemoteStubLoader extends ClassLoader{
    /**
     * Logger object
     */
    private static Logger LOG = LoggerFactory.getLogger(RemoteStubLoader.class);

    /**
     * Buffer size
     */
    private static int READ_BUFFER_SIZE = 4096;

    /**
     * The host name of stub server.
     */
	String host;
	
    /**
     * The port number of stub downloading service.
     */
	int port;

    /**
     * The implementation class name
     */
    String implClassName;

    /**
     * constuctor 
     * 
     * @param host host name
     * @param port port number
     * @param implClassName the name of implementation class
     */
	public RemoteStubLoader(String host, int port, String implClassName){
		this.host = host;
		this.port = port;
		this.implClassName = implClassName;
	}

	/**
	 * If class exist locally, return the class.
	 * If it doesn't exist locally, download it from server.
	 * 
	 * @return Class
	 * @throws IOException
	 */
    public Class getStubClass() throws IOException {
        Class c;
        try {
            c = Class.forName(implClassName + RemoteStub.STUB_SUFFIX);
        } catch (ClassNotFoundException e) {
            c = getRemoteStubClass();
        }
        return c;
    }


    /**
     * Download stub class and generate the create the class
     * at runtime.
     */
	private Class getRemoteStubClass()
			throws IOException{
        byte[] stubClassBytes = getClassFileByte(implClassName + RemoteStub.STUB_SUFFIX);
        return defineClass(implClassName + RemoteStub.STUB_SUFFIX, stubClassBytes, 0, stubClassBytes.length);
    }

	/**
	 * Using class file URL to get class file from server.
	 * 
	 * @param className the name of class
	 * @return byte[], byte file data
	 * @throws IOException
	 */
    private byte[] getClassFileByte(String className)
            throws IOException{
        String classPath = className.replaceAll("\\.", "/") + ".class";
        String urlStr = "http://" + host + ":" + port + RemoteStub.HTTP_URI + classPath;
        URL url = new URL(urlStr);
        LOG.debug("Try to download stub class for " + implClassName + " from " + urlStr + " ...");
        BufferedInputStream in = new BufferedInputStream(url.openStream());
        byte buffer[] = new byte[READ_BUFFER_SIZE];
        int length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while((length = in.read(buffer, 0, READ_BUFFER_SIZE)) != -1){
            out.write(buffer, 0, length);
        }
        byte[] classFileBytes = out.toByteArray();
        in.close();
        out.close();
        LOG.debug("Downloaded!");
        return classFileBytes;
    }
}
