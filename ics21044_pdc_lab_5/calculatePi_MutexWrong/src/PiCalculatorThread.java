/**
 * PiCalculatorThread is a Thread subclass used to calculate an approximation of pi using numerical integration.
 * Each instance of PiCalculatorThread is responsible for computing a portion of the overall sum, parallelizing the computation.
 */
public class PiCalculatorThread extends Thread {
    private int start;  // Start index for the thread
    private int stop;   // Stop index for the thread
    private double step;   // Step size for the numerical integration
    private double localSum;   // Local sum of the pi approximation
    private SharedSum sharedSum; // Shared object to store the total sum

    /**
     * Constructs a PiCalculatorThread with the specified start, stop, and step values.
     *
     * @param start The start index for the summation.
     * @param stop  The stop index for the summation.
     * @param step  The step size for the numerical integration.
     */
    public PiCalculatorThread(int start, int stop, double step, SharedSum sharedSum) {
        this.start = start;
        this.stop = stop;
        this.step = step;
        this.localSum = 0.0;
        this.sharedSum = sharedSum;
    }

    /**
     * Executes the thread logic to calculate the approximation of pi using numerical integration.
     * Iterates through the specified range [start, stop) and accumulates the local sum.
     */
    @Override
    public void run() {
        for (int i = start; i < stop; i++) {
            double x = calculateX(i);
            localSum += f(x);
        }

        // Since we now use a shared object we will add the local sum to the shared object sum
        // The shared object uses locks to ensure that only one thread can add to the sum at a time
        sharedSum.add(localSum);
    }

    /**
     * Calculates the value of 'x' based on the index 'i' and the step size.
     *
     * @param i The index for which 'x' is to be calculated.
     * @return The value of 'x' calculated based on the index 'i'.
     */
    private double calculateX(int i) {
        return ((double) i + 0.5) * step;
    }

    /**
     * Computes the function value 'f(x)' for a given 'x'.
     *
     * @param x The input value for which the function 'f(x)' is to be evaluated.
     * @return The value of the function 'f(x)' evaluated at the input 'x'.
     */
    private double f(double x) {
        return 4.0 / (1.0 + x * x);
    }
}
