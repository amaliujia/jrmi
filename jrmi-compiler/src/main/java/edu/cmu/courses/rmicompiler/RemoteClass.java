package edu.cmu.courses.rmicompiler;

import edu.cmu.courses.rmi.utils.Util;

import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemoteClass {
    public static final String REMOTE_CLASS = "edu.cmu.courses.rmi.Remote";
    public static final String REMOTE_EXCEPTION_CLASS = "edu.cmu.courses.rmi.exceptions.RemoteException";
    public static final String REMOTE_STUB_CLASS = "edu.cmu.courses.rmi.RemoteStub";
    public static final String REMOTE_REF_CLASS = "edu.cmu.courses.rmi.RemoteRef";

    private Class implClass;
    private Class remoteClass;
    private Class remoteExceptionClass;
    private Class[] remoteInterfaces;
    private Method[] remoteMethods;
    private URLClassLoader urlClassLoader;

    public RemoteClass(String implClassName, URLClassLoader urlClassLoader)
            throws ClassNotFoundException, UnsupportedClassException {
        this.urlClassLoader = urlClassLoader;
        init(implClassName);
        setRemoteInterfaces();
        setRemoteMethods();
    }

    public Class getImplClass(){
        return implClass;
    }

    public Class[] getRemoteInterfaces(){
        return remoteInterfaces;
    }

    public Method[] getRemoteMethods(){
        return remoteMethods;
    }

    private void init(String implClassName)
            throws ClassNotFoundException, UnsupportedClassException {
        implClass = Class.forName(implClassName, true, urlClassLoader);
        if(implClass.isInterface()){
            throw new UnsupportedClassException("Can't make stubs for an interface class");
        }
        remoteClass = Class.forName(REMOTE_CLASS, true, urlClassLoader);
        remoteExceptionClass = Class.forName(REMOTE_EXCEPTION_CLASS, true, urlClassLoader);
    }

    private boolean containsClass(List<Class> classLists, Class c){
        for(Class item : classLists){
            if(item.getName().equals(c.getName()))
                return true;
        }
        return false;
    }

    private void setRemoteInterfaces()
            throws UnsupportedClassException {
        List<Class> remotesImplemented = new ArrayList<Class>();
        for(Class c = implClass; c != null; c = c.getSuperclass()){
            Class interfaces[] = c.getInterfaces();
            for(int i = 0; i < interfaces.length; i++){
                if(!containsClass(remotesImplemented, interfaces[i]) &&
                   remoteClass.isAssignableFrom(interfaces[i])){
                    remotesImplemented.add(interfaces[i]);
                }
            }

            if(c == implClass && remotesImplemented.isEmpty()){
                if(remoteClass.isAssignableFrom(c)){
                    throw new UnsupportedClassException("The implementation class should" +
                            "implement the remote interface directly");
                } else {
                    throw new UnsupportedClassException("The implementation class should" +
                            "implement the remote interface");
                }
            }
        }
        remoteInterfaces = new Class[remotesImplemented.size()];
        remotesImplemented.toArray(remoteInterfaces);
    }

    private void setRemoteMethods()
            throws UnsupportedClassException{
        Map<Long, Method> methodMap = new HashMap<Long, Method>();
        for(Class remoteInterface : remoteInterfaces){
            calcMethods(remoteInterface, methodMap);
        }
        remoteMethods = new Method[methodMap.size()];
        methodMap.values().toArray(remoteMethods);
    }

    private void calcMethods(Class interfaceClass, Map<Long, Method> methodMap)
            throws UnsupportedClassException{
        if(!interfaceClass.isInterface()){
            throw new UnsupportedClassException("Expected interface, not class: " +
                    interfaceClass.getName());
        }
        Method methods[] = interfaceClass.getDeclaredMethods();
        for(Method method: methods){
            boolean hasRemoteException = false;
            Class exceptions[] = method.getExceptionTypes();
            for(Class exception : exceptions){
                if(remoteExceptionClass.isAssignableFrom(exception)){
                    hasRemoteException = true;
                    break;
                }
            }
            if(!hasRemoteException){
                throw new UnsupportedClassException("method " + method.getName() +
                    " of interface " + interfaceClass.getName() + " should throw " +
                    REMOTE_EXCEPTION_CLASS);
            }
            long key = Util.computeMethodHash(method);
            methodMap.put(key, method);
        }

        Class superInterfaces[] = interfaceClass.getInterfaces();
        for(Class superInterface : superInterfaces){
            calcMethods(superInterface, methodMap);
        }
    }
}
