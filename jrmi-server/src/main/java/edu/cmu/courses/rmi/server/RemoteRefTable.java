package edu.cmu.courses.rmi.server;

import edu.cmu.courses.rmi.Remote;
import edu.cmu.courses.rmi.RemoteRef;
import edu.cmu.courses.rmi.utils.Util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The <code>RemoteRefTable</code> contains all remote object references
 * We can use a remote object references to get the remote object
 *
 * @author Jian Fang
 * @author Fangyu Gao
 */
public class RemoteRefTable {
    /**
     * We use <code>ConcurrentHashMap</code> to make sure the
     * access to the table is thread-safe
     */
    private static Map<Long, Remote> table =
            new ConcurrentHashMap<Long, Remote>();
    private static Map<String, Map<Long, Method>> methodHashes =
            new ConcurrentHashMap<String, Map<Long, Method>>();

    /**
     * Put a remote object and its reference to the table
     *
     * @param ref the remote object's reference
     * @param obj the remote object
     */
    public static void addObject(RemoteRef ref, Remote obj){
        table.put(ref.getId(), obj);
        if(!methodHashes.containsKey(obj.getClass().getName())){
            calculateObjectMethodsHash(obj);
        }
    }

    /**
     * Find a remote object by its reference
     *
     * @param ref the remote object's reference
     * @return the remote object
     */
    public static Remote findObject(RemoteRef ref){
        return table.get(ref.getId());
    }

    /**
     * Find a method by its hash value
     *
     * @param remote the remote object
     * @param methodHash the hash value
     * @return
     */
    public static Method findObjectMethod(Remote remote, long methodHash){
        Map<Long, Method> map = methodHashes.get(remote.getClass().getName());
        return map.get(methodHash);
    }

    /**
     * Calculate all methods' hash for a obj
     *
     * @param obj
     */
    private static void calculateObjectMethodsHash(Remote obj){
        HashMap<Long, Method> map = new HashMap<Long, Method>();
        for (Class<?> cl = obj.getClass();
             cl != null;
             cl = cl.getSuperclass()){
            for (Class<?> intf : cl.getInterfaces()) {
                if (Remote.class.isAssignableFrom(intf)) {
                    for (Method method : intf.getMethods()) {
                        Method m = method;
                        map.put(Util.computeMethodHash(m), m);
                    }
                }
            }
        }
        methodHashes.put(obj.getClass().getName(), map);
    }
}
