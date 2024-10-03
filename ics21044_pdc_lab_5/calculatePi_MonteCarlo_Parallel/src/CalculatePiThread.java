import java.util.Random;

/**
 * A class representing a thread that calculates Pi using the Monte Carlo method.
 */
public class CalculatePiThread extends Thread {
    private int start; 
    private int end;

    private SharedCircleCount sharedCircleCount;
    private Random random;

    /**
     * Constructs a new CalculatePiThread with the specified start and end index, and a SharedCircleCount object.
     *
     * @param start            The start index of the thread's calculation range.
     * @param end              The end index of the thread's calculation range.
     * @param sharedCircleCount The SharedCircleCount object used for shared counting.
     */
    public CalculatePiThread(int start, int end, SharedCircleCount sharedCircleCount) {
        this.start = start;
        this.end = end;
        this.sharedCircleCount = sharedCircleCount;
        this.random = new Random();
    }

    
    /**
     * Runs the thread's calculation process.
     * Generates random points within the range defined by start and end, checks if they fall inside the unit circle,
     * and increments the shared circle count accordingly.
     */
    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            double x = random.nextDouble();
            double y = random.nextDouble();

            boolean isInsideCircle = isInsideCircle(x, y);
            if (isInsideCircle) sharedCircleCount.incrementInsideCircleCount();
        }
    }

    /**
     * Checks if a given point (x, y) falls inside the unit circle.
     *
     * @param x The x-coordinate of the point.
     * @param y The y-coordinate of the point.
     * @return true if the point is inside the unit circle, false otherwise.
     */
    private boolean isInsideCircle(double x, double y) {
        return x * x + y * y <= 1;
    }
}
