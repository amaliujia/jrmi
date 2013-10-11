package edu.cmu.courses.rmiexample.client;

import edu.cmu.courses.rmiexample.common.CalculatePI;

public class CalculatePIWorker implements Runnable {
    private static double PI;

    private CalculatePI calculator;
    private int index;

    public CalculatePIWorker(CalculatePI calculator, int index){
        this.calculator = calculator;
        this.index = index;
    }

    public static synchronized void increasePI(double x) {
        PI += x;
    }

    public static double getPI(){
        return PI;
    }

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
