package edu.cmu.courses.rmi;

import edu.cmu.courses.rmi.exceptions.AlreadyBoundException;
import edu.cmu.courses.rmi.exceptions.NotBoundException;
import edu.cmu.courses.rmi.exceptions.RemoteException;

public interface Registry extends Remote {
    public static final int REGISTRY_PORT = 15640;
    public static final int REGISTRY_OBJID = 0x0;

    RemoteRef lookup(String serviceName)
            throws RemoteException;
    void bind(String serviceName, RemoteRef ref)
            throws RemoteException, AlreadyBoundException;
    void unbind(String serviceName)
            throws RemoteException, NotBoundException;
    void rebind(String serviceName, RemoteRef ref)
            throws RemoteException;
    String[] list() throws RemoteException;
}
