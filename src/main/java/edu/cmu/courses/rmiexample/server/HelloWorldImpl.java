package edu.cmu.courses.rmiexample.server;

public class HelloWorldImpl implements HelloWorld {
    public HelloWorldImpl(){

    }

    @Override
    public void sayHello() {
        System.out.println("Hello World!");
    }
}
