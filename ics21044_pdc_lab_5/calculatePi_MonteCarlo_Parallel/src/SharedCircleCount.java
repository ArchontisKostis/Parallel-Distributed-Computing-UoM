/**
 * A class representing shared counting of points falling inside the unit circle.
 */
public class SharedCircleCount {
    private int insideCircleCount;
    private final Object lock;

    /**
     * Constructs a new SharedCircleCount object with an initial inside circle count of 0.
     * Initializes the lock object for synchronization.
     */
    public SharedCircleCount() {
        insideCircleCount = 0;
        lock = new Object();
    }

    /**
     * Increments the count of points falling inside the unit circle.
     * Synchronized to ensure thread safety.
     */
    public void incrementInsideCircleCount() {
        synchronized (lock) {
            insideCircleCount++;
        }
    }

    /**
     * Retrieves the count of points falling inside the unit circle.
     *
     * @return The count of points falling inside the unit circle.
     * Synchronized to ensure thread safety.
     */
    public int getInsideCircleCount() {
        synchronized (lock) {
            return insideCircleCount;
        }
    }
}
