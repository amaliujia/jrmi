package edu.cmu.courses.rmiexample.server;

import edu.cmu.courses.rmi.Remote;

public interface HelloWorld extends Remote {
    void sayHello() throws Exception;
}
