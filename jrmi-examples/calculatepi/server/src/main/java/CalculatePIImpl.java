public class CalculatePIImpl implements CalculatePI {
    public CalculatePIImpl(){

    }

    @Override
	public double sumReciprocalSqure(int n)
    {
    	return 1/Math.pow(n, 2);
    }
    
    @Override
	public double sumReciprocalSqure(int n1, int n2)
	{
		double result = 0;
		for(int n = n1; n <= n2; n++) {
			result += sumReciprocalSqure(n);
		}
		return result;
	}
    
    @Override
	public double calPI(double x)
	{
		return Math.sqrt(6 * x);
	}
}
