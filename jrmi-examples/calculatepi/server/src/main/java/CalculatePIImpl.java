/**
 * The implementation of CalculatePI interface.
 * 
 * @author Jian Fang
 * @author Fangyu Gao
 *
 */

public class CalculatePIImpl implements CalculatePI {
	
	/**
	 * Default constructor
	 */
    public CalculatePIImpl(){

    }

    /**
     * Calculate reciprocal squre of a number
     * @param n, intput integer
     * @return 1/(n^2)
     */
    @Override
	public double sumReciprocalSqure(int n)
    {
    	return 1/Math.pow(n, 2);
    }
    
    /**
     * Calculate sum reciprocal squre of n1 to n2.
     * 
     * @param n1, start integer
     * @param n2, end integer
     * @return 1/(n1^2)+...+1/(n2*2)
     */
    @Override
	public double sumReciprocalSqure(int n1, int n2)
	{
		double result = 0;
		for(int n = n1; n <= n2; n++) {
			result += sumReciprocalSqure(n);
		}
		return result;
	}
    
    /**
     * The last step of calculating PI
     * 
     * @param x, input
     * @return (6x)^(1/2)
     */
    @Override
	public double calPI(double x)
	{
		return Math.sqrt(6 * x);
	}
}
