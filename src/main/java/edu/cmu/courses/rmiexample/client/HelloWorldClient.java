package edu.cmu.courses.rmiexample.client;

import edu.cmu.courses.rmi.IllegalStubException;
import edu.cmu.courses.rmi.NoSuchStubException;
import edu.cmu.courses.rmi.RemoteRef;

public class HelloWorldClient {
    public static void main(String[] args) {
        RemoteRef ref = new RemoteRef("localhost", 15440, "edu.cmu.courses.rmiexample.client.HelloWorldImpl");
        try {
            HelloWorldImpl_Stub stub = (HelloWorldImpl_Stub) ref.localise();
            stub.sayHello();
        } catch (NoSuchStubException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalStubException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
