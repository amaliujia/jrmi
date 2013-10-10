package edu.cmu.courses.rmiexample.common;

import edu.cmu.courses.rmi.Remote;

public interface HelloWorld extends Remote {
    void sayHello() throws Exception;
}
