/**
 * A class representing shared summation of values (incorrect implementation without synchronization).
 * This class is not thread-safe.
 */
public class SharedSum {
    private double sum;

    /**
     * Constructs a new SharedSum object with an initial sum of 0.0.
     */
    public SharedSum() {
        sum = 0.0;
    }

    /**
     * Retrieves the current sum.
     *
     * @return The current sum.
     * This method is not thread-safe.
     */
    public double getSum() {
        return sum;
    }

    /**
     * Adds a value to the current sum.
     *
     * @param value The value to add to the sum.
     * This method is not thread-safe.
     */
    public void add(double value) {
        sum += value;
    }
}
