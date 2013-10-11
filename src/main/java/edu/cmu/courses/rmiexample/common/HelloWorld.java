package edu.cmu.courses.rmiexample.common;

import edu.cmu.courses.rmi.Remote;
import edu.cmu.courses.rmi.exceptions.RemoteException;

public interface HelloWorld extends Remote {
    void sayHello() throws RemoteException;
}
