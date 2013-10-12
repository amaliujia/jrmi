package edu.cmu.courses.rmiexamples;// Stub class source file generated by jrmi-compiler, do not edit it

import edu.cmu.courses.rmiexamples.CalculatePI;

public final class CalculatePIImpl_Stub
    extends edu.cmu.courses.rmi.RemoteStub
    implements CalculatePI
{
    
    private static java.lang.reflect.Method $method_calPI_0;
    private static java.lang.reflect.Method $method_sumReciprocalSqure_1;
    private static java.lang.reflect.Method $method_sumReciprocalSqure_2;
    static {
	    try {
		    $method_calPI_0 = CalculatePI.class.getMethod("calPI", new java.lang.Class[] {double.class});
		    $method_sumReciprocalSqure_1 = CalculatePI.class.getMethod("sumReciprocalSqure", new java.lang.Class[] {int.class});
		    $method_sumReciprocalSqure_2 = CalculatePI.class.getMethod("sumReciprocalSqure", new java.lang.Class[] {int.class, int.class});
		} catch (java.lang.NoSuchMethodException e) {
			throw new java.lang.NoSuchMethodError("stub class initialization failed");
		}
	}
	
	public CalculatePIImpl_Stub() {
	    super();
	}
	public CalculatePIImpl_Stub(edu.cmu.courses.rmi.RemoteRef ref) {
	    super(ref);
	}
	
	
	public double calPI(double $param_0)
		    throws edu.cmu.courses.rmi.exceptions.RemoteException {
		Object $result = invoke($method_calPI_0, new java.lang.Object[] {$param_0});
		return (java.lang.Double)$result;
	}
	
	public double sumReciprocalSqure(int $param_0)
		    throws edu.cmu.courses.rmi.exceptions.RemoteException {
		Object $result = invoke($method_sumReciprocalSqure_1, new java.lang.Object[] {$param_0});
		return (java.lang.Double)$result;
	}
	
	public double sumReciprocalSqure(int $param_0, int $param_1)
		    throws edu.cmu.courses.rmi.exceptions.RemoteException {
		Object $result = invoke($method_sumReciprocalSqure_2, new java.lang.Object[] {$param_0, $param_1});
		return (java.lang.Double)$result;
	}
}