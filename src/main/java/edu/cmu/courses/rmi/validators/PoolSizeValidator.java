package edu.cmu.courses.rmi.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class PoolSizeValidator implements IParameterValidator {

    @Override
    public void validate(String s, String s2) throws ParameterException {
        int poolSize = Integer.parseInt(s2);
        if(poolSize <= 0)
            throw new ParameterException("pool size should be greater than 0");
    }
}