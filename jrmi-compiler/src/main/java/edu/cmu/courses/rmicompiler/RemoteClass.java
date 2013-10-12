package edu.cmu.courses.rmicompiler;

import edu.cmu.courses.rmi.utils.Util;

import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to parsing a implementation class for
 * remote interface. It will generate the remote interfaces
 * the specific class has implemented and all the methods in
 * these interfaces.
 *
 * @author Jian Fang
 * @author Fangyu Gao
 */
public class RemoteClass {
    /**
     * The class name of our basic remote classes
     */
    public static final String REMOTE_CLASS = "edu.cmu.courses.rmi.Remote";
    public static final String REMOTE_EXCEPTION_CLASS = "edu.cmu.courses.rmi.exceptions.RemoteException";
    public static final String REMOTE_STUB_CLASS = "edu.cmu.courses.rmi.RemoteStub";
    public static final String REMOTE_REF_CLASS = "edu.cmu.courses.rmi.RemoteRef";

    /**
     * The class to be parsed
     */
    private Class implClass;


    /**
     * The remote interface class
     */
    private Class remoteClass;

    /**
     * The remote exception class
     */
    private Class remoteExceptionClass;

    /**
     * All remote interfaces <code>implClass</code> has implemented
     */
    private Class[] remoteInterfaces;

    /**
     * All remote methods <code>implClass</code> has implemented
     */
    private Method[] remoteMethods;

    /**
     * The class loader
     */
    private URLClassLoader urlClassLoader;

    /**
     * The constructor of <code>RemoteClass</code>
     *
     * @param implClassName
     * @param urlClassLoader
     * @throws ClassNotFoundException
     * @throws UnsupportedClassException
     */
    public RemoteClass(String implClassName, URLClassLoader urlClassLoader)
            throws ClassNotFoundException, UnsupportedClassException {
        this.urlClassLoader = urlClassLoader;
        init(implClassName);
        setRemoteInterfaces();
        setRemoteMethods();
    }

    /**
     * Get the class to be parsed
     *
     * @return the class to be parsed
     */
    public Class getImplClass(){
        return implClass;
    }

    /**
     * Get the remote interfaces the <code>implClass</code>
     * has implemented
     *
     * @return the remote interfaces
     */
    public Class[] getRemoteInterfaces(){
        return remoteInterfaces;
    }

    /**
     * Get the remote methods the <code>implClass</code> has implemented
     *
     * @return the remote methods
     */
    public Method[] getRemoteMethods(){
        return remoteMethods;
    }

    /**
     * Initialize the <code>RemoteClass</code>. Generate classes from class
     * name, check whether the input class is valid.
     *
     * @param implClassName the name of <code>implClass</code>
     * @throws ClassNotFoundException
     * @throws UnsupportedClassException
     */
    private void init(String implClassName)
            throws ClassNotFoundException, UnsupportedClassException {
        implClass = Class.forName(implClassName, true, urlClassLoader);
        if(implClass.isInterface()){
            throw new UnsupportedClassException("Can't make stubs for an interface class");
        }
        remoteClass = Class.forName(REMOTE_CLASS, true, urlClassLoader);
        remoteExceptionClass = Class.forName(REMOTE_EXCEPTION_CLASS, true, urlClassLoader);
    }

    /**
     * Check whether a list of classes contains a specific class
     *
     * @param classLists the class list
     * @param c the class to be looked up.
     * @return true, if found, otherwise, return false
     */
    private boolean containsClass(List<Class> classLists, Class c){
        for(Class item : classLists){
            if(item.getName().equals(c.getName()))
                return true;
        }
        return false;
    }

    /**
     * Generate the remote interfaces for <code>implClass</code>
     *
     * @throws UnsupportedClassException
     */
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

    /**
     * Generate the remote methods for <code>implClass</code>
     *
     * @throws UnsupportedClassException
     */
    private void setRemoteMethods()
            throws UnsupportedClassException{
        Map<Long, Method> methodMap = new HashMap<Long, Method>();
        for(Class remoteInterface : remoteInterfaces){
            calcMethods(remoteInterface, methodMap);
        }
        remoteMethods = new Method[methodMap.size()];
        methodMap.values().toArray(remoteMethods);
    }

    /**
     * Generate the remote methods from a remote interface
     *
     * @param interfaceClass the remote interface
     * @param methodMap the map to store those methods.
     * @throws UnsupportedClassException
     */
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
