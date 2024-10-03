public class Main {
    public static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {
        checkArgs(args);

        int totalPoints = Integer.parseInt(args[0]);
        SharedCircleCount sharedCircleCount = new SharedCircleCount();

        CalculatePiThread[] threads = new CalculatePiThread[NUM_THREADS];
        int pointsPerThread = totalPoints / NUM_THREADS;

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < NUM_THREADS; i++) {
            int start = i * pointsPerThread;
            int end = (i == NUM_THREADS - 1) ? totalPoints : start + pointsPerThread;
            threads[i] = new CalculatePiThread(start, end, sharedCircleCount);
            threads[i].start();
        }

        waitForThreadsToFinish(threads);


        // Get the total number of points inside the circle
        int insideCircleCount = sharedCircleCount.getInsideCircleCount();

        // Calculate the approximation of pi using the ratio of points inside the circle to total points
        double piApproximation = 4.0 * insideCircleCount / totalPoints;

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        // Print the approximation of pi
        System.out.println("Approximation of pi using Monte Carlo method: " + piApproximation);
        System.out.println("Elapsed time: " + elapsedTime + " ms");

    }

    public static void checkArgs(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java Main <number>");
            System.exit(1);
        }
    }

    public static void waitForThreadsToFinish(CalculatePiThread[] threads) {
        for (CalculatePiThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}