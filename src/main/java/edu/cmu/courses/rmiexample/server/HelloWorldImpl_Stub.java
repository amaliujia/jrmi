package edu.cmu.courses.rmiexample.server;

import edu.cmu.courses.rmi.RemoteRef;
import edu.cmu.courses.rmi.RemoteStub;
import edu.cmu.courses.rmi.utils.Util;
import edu.cmu.courses.rmiexample.server.HelloWorld;

import java.lang.reflect.Method;

public class HelloWorldImpl_Stub extends RemoteStub
        implements HelloWorld {
    private static Method method_say_hello_0;

    static{
        try{
            method_say_hello_0 = HelloWorld.class.getMethod("sayHello", new Class[]{});
        } catch (NoSuchMethodException e){

        }
    }

    public HelloWorldImpl_Stub(){
        super();
    }

    public HelloWorldImpl_Stub(RemoteRef ref){
        super(ref);
    }

    @Override
    public void sayHello()
            throws Exception{
        ref.invoke(method_say_hello_0, Util.computeMethodHash(method_say_hello_0), null);
    }
}
