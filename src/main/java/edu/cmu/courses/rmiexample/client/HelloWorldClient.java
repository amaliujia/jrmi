package edu.cmu.courses.rmiexample.client;

import edu.cmu.courses.rmi.IllegalStubException;
import edu.cmu.courses.rmi.NoSuchStubException;
import edu.cmu.courses.rmi.RemoteRef;
import edu.cmu.courses.rmi.registry.LocateRegistry;
import edu.cmu.courses.rmiexample.server.HelloWorldImpl_Stub;

public class HelloWorldClient {
    public static void main(String[] args) {
        RemoteRef ref;
        try {
            ref = LocateRegistry.getRegistry().lookup("hello");
            if(ref != null){
                HelloWorldImpl_Stub stub = (HelloWorldImpl_Stub) ref.localise();
                stub.sayHello();
            } else {
                System.out.println("Ooops!");
            }
        } catch (NoSuchStubException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalStubException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
