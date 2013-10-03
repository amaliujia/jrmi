package edu.cmu.courses.rmi.registry;

import edu.cmu.courses.rmi.RemoteException;
import edu.cmu.courses.rmi.RemoteRef;
import edu.cmu.courses.rmi.utils.Util;

import java.net.UnknownHostException;

public final class LocateRegistry {
    private static Object lock = new Object();
    private static Registry registry = null;

    private LocateRegistry(){}

    public static Registry getRegistry()
            throws RemoteException{
        return getRegistry(null, Registry.REGISTRY_PORT);
    }

    public static Registry getRegistry(int port)
            throws RemoteException{
        return getRegistry(null, port);
    }

    public static Registry getRegistry(String host)
            throws RemoteException{
        return getRegistry(host, Registry.REGISTRY_PORT);
    }

    public static Registry getRegistry(String host, int port)
            throws RemoteException{
        synchronized (lock){
            if(registry == null){
                if(host == null)
                    try {
                        host = Util.getHost();
                    } catch (UnknownHostException e) {
                        host = "";
                    }
                RemoteRef ref = new RemoteRef(host, port,
                        RegistryImpl.class.getName(), Registry.REGISTRY_OBJID);
                registry = (Registry)ref.localise();
            }
        }
        return registry;
    }
}
