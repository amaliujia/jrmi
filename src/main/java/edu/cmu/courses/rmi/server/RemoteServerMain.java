package edu.cmu.courses.rmi.server;


import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import edu.cmu.courses.rmi.*;
import edu.cmu.courses.rmi.registry.LocateRegistry;
import edu.cmu.courses.rmi.registry.Registry;
import edu.cmu.courses.rmi.utils.Util;
import edu.cmu.courses.rmi.validators.PoolSizeValidator;
import edu.cmu.courses.rmi.validators.PortValidator;
import edu.cmu.courses.rmi.validators.RemoteFormatValidator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class RemoteServerMain {
    private static Logger LOG = LogManager.getLogger(RemoteServerMain.class);

    @Parameter(names = {"-p", "--port"},
               description = "the listening port of remote server",
               validateWith = PortValidator.class)
    private int port = 15440;

    @Parameter(names = {"-R", "--registry"},
            description = "the host of registry server")
    private String registryHost = null;

    @Parameter(names = {"-P", "--registry-port"},
            description = "the listening port of registry server",
            validateWith = PortValidator.class)
    private int registryPort = Registry.REGISTRY_PORT;

    @Parameter(names = {"-t", "--threads"},
               description = "the size of the thread pool",
               validateWith = PoolSizeValidator.class)
    private int poolSize = 32;

    @Parameter(names = {"-r", "--remote"},
               description = "the remote object name and service name, use format className@serviceName",
               required = true,
               validateWith = RemoteFormatValidator.class)
    private List<String> remotes = new ArrayList<String>();

    private String host;
    private RemoteServer server;
    private Thread serverThread;

    public RemoteServerMain() throws UnknownHostException {
        host = Util.getHost();
    }

    private boolean registerRemoteClass(String className, String serviceName) throws UnknownHostException, IOException{
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

    private boolean registerRemoteClasses() throws UnknownHostException, IOException{
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

    private void startServer() throws UnknownHostException, IOException{
        server = new RemoteServer(host, port, poolSize);
        serverThread = new Thread(server);
        if(registerRemoteClasses())
            serverThread.start();
    }

    public static void main(String[] args) throws UnknownHostException, IOException {
    	StubServer ss = new StubServer(Stub.STUB_PORT, 32);
    	Thread stubServer = new Thread(ss);
    	stubServer.start();

        RemoteServerMain main = null;
        try {
            main = new RemoteServerMain();
        } catch (UnknownHostException e) {
            LOG.error("can't get this machine's address", e);
            System.exit(-1);
        }
        new JCommander(main, args);
        main.startServer();
    }
}
