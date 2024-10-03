import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PiCalculator extends Remote {
    String computePi(int numSteps) throws RemoteException;
}
