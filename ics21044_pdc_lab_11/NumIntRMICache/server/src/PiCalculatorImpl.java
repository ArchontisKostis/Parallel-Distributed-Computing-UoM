import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Optional;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class PiCalculatorImpl extends UnicastRemoteObject implements PiCalculator {

    private static final int LIMIT = 1000000;

    private final ServerCache cache;

    protected PiCalculatorImpl() throws RemoteException {
        super();
        this.cache = new ServerCache();
    }

    @Override
    public String computePi(int numSteps) throws RemoteException {
        if (numSteps == -1) return "EXIT";

        // Ελεγχος αν το π για τον ζητουμενο αριθμο βημάτων εχει υπολογιστεί ηδη και βρίσκεται στην cache
        Optional<Double> cachedValue = cache.getItem(numSteps);
        if (cachedValue.isPresent()) return String.format("Cached pi = %22.20f\n", cachedValue.get());

        // Θα φτάσουμε εδω μονο αν το π για τον δωθεν αριθμο βημάτων ΔΕΝ εχει ήδη υπολογιστεί και ΔΕΝ βρίσκεται στην cache
        long startTime = System.currentTimeMillis();
        double pi = computePiValue(numSteps);
        long endTime = System.currentTimeMillis();

        // Αποθήκευση του Π που υπολογίστηκε στην cache
        cache.put(numSteps, pi);

        return String.format("Computed pi = %22.20f\nTime to compute = %f seconds\n", pi, (double) (endTime - startTime) / 1000);
    }

    private double computePiValue(int numSteps) {
        double step = 1.0 / numSteps;
        double sum = 0.0;
        for (int i = 0; i < numSteps; ++i) {
            double x = ((double)i + 0.5) * step;
            sum += 4.0 / (1.0 + x * x);
        }
        double pi = sum * step;

        return pi;
    }

}
