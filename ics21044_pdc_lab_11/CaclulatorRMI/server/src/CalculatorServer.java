import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CalculatorServer {
    private static final int PORT = 1099;

    public static void main(String[] args) {
        try {
            // Create and export the remote object
            Calculator calc = new CalculatorImpl();

            // Create and start the registry
            Registry registry = LocateRegistry.createRegistry(PORT);

            // Bind the remote object to a name in the registry
            registry.rebind("CalculatorService", calc);

            System.out.println("Calculator service bound to registry");

        } catch (Exception e) {
            System.out.println("Calculator server exception:");
            e.printStackTrace();
        }
    }
}
