package edu.cmu.courses.rmi.server;


import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import edu.cmu.courses.rmi.*;
import edu.cmu.courses.rmi.exceptions.RemoteException;
import edu.cmu.courses.rmi.LocateRegistry;
import edu.cmu.courses.rmi.Registry;
import edu.cmu.courses.rmi.registry.RegistryImpl;
import edu.cmu.courses.rmi.utils.Util;
import edu.cmu.courses.rmi.server.validators.PoolSizeValidator;
import edu.cmu.courses.rmi.server.validators.PortValidator;
import edu.cmu.courses.rmi.server.validators.RemoteFormatValidator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * The <code>RemoteServerMain</code> class is the start point
 * of the server. It can be select to start a registry service
 * or a rmi service. The rmi service contains a stub service
 * that enable client to download stub.
 *
 * @author Jian Fang
 * @author Fangyu Gao
 */
public class RemoteServerMain {
    private static Logger LOG = LogManager.getLogger(RemoteServerMain.class);

    @Parameter(names = {"-p", "--port"},
               description = "the listening port of remote server",
               validateWith = PortValidator.class)
    private int port = 15440;

    @Parameter(names = {"-R", "--as-registry-server"},
               description = "start up as a registry server")
    private boolean asRegistry = false;

    @Parameter(names = {"-rh", "--registry-host"},
            description = "the host of registry server")
    private String registryHost = null;

    @Parameter(names = {"-rp", "--registry-port"},
            description = "the listening port of registry server",
            validateWith = PortValidator.class)
    private int registryPort = Registry.REGISTRY_PORT;

    @Parameter(names = {"-t", "--threads"},
               description = "the size of the thread pool",
               validateWith = PoolSizeValidator.class)
    private int poolSize = 32;

    @Parameter(names = {"-r", "--remote"},
               description = "the remote object name and service name, use format className@serviceName",
               validateWith = RemoteFormatValidator.class)
    private List<String> remotes = new ArrayList<String>();

    @Parameter(names = {"-h", "--help"},
            help = true)
    private boolean help;

    /**
     * host name
     */
    private String host;
    
    /**
     * host name
     */
    private RemoteServer server;
    private Thread serverThread;

    /**
     * Defult constructor, set the host.
     */
    public RemoteServerMain() throws UnknownHostException {
        host = Util.getHost();
    }

    public boolean needHelp(){
        return help;
    }

    /**
     * Register the romote class to registry
     * 
     * @param className, name of class
     * @param serviceName, name of service that client
     * can look up with
     */
    private boolean registerRemoteClass(String className, String serviceName) throws IOException{
        Class c;
        Remote obj;
        Registry registry;
        try {
            c = Class.forName(className);
        } catch (ClassNotFoundException e) {
            LOG.error("Can't find class " + className, e);
            return false;
        }
        try {
            obj = (Remote)c.newInstance();
        }catch (Exception e) {
            LOG.error("Failed to create class " + className, e);
            return false;
        }
        RemoteRef ref = new RemoteRef(host, port, className);
        RemoteRefTable.addObject(ref, obj);
        try {
            registry = LocateRegistry.getRegistry(registryHost, registryPort);
        } catch (RemoteException e) {
            LOG.error("Can't communicate with registry server", e);
            return false;
        }
        try {
            if(registry.lookup(serviceName) == null)
                registry.bind(serviceName, ref);
        } catch (Exception e) {
            LOG.error("Failed to bind " + serviceName + " for class " + className, e);
            return false;
        }
        return true;
    }

    /**
     * Register remote object in registry.
     * 
     * @throws IOEXception 
     */
    private boolean registerRemoteClasses() throws IOException{
        for(String remote: remotes){
            String[] words = remote.split("@");
            String className = words[0];
            String serviceName = words[1];
            if(!registerRemoteClass(className, serviceName)){
                return false;
            }
        }
        return true;
    }

    /**
     * Start rmi service and stub service. . 
     */
    private void startRMIServer() throws Exception{
        server = new RemoteServer(host, port, poolSize);
        serverThread = new Thread(server);
        if(registerRemoteClasses()){
            startStubServer();
            serverThread.start();
            serverThread.join();
        }
    }

    /**
     * Start the registry server. 
     */
    private void startRegistryServer() throws Exception{
        RemoteRef ref = new RemoteRef(host, port,
                RegistryImpl.class.getName(), Registry.REGISTRY_OBJID);
        RegistryImpl registry = new RegistryImpl();
        RemoteRefTable.addObject(ref, registry);
        server = new RemoteServer(registryHost, registryPort, poolSize);
        serverThread = new Thread(server);
        serverThread.start();
        serverThread.join();
    }

    /**
     * If asRegistry, start the registry server. Else, start
     * an normal server with rmi service with stub service.
     *
     */
    private void startServer() throws Exception{
        if(asRegistry){
            startRegistryServer();
        } else {
            startRMIServer();
        }
    }

    /**
     * Start stub server.
     */
    private void startStubServer() throws Exception {
        Server server = new Server(RemoteStub.STUB_PORT);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(new ServletHolder(new StubClassDownloadHandler()), RemoteStub.HTTP_URI + "*");
        server.setHandler(context);
        server.start();
    }
    /**
     * The main function of server.
     * Parsing command line and start server.
     *
     * @param args program augments
     * @see edu.cmu.courses.rmi.server.RemoteServerMain#RemoteServerMain()
     */
    public static void main(String[] args){
        RemoteServerMain main = null;
        try {
            main = new RemoteServerMain();
            JCommander jCommander = new JCommander(main, args);
            if(main.needHelp()){
                jCommander.usage();
            } else {
                main.startServer();
            }
        } catch (UnknownHostException e) {
            LOG.error("can't get this machine's address", e);
            System.exit(-1);
        } catch (Exception e) {
            LOG.error(e);
        }
    }
}
