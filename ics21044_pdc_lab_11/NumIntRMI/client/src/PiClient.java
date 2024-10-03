import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class PiClient {
    private static final String HOST = "localhost";
    private static final int PORT = 1099;
    private static final boolean RUNNING = true;

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry(HOST, PORT);
            PiCalculator calculator = (PiCalculator) registry.lookup("PiCalculator");
            Scanner scanner = new Scanner(System.in);

            while (RUNNING) {
                System.out.print("Enter number of steps for π computation or -1 to exit: ");
                int numSteps = Integer.parseInt(scanner.nextLine());

                String response = calculator.computePi(numSteps);
                System.out.println(response);

                if ("EXIT".equals(response.trim())) break;
            }
            scanner.close();
        }

        catch (Exception e) { System.out.println("RMI Client Error: " + e); }
    }
}
