import java.rmi.Remote;

public interface Calculator extends Remote {
    String calculate(String expression) throws Exception;
}
