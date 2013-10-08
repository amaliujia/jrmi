package edu.cmu.courses.rmi.registry;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import edu.cmu.courses.rmi.RemoteRef;
import edu.cmu.courses.rmi.RemoteRefTable;
import edu.cmu.courses.rmi.server.RemoteServer;
import edu.cmu.courses.rmi.utils.Util;
import edu.cmu.courses.rmi.validators.PoolSizeValidator;
import edu.cmu.courses.rmi.validators.PortValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.UnknownHostException;


public class RegistryServerMain {
    private static Logger LOG = LogManager.getLogger(RegistryServerMain.class);

    @Parameter(names = {"-p", "--port"},
            description = "the listening port of registry server",
            validateWith = PortValidator.class)
    private int port = Registry.REGISTRY_PORT;

    @Parameter(names = {"-t", "--threads"},
            description = "the size of the thread pool",
            validateWith = PoolSizeValidator.class)
    private int poolSize = 32;

    private String host;
    private Thread serverThread;

    /**
     * Constructor of <code>RegistryServerMain</code>
     * Get the ip of this host.
     * 
     * @see edu.cmu.courses.rmi.utils.Util#getHost()
     */
    public RegistryServerMain() throws UnknownHostException {
        host = Util.getHost();
    }

    /**
     * Start a <code>RemoteServer</code>
     * Get the ip of this host.
     * 
     * @see edu.cmu.courses.rmi.utils.Util#getHost()
     */
    public void startServer(){
        RemoteRef ref = new RemoteRef(host, port,
                RegistryImpl.class.getName(), Registry.REGISTRY_OBJID);
        RegistryImpl registry = new RegistryImpl();
        RemoteRefTable.addObject(ref, registry);
        serverThread = new Thread(new RemoteServer(host, port, poolSize));
        serverThread.start();
    }
    /**
     * Main function.
     * Start <code>RegistryServerMain</code> and parsing the arguments
     *
     * @param args program augments
     * @see edu.cmu.courses.rmi.registry.RegistryServerMain#registryServerMain()
     */
    public static void main(String[] args) {
        RegistryServerMain registryServerMain = null;
        try{
            registryServerMain = new RegistryServerMain();
        } catch (UnknownHostException e){
            LOG.error("can't get this machine's address",e);
            System.exit(-1);
        }
        new JCommander(registryServerMain, args);
        registryServerMain.startServer();
    }
}
