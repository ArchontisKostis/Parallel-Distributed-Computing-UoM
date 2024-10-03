import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CalculatorImpl extends UnicastRemoteObject implements Calculator {
    private static final long serialVersionUID = 1L;

    private final CalculatorService calculatorService;

    public CalculatorImpl() throws RemoteException {
        super();
        calculatorService = new CalculatorService();
    }

    @Override
    public String calculate(String operation) throws RemoteException {
        System.out.println("> Received Request: " + operation + " (Timestamp: " + System.currentTimeMillis() + ")");

        // Delegate the request to the CalculatorService
        String result = calculatorService.processRequest(operation);

        System.out.println("> Sending Response: R " + result);
        return result;
    }
}
