package edu.cmu.courses.rmi.registry;

import edu.cmu.courses.rmi.exceptions.AlreadyBoundException;
import edu.cmu.courses.rmi.exceptions.NotBoundException;
import edu.cmu.courses.rmi.exceptions.RemoteException;
import edu.cmu.courses.rmi.RemoteRef;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RegistryImpl implements Registry{
    private ConcurrentHashMap<String, RemoteRef> refs;

    public RegistryImpl(){
        refs = new ConcurrentHashMap<String, RemoteRef>();
    }

    @Override
    public RemoteRef lookup(String serviceName) throws RemoteException {
        RemoteRef ref = refs.get(serviceName);
        return ref;
    }

    @Override
    public void bind(String serviceName, RemoteRef ref) throws RemoteException, AlreadyBoundException {
        if(refs.putIfAbsent(serviceName, ref) != null)
            throw new AlreadyBoundException("service " + serviceName +
                    " has already bound to " + ref.toString());
    }

    @Override
    public void unbind(String serviceName) throws RemoteException, NotBoundException {
        if(refs.remove(serviceName) == null)
            throw new NotBoundException("service " + serviceName + " have not bound yet");
    }

    @Override
    public void rebind(String serviceName, RemoteRef ref) throws RemoteException {
        refs.put(serviceName, ref);
    }

    @Override
    public String[] list() throws RemoteException {
        Set<String> keys = refs.keySet();
        String[] results = new String[keys.size()];
        keys.toArray(results);
        return results;
    }
}
