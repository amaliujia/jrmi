package edu.cmu.courses.rmi.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class RemoteFormatValidator implements IParameterValidator {
    @Override
    public void validate(String s, String s2) throws ParameterException {
        String[] words = s2.split("@");
        if(words.length != 2){
            throw new ParameterException("wrong format for remote object name and service name, " +
                    "use format className@serviceName");
        }
    }
}