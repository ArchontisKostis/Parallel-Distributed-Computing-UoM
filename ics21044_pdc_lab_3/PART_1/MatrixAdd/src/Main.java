public class Main {
    // ΚΑΘΟΛΙΚΕΣ ΜΕΤΑΒΛΗΤΕΣ
    public static final int SIZE_DEFAULT = 10000;
    public static final double DEFAULT_A_VALUE = 0.1;
    public static final double DEFAULT_B_VALUE = 0.3;
    public static final double DEFAULT_C_VALUE = 0.5;
    public static final int NUM_THREADS = 4;

    public static void main(String[] args) {
        System.out.println("PARALLEL EXECUTION" + System.lineSeparator());

        Data data = new Data(SIZE_DEFAULT);
        data.initialize(DEFAULT_A_VALUE, DEFAULT_B_VALUE, DEFAULT_C_VALUE);

        long startTime = System.currentTimeMillis();

        // Πίνακας για αποθήκευση των Νημάτων
        MatrixCalcThread[] threads = new MatrixCalcThread[NUM_THREADS];

        // Μέγεθος τμήματος που αναλαμβάνει να υπολογίσει κάθε νήμα
        int chunkSize = SIZE_DEFAULT / NUM_THREADS;

        // Δημιουργία και εκκίνηση νημάτων
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new MatrixCalcThread(i, data, chunkSize);
            threads[i].start();
        }

        waitThreadsToFinish(threads);

        long endTime = System.currentTimeMillis();

        printDebugInfo(true, data);

        System.out.println("Time: " + (endTime - startTime) + " ms" + System.lineSeparator());
    }

    // Αναμονή τερματισμού όλων των νημάτων
    public static void waitThreadsToFinish(Thread[] threads) {
        for (int i = 0; i < NUM_THREADS; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void printDebugInfo(boolean printData, Data data) {
        if (!printData) return;

        // for debugging
        for (int i = 0; i < SIZE_DEFAULT; i++) {
            for (int j = 0; j < SIZE_DEFAULT; j++)
                System.out.print(data.a[i][j] + " ");
            System.out.println();
        }
    }
}