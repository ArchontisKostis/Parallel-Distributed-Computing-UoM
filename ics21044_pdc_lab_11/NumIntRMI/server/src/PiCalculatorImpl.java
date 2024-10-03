import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class PiCalculatorImpl extends UnicastRemoteObject implements PiCalculator {

    private static final int LIMIT = 1000000; // Threshold for splitting tasks

    protected PiCalculatorImpl() throws RemoteException { super(); }

    @Override
    public String computePi(int numSteps) throws RemoteException {
        if (numSteps == -1) return "EXIT";

        long startTime = System.currentTimeMillis();
        double pi = computePiValue(numSteps);
        long endTime = System.currentTimeMillis();

        return String.format("Computed pi = %22.20f\nTime to compute = %f seconds\n", pi, (double) (endTime - startTime) / 1000);
    }

    private double computePiValue(int numSteps) {
        double step = 1.0 / (double) numSteps;
        ForkJoinPool pool = new ForkJoinPool();
        PiTask rootTask = new PiTask(0, numSteps, step);
        return pool.invoke(rootTask);
    }

    // Private εσωτερική κλάση για τον υπολογισμό του π παράλληλα χρησιμοποιώντας αναδρομικές εργασίες
    private static class PiTask extends RecursiveTask<Double> {
        // Δείκτης εκκίνησης των εργασιών υπολογισμου για το νήμα
        // Το νήμα θα υπολογίσει την τιμή για ευρος [start, end)
        private final int start;

        // Δείκτης τέλους των εργασιών υπολογισμου για το νήμα
        // Το νήμα θα υπολογίσει την τιμή για ευρος [start, end)
        private final int end;

        // Βήμα για τον υπολογισμό της τιμής του π
        // Το βήμα είναι η απόσταση μεταξύ των segment του ευρους [start, end)
        private final double step;

        PiTask(int start, int end, double step) {
            this.start = start;
            this.end = end;
            this.step = step;
        }

        @Override
        protected Double compute() {
            int range = end - start;

            if (range <= LIMIT) return computeDirectly();
            else return computeRecursively(range);
        }

        // Υπολογισμός της τιμής του π αναδρομικά. Η εργασία χωρίζεται σε δύο υποεργασίες
        // Οι εργασιες αντιπροσωπεύονται από τις μεταβλητές leftTask και rightTask
        // Η εργασία αυτή υπολογίζει την τιμή του π για το εύρος [start, end)
        // Μολις ολοκληρωθεί η υπολογιστική διαδικασία, επιστρέφεται το αποτέλεσμα των δύο υποεργασιών
        private Double computeRecursively(int range) {
            int mid = start + range / 2;
            PiTask leftTask = new PiTask(start, mid, step);
            PiTask rightTask = new PiTask(mid, end, step);
            leftTask.fork();
            Double rightResult = rightTask.compute();
            Double leftResult = leftTask.join();

            return leftResult + rightResult;
        }

        // Υπολογισμός της τιμής του π απευθείας χωρίς αναδρομική διαδικασία
        // Η εργασία αυτή υπολογίζει την τιμή του π για το εύρος [start, end)
        private Double computeDirectly() {
            double sum = 0.0;
            for (int i = start; i < end; i++) {
                double x = ((double) i + 0.5) * step;
                sum += 4.0 / (1.0 + x * x);
            }
            return sum * step;
        }
    }
}
