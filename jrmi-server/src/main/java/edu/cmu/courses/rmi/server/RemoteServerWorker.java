package edu.cmu.courses.rmi.server;

import edu.cmu.courses.rmi.exceptions.ConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

/**
 * The <code>RemoteServerWorker</code> is used to handle
 * the remote invocation request from client. In order to
 * use thread pool, it implements the <code>Runnable</code>
 * interface
 *
 * @author Jian Fang
 * @author Fangyu Gao
 */
public class RemoteServerWorker implements Runnable {
    /**
     * Logger
     */
    private static Logger LOG = LoggerFactory.getLogger(RemoteServerWorker.class);
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
    public RemoteServerWorker(Socket socket){
        this.socket = socket;
    }

    /**
     * Start invocation
     */
    @Override
    public void run() {
        DispatchConnection connection = null;
        try {
            connection = new DispatchConnection(socket);
            connection.dispatch();
        } catch (ConnectionException e) {
            LOG.error("Failed to create connection for socket" +
                    socket.toString(), e);
        } catch (Exception e) {
            LOG.error("Failed to dispatch the request for socket " +
                    socket.toString(), e);
        } finally {
            try{
                if(connection != null)
                    connection.close();
            } catch (IOException ex){
                LOG.error("Failed to close connection", ex);
            }
        }

    }
}
