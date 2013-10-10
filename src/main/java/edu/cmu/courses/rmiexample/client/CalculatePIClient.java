package edu.cmu.courses.rmiexample.client;

import edu.cmu.courses.rmi.exceptions.IllegalStubException;
import edu.cmu.courses.rmi.exceptions.NoSuchStubException;
import edu.cmu.courses.rmi.RemoteRef;
import edu.cmu.courses.rmi.registry.LocateRegistry;
import edu.cmu.courses.rmiexample.common.CalculatePI;

public class CalculatePIClient implements Runnable{
	private static double PI;
	private static CalculatePI stub;
	private int n;
	public synchronized void increasePI(double x) {
        PI += x;
    }
	public CalculatePIClient(int n)
	{
		this.n = n;
	}
    public static void main(String[] args) {
        RemoteRef ref;
        try {
            ref = LocateRegistry.getRegistry().lookup("calculatePI");
            if(ref != null){
            	
                stub = (CalculatePI)ref.localise();
                int times = 1000;
                Thread[] threads = new Thread[times];
                for(int i = 0; i < times; i++)
                {
                	//System.out.println(i);
                	Runnable r = new CalculatePIClient(i);
                	threads[i] = new Thread(r);
                	threads[i].run();
                }
                for(int i = 0; i < times; i++)
                {
                	
                	threads[i].join();
                }
                System.out.println(stub.calPI(PI));
            } else {
                System.out.println("Ooops!");
            }
        } catch (NoSuchStubException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalStubException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

	@Override
	public void run() {
		double result = 0;
		try {
			//System.out.println(stub.sumReciprocalSqure(n*10000));
			if(stub.sumReciprocalSqure(n*100000) > 1e-16)
			result = stub.sumReciprocalSqure(n*100000+1, (n+1)*100000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		increasePI(result);
	}
}
