package edu.cmu.courses.rmiexample.client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.validators.PositiveInteger;
import edu.cmu.courses.rmi.RemoteRef;
import edu.cmu.courses.rmi.registry.LocateRegistry;
import edu.cmu.courses.rmi.registry.Registry;
import edu.cmu.courses.rmi.validators.PortValidator;
import edu.cmu.courses.rmiexample.common.CalculatePI;

import java.io.IOException;

public class CalculatePIClient{
    @Parameter(names = {"-r", "--registry"},
            description = "the host of registry server")
    private String registryHost = null;

    @Parameter(names = {"-p", "--registry-port"},
            description = "the listening port of registry server",
            validateWith = PortValidator.class)
    private int registryPort = Registry.REGISTRY_PORT;

    @Parameter(names = {"-t", "--threads"},
               description = "the number of threads for calculating the PI",
               validateWith = PositiveInteger.class)
    private int threadNumber = 32;

    @Parameter(names = {"-h", "--help"},
            help = true)
    private boolean help;

    public static void main(String[] args) throws IOException, InterruptedException {
        CalculatePIClient client = new CalculatePIClient();
        JCommander jCommander = new JCommander(client, args);
        if(client.needHelp()){
            jCommander.usage();
        } else {
            client.startCalculate();
        }
    }

    public boolean needHelp(){
        return help;
    }

    private void startCalculate() throws IOException, InterruptedException {
        Registry registry = LocateRegistry.getRegistry(registryHost, registryPort);
        RemoteRef ref = registry.lookup("calculatePI");
        CalculatePI calculator = (CalculatePI)ref.localise();
        Thread[] threads = new Thread[threadNumber];
        for(int i = 0; i < threadNumber; i++){
            Thread thread = new Thread(new CalculatePIWorker(calculator, i));
            threads[i] = thread;
            thread.start();
        }
        for(int i = 0; i < threadNumber; i++){
            threads[i].join();
        }
        double PI = calculator.calPI(CalculatePIWorker.getPI());
        System.out.println(PI);
    }
}
