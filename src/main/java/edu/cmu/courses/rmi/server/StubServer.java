package edu.cmu.courses.rmi.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.cmu.courses.rmi.ConnectionException;
import edu.cmu.courses.rmi.Stub;


public class StubServer implements Runnable{
	private static Logger LOG = LogManager.getLogger(RemoteServerMain.class);
    /**
     * Server's port
     */
    private int port;

    /**
     * Server's binding address
     */
    private String host;

    /**
     * Server's thread pool
     */
    private ExecutorService threadPool;

    /**
     * Server socket
     */
    private ServerSocket serverSocket;

    /**
     * Constructor of <code>RemoteServer</code> with specified
     * port and thread pool size
     *
     * @param port
     * @param poolSize
     */
    public StubServer(int port, int poolSize){
        this.port = port;
        threadPool = Executors.newFixedThreadPool(poolSize);
    }
    @Override
    public void run() {
        try{
            setHost();
        } catch (IOException e){
            LOG.error("Binding server socket error", e);
            return;
        }
        while(true){
            Socket socket;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                LOG.error("Accept socket error", e);
                continue;
            }
            threadPool.execute(new StubServerWorker(socket));
        }
    }
    private void setHost() throws IOException {
        serverSocket = new ServerSocket(port);
    }
}
