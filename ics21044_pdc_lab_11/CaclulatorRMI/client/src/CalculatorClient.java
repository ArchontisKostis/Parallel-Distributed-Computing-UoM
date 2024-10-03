import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class CalculatorClient {
    private static final String HOST = "localhost";
    private static final int PORT = 1099;

    public static void main(String[] args) {
        try {
            // Connect to the RMI registry
            Registry registry = LocateRegistry.getRegistry(HOST, PORT);

            // Lookup the remote object
            Calculator calc = (Calculator) registry.lookup("CalculatorService");

            Scanner scanner = new Scanner(System.in);
            String request;

            while (true) {
                System.out.println("Enter operation and operands (e.g., + 5 3): ");
                request = scanner.nextLine();

                if (request.equals("!")) {
                    System.out.println("Exiting...");
                    break;
                }

                // Call the remote method
                String response = calc.calculate(request);
                System.out.println("Response: " + response);
            }

            scanner.close();

        } catch (Exception e) {
            System.out.println("Calculator client exception:");
            e.printStackTrace();
        }
    }
}
