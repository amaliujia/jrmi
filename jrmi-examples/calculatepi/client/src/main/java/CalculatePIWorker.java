/**
 * The <code>CalculatePIWorker</code> perform
 * operations to PI. It implements the
 * <code>Runnable</code> interface.
 *
 * @author Jian Fang
 * @author Fangyu Gao
 */
public class CalculatePIWorker implements Runnable {
	
	/**
	 * PI
	 */
    private static double PI;

    /**
     * operation interface
     */
    private CalculatePI calculator;
    
    /**
     * indicating the number iterations already done
     */
    private int index;

    /**
     * Constructor
     * 
     * @param calculator, operation interface
     * @param index, number iterations already done
     */
    public CalculatePIWorker(CalculatePI calculator, int index){
        this.calculator = calculator;
        this.index = index;
    }

    /**
     * PI = PI + x
     * 
     * @param x
     */
    public static synchronized void increasePI(double x) {
        PI += x;
    }

    /**
     * get PI
     * @return PI
     */
    public static double getPI(){
        return PI;
    }

    /**
     * If a a condition is not too small, continue continue calculating.
     * Here we invoke two methods with the same name sumReciprocalSqure,
     * we can see that our framework distinguish them well.
     */
    @Override
    public void run() {
        double result = 0;
        try {
            if(calculator.sumReciprocalSqure(index * 100000) > 1e-16)
                result = calculator.sumReciprocalSqure(index * 100000 + 1, (index + 1) * 100000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        increasePI(result);
    }
}
