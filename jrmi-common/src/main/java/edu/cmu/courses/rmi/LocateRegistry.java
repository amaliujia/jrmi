package edu.cmu.courses.rmi;

import edu.cmu.courses.rmi.utils.Util;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * A <code>LocateRegistry</code> provide methods
 * to return registry information from the server.
 *
 * @author Jian Fang
 * @author Fangyu Gao
 */
public final class LocateRegistry {
    private static Object lock = new Object();
    private static Registry registry = null;

    private LocateRegistry(){}

    /**
     * Get the stub for registry
     * 
     * @return registry stub
     * @throws IOException
     */
    public static Registry getRegistry()
            throws IOException{
        return getRegistry(null, Registry.REGISTRY_PORT);
    }
    
    /**
     * Get the stub for registry
     * 
     * @param port port number
     * @return registry stub
     * @throws IOException
     */
    public static Registry getRegistry(int port)
            throws IOException{
        return getRegistry(null, port);
    }
    /**
     * Get the stub for registry
     * 
     * @param host host name
     * @return registry stub
     * @throws IOException
     */
    public static Registry getRegistry(String host)
            throws IOException{
        return getRegistry(host, Registry.REGISTRY_PORT);
    }

    /**
     * Get the stub for registry
     * 
     * @param host host name
     * @param port registry port
     */
    public static Registry getRegistry(String host, int port)
            throws IOException{
        synchronized (lock){
            if(registry == null){
                if(host == null)
                    try {
                        host = Util.getHost();
                    } catch (UnknownHostException e) {
                        host = "";
                    }
                RemoteRef ref = new RemoteRef(host, port,
                        "edu.cmu.courses.rmi.registry.RegistryImpl",
                        Registry.REGISTRY_OBJID);
                registry = (Registry)ref.localise();
            }
        }
        return registry;
    }
}
