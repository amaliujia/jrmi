package edu.cmu.courses.rmiexample.client;

import edu.cmu.courses.rmi.exceptions.IllegalStubException;
import edu.cmu.courses.rmi.exceptions.NoSuchStubException;
import edu.cmu.courses.rmi.RemoteRef;
import edu.cmu.courses.rmi.registry.LocateRegistry;
import edu.cmu.courses.rmiexample.common.HelloWorld;

public class HelloWorldClient {
    public static void main(String[] args) {
        RemoteRef ref;
        try {
            ref = LocateRegistry.getRegistry().lookup("hello");
            if(ref != null){
            	
                HelloWorld stub = (HelloWorld)ref.localise();
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
