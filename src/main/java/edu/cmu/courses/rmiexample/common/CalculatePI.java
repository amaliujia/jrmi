package edu.cmu.courses.rmiexample.common;

import edu.cmu.courses.rmi.Remote;

public interface CalculatePI extends Remote {
	public double sumReciprocalSqure(int n) throws Exception;
	public double sumReciprocalSqure(int n1, int n2) throws Exception;
	public double calPI(double x) throws Exception;
}