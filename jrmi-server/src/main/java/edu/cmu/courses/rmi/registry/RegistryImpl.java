package edu.cmu.courses.rmi.registry;

import edu.cmu.courses.rmi.Registry;
import edu.cmu.courses.rmi.exceptions.AlreadyBoundException;
import edu.cmu.courses.rmi.exceptions.NotBoundException;
import edu.cmu.courses.rmi.exceptions.RemoteException;
import edu.cmu.courses.rmi.RemoteRef;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A <code>RegistryImpl</code> implements the
 * Registry interfaces.
 *
 * @author Jian Fang
 * @author Fangyu Gao
 */
public class RegistryImpl implements Registry {
    private ConcurrentHashMap<String, RemoteRef> refs;

    /**
     * Default constructor, create a hashmap.
     */
    public RegistryImpl(){
        refs = new ConcurrentHashMap<String, RemoteRef>();
    }

    /**
     * Look up a service name in the registry.
     * 
     * @param serviceName the name of service
     * @return a reference to a remote object
     * @throws RemoteException
     */
    @Override
    public RemoteRef lookup(String serviceName) throws RemoteException {
        RemoteRef ref = refs.get(serviceName);
        return ref;
    }

    /**
     * Bind a service name to a remote object referece.
     * 
     * @param serviceName the name of service
     * @param ref remote object reference.
     * @throws RemoteException
     * @throws AlreadyBoundException
     */
    @Override
    public void bind(String serviceName, RemoteRef ref) throws RemoteException, AlreadyBoundException {
        if(refs.putIfAbsent(serviceName, ref) != null)
            throw new AlreadyBoundException("service " + serviceName +
                    " has already bound to " + ref.toString());
    }

    /**
     * Unbind a service name.
     * 
     * @param serviceName the service name to unbind.
     * @throws RemoteException
     * @throws NotBoundException
     */
    @Override
    public void unbind(String serviceName) throws RemoteException, NotBoundException {
        if(refs.remove(serviceName) == null)
            throw new NotBoundException("service " + serviceName + " have not bound yet");
    }

    /**
     * Bind a service name to a remote object reference.
     * 
     * @param serviceName service name to unbind.
     * @param ref remote reference
     * @throws RemoteException
     */
    @Override
    public void rebind(String serviceName, RemoteRef ref) throws RemoteException {
        refs.put(serviceName, ref);
    }

    /**
     * return the service names in the registry.
     * 
     * @return String[] serviceNames
     * @throws RemoteException
     */
    @Override
    public String[] list() throws RemoteException {
        Set<String> keys = refs.keySet();
        String[] results = new String[keys.size()];
        keys.toArray(results);
        return results;
    }
}
