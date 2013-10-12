import edu.cmu.courses.rmi.Remote;
import edu.cmu.courses.rmi.exceptions.RemoteException;
/**
 * Contains the CalculatePI interfaces
 * 
 * @author Jian Fang
 * @author Fangyu Gao
 *
 */
public interface CalculatePI extends Remote {
	public double sumReciprocalSqure(int n) throws RemoteException;
	public double sumReciprocalSqure(int n1, int n2) throws RemoteException;
	public double calPI(double x) throws RemoteException;
}