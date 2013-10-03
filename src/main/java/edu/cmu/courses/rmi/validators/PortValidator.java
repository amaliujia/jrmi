package edu.cmu.courses.rmi.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class PortValidator implements IParameterValidator {

    @Override
    public void validate(String s, String s2) throws ParameterException {
        int port = Integer.parseInt(s2);
        if(port <= 0 || port >= 65536)
            throw new ParameterException("port number should in the range between 0 and 65535");
    }
}