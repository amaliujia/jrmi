package edu.cmu.courses.rmi;


import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import edu.cmu.courses.rmi.validators.PoolSizeValidator;
import edu.cmu.courses.rmi.validators.PortValidator;
import edu.cmu.courses.rmi.validators.RemoteFormatValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        host = getHost();
    }

    private static String getHost()
            throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        return inetAddress.getHostAddress();
    }

    private void registerRemoteClass(String className, String serviceName){
        Class c;
        Remote obj;
        try {
            c = Class.forName(className);
        } catch (ClassNotFoundException e) {
            LOG.error("Can't find class " + className, e);
            return;
        }
        try {
            obj = (Remote)c.newInstance();
        } catch (Exception e) {
            LOG.error("Failed to create class " + className, e);
            return;
        }
        RemoteRef ref = new RemoteRef(host, port, className);
        RemoteRefTable.addObject(ref, obj);
    }

    private void registerRemoteClasses(){
        for(String remote: remotes){
            String[] words = remote.split("@");
            String className = words[0];
            String serviceName = words[1];
            registerRemoteClass(className, serviceName);
        }
    }

    private void startServer(){
        server = new RemoteServer(host, port, poolSize);
        serverThread = new Thread(server);
        registerRemoteClasses();
        serverThread.start();
    }

    public static void main(String[] args) {
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
