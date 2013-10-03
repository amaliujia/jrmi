package edu.cmu.courses.rmi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

/**
 * The <code>RemoteServer</code> is multi-thread server with giving
 * port.
 *
 * @author Jian Fang
 * @author Fangyu Gao
 */
public class RemoteServer implements Runnable{
    /**
     * Logger
     */
    private static Logger LOG = LogManager.getLogger(RemoteServer.class);

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
    public RemoteServer(String host, int port, int poolSize){
        this.host = host;
        this.port = port;
        threadPool = Executors.newFixedThreadPool(poolSize);
    }

    /**
     * Start accepting client request
     */
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
            threadPool.execute(new RemoteServerWorker(socket));
        }
    }

    /**
     * Setting the server socket
     *
     * @throws IOException
     */
    private void setHost() throws IOException {
        serverSocket = new ServerSocket(port);
    }
}
