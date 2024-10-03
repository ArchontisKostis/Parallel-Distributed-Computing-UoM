public class Main {
    private static final int TOTAL_THREADS = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {
        checkArgs(args);

        // Get numSteps from the command line
        int numSteps = getStepsFromArgs(args);
        double step = 1.0 / (double) numSteps;
        double totalSum = 0.0;

        // Create an array of PiCalculatorThread objects
        PiCalculatorThread[] threads = new PiCalculatorThread[TOTAL_THREADS];

        long startTime = System.currentTimeMillis();

        // Create and start the threads
        for (int i = 0; i < TOTAL_THREADS; i++) {
            int start = findThreadStart(i, numSteps);
            int stop = findThreadStop(i, numSteps);

            threads[i] = new PiCalculatorThread(start, stop, step);
            threads[i].start();
        }

        // Wait for the threads to finish
        // After each thread finishes, add its local sum to the total sum
        try {
            for (int i = 0; i < TOTAL_THREADS; i++) {
                threads[i].join();
                totalSum += threads[i].getLocalSum();
            }
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted.");
            e.printStackTrace();
        }

        // Multiply the total sum by the step size to get the final result
        double pi = totalSum * step;

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        System.out.println("Approximation of pi: " + pi);
        System.out.println("Time taken: " + elapsedTime + " ms");
    }

    public static void checkArgs(String[] args) {
        // Make sure we have arguments
        if (args.length < 1) {
            System.out.println("Usage: java Main <numSteps>");
            System.exit(1);
        }
    }

    public static int getStepsFromArgs(String[] args) {
        return Integer.parseInt(args[0]);
    }

    public static int findThreadStart(int threadIndex, int numSteps) {
        return threadIndex * (numSteps / TOTAL_THREADS);
    }

    public static int findThreadStop(int threadIndex, int numSteps) {
        return (threadIndex + 1) * (numSteps / TOTAL_THREADS);
    }
}