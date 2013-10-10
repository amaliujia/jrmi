package edu.cmu.courses.rmiexample.server;

import edu.cmu.courses.rmiexample.common.HelloWorld;

public class HelloWorldImpl implements HelloWorld {
    public HelloWorldImpl(){

    }

    @Override
    public void sayHello() {
        System.out.println("Hello World!");
    }
}
