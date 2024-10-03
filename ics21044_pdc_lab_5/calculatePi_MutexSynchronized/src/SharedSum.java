/**
 * A class representing shared summ of values.
 */
public class SharedSum {
    private double sum;
    private static Object lock;

    /**
     * Constructs a new SharedSum object with an initial sum of 0.0.
     * Initializes the lock object for synchronization using the lock every object inherits in java
     */
    public SharedSum() {
        sum = 0.0;
        lock = new Object();
    }

    /**
     * Retrieves the current sum.
     *
     * @return The current sum.
     * synchronized to ensure thread safety.
     */
    public double getSum() {
        synchronized (lock) {
            return sum;
        }
    }

    /**
     * Adds a value to the current sum.
     *
     * @param value The value to add to the sum.
     * Synchronized to ensure thread safety.
     */
    public void add(double value) {
        synchronized (lock) {
            sum += value;
        }
    }
}
