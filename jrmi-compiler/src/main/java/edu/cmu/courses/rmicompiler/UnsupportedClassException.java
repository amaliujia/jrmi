package edu.cmu.courses.rmicompiler;

public class UnsupportedClassException extends Exception {
    public UnsupportedClassException(){
        super();
    }

    public UnsupportedClassException(String message){
        super(message);
    }
}
