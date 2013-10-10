package edu.cmu.courses.rmi.registry;

import edu.cmu.courses.rmi.*;
import edu.cmu.courses.rmi.exceptions.AlreadyBoundException;
import edu.cmu.courses.rmi.exceptions.NotBoundException;
import edu.cmu.courses.rmi.exceptions.RemoteException;
import edu.cmu.courses.rmi.utils.Util;

import java.lang.reflect.Method;

public class RegistryImpl_Stub extends RemoteStub
        implements Registry {
    private static Method method_lookup_0;
    private static Method method_bind_0;
    private static Method method_unbind_0;
    private static Method method_rebind_0;
    private static Method method_list_0;

    static {
        try{
            method_lookup_0 = Registry.class.getMethod("lookup", String.class);
            method_bind_0 = Registry.class.getMethod("bind", String.class, RemoteRef.class);
            method_unbind_0 = Registry.class.getMethod("unbind", String.class);
            method_rebind_0 = Registry.class.getMethod("rebind", String.class, RemoteRef.class);
            method_list_0 = Registry.class.getMethod("list", new Class[] {});
        } catch (NoSuchMethodException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public RegistryImpl_Stub(){
        super();
    }

    public RegistryImpl_Stub(RemoteRef ref){
        super(ref);
    }

    @Override
    public RemoteRef lookup(String serviceName) throws RemoteException {
        return (RemoteRef)invoke(method_lookup_0, Util.computeMethodHash(method_lookup_0), new Object[]{serviceName});
    }

    @Override
    public void bind(String serviceName, RemoteRef ref) throws RemoteException, AlreadyBoundException {
        invoke(method_bind_0, Util.computeMethodHash(method_bind_0), new Object[] {serviceName, ref});
    }

    @Override
    public void unbind(String serviceName) throws RemoteException, NotBoundException {
        invoke(method_unbind_0, Util.computeMethodHash(method_unbind_0), new Object[] {serviceName});
    }

    @Override
    public void rebind(String serviceName, RemoteRef ref) throws RemoteException {
        invoke(method_rebind_0, Util.computeMethodHash(method_rebind_0), new Object[] {serviceName, ref});
    }

    @Override
    public String[] list() throws RemoteException {
        return (String[])invoke(method_list_0, Util.computeMethodHash(method_list_0), new Object[]{});
    }
}
