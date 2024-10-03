import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A class representing shared sum of the values.
 */
public class SharedSum {
    private double sum;
    private static Lock lock;

    /**
     * Constructs a new SharedSum object with an initial sum of 0.0.
     * Initializes the lock object for synchronization using a ReentrantLock.
     */
    public SharedSum() {
        sum = 0.0;
        lock = new ReentrantLock();
    }

    /**
     * Retrieves the current sum.
     *
     * @return The current sum.
     * Uses ReentrantLock to ensure thread safety.
     */
    public double getSum() {
        lock.lock();
        try {
            return sum;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Adds a value to the current sum.
     *
     * @param value The value to add to the sum.
     * Uses ReentrantLock to ensure thread safety.
     */
    public void add(double value) {
        lock.lock();
        try {
            sum += value;
        } finally {
            lock.unlock();
        }
    }
}
