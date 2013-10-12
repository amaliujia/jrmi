package edu.cmu.courses.rmicompiler;

/**
 * The <code>UnsupportedClassException</code>
 * is throw if the class doesn't support RMI Compiler
 * 
 * @author Jian Fang
 * @author Fangyu Gao
 *
 */
public class UnsupportedClassException extends Exception {
    public UnsupportedClassException(){
        super();
    }

    public UnsupportedClassException(String message){
        super(message);
    }
}
