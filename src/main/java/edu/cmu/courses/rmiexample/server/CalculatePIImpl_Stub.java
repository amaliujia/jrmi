package edu.cmu.courses.rmiexample.server;

import java.lang.reflect.Method;

import edu.cmu.courses.rmi.exceptions.RemoteException;
import edu.cmu.courses.rmi.RemoteRef;
import edu.cmu.courses.rmi.RemoteStub;
import edu.cmu.courses.rmi.utils.Util;
import edu.cmu.courses.rmiexample.common.CalculatePI;

public class CalculatePIImpl_Stub extends RemoteStub
implements CalculatePI {
private static Method method_sumReciprocalSqure_0;
private static Method method_sumReciprocalSqure_1;
private static Method method_calPI_0;
static {
try{
	method_sumReciprocalSqure_0 = CalculatePI.class.getMethod("sumReciprocalSqure", int.class);
	method_sumReciprocalSqure_1 = CalculatePI.class.getMethod("sumReciprocalSqure", int.class, int.class);
	method_calPI_0 = CalculatePI.class.getMethod("calPI", double.class);
} catch (NoSuchMethodException e){
    e.printStackTrace();
    System.exit(-1);
}
}

public CalculatePIImpl_Stub(){
super();
}

public CalculatePIImpl_Stub(RemoteRef ref){
super(ref);
}

@Override
public double sumReciprocalSqure(int n) throws RemoteException{
return (Double)invoke(method_sumReciprocalSqure_0,
		Util.computeMethodHash(method_sumReciprocalSqure_0),
		new Object[]{n});
}

@Override
public double sumReciprocalSqure(int n1, int n2) throws RemoteException{
return (Double)invoke(method_sumReciprocalSqure_1,
		Util.computeMethodHash(method_sumReciprocalSqure_1),
		new Object[]{n1, n2});
}

@Override
public double calPI(double x) throws RemoteException{
return (Double)invoke(method_calPI_0,
		Util.computeMethodHash(method_calPI_0),
		new Object[]{x});
}

}
