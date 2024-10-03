import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class PiServer {
    private static final int PORT = 1099;

    public static void main(String[] args) {
        try {
            PiCalculatorImpl calculator = new PiCalculatorImpl();
            Registry registry = LocateRegistry.createRegistry(PORT);
            registry.rebind("PiCalculator", calculator);
            System.out.println("PiCalculator service is bound and running...");
        } catch (Exception e) {
            System.out.println("RMI Server Error: " + e);
        }
    }
}
