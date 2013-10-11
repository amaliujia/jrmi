package edu.cmu.courses.rmiexample.common;

import edu.cmu.courses.rmi.Remote;
import edu.cmu.courses.rmi.exceptions.RemoteException;

public interface CalculatePI extends Remote {
	public double sumReciprocalSqure(int n) throws RemoteException;
	public double sumReciprocalSqure(int n1, int n2) throws RemoteException;
	public double calPI(double x) throws RemoteException;
}