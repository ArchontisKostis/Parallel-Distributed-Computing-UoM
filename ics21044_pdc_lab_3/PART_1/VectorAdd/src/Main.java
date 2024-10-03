public class Main {
    public static final int SIZE_DEFAULT = 10000;
    public static final double DEFAULT_A_VALUE = 0.0;
    public static final double DEFAULT_B_VALUE = 1.0;
    public static final double DEFAULT_C_VALUE = 0.5;
    public static final int NUM_THREADS = 4;

    
    public static void main(String[] args) {
        Data data = new Data(SIZE_DEFAULT);
        data.initialize(DEFAULT_A_VALUE, DEFAULT_B_VALUE, DEFAULT_C_VALUE);

        long startTime = System.currentTimeMillis();

        // Ο κώδικας που θα παραλληλοποιηθεί είναι στην ουσία ο κώδικας που εκτελεί
        // τον υπολογισμό του πίνακα a[] με βάση τους πίνακες b[] και c[].

        // Πίνακας για αποθήκευση των Νημάτων
        CalculationThread[] threads = new CalculationThread[NUM_THREADS];

        // Μέγεθος τμήματος που αναλαμβάνει κάθε νήμα
        int chunkSize = SIZE_DEFAULT / NUM_THREADS;

        // Δημιουργία και εκκίνηση νημάτων
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new CalculationThread(i, data, chunkSize);
            threads[i].start();
        }

        waitThreadsToFinish(threads);

        long endTime = System.currentTimeMillis();

        printDebugInfo(false, data);

        System.out.println("Time: " + (endTime - startTime) + " ms" + System.lineSeparator());
    }

    public static void waitThreadsToFinish(Thread[] threads) {
        for (int i = 0; i < NUM_THREADS; i++) {
            try { threads[i].join(); }
            catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    public static void printDebugInfo(boolean printData, Data data) {
        if (!printData) return;

        for(int i = 0; i < SIZE_DEFAULT; i++)
            System.out.println(data.a[i]);
    }
}