public class Main {
    public static final int TOTAL_THREADS = 10;

    public static void main(String[] args) {
        // Δομες για την αποθήκευση των αντικειμένων MyThread και MyRunnable
        MyThread[] myThreads = new MyThread[10];
        Thread[] myRunnableThreads = new Thread[10];

        // Δημιουργία και εκκίνηση αντικειμένων MyThread
        for (int i = 0; i < TOTAL_THREADS; i++) {
            myThreads[i] = new MyThread("Hello from MyThread " + (i + 1) + "!");
            myThreads[i].start();
        }

        // Δημιουργία και εκκίνηση αντικειμένων MyRunnable
        for (int i = 0; i < TOTAL_THREADS; i++) {
            myRunnableThreads[i] = new Thread(new MyRunnable("Hello from MyRunnable " + (i + 1) + "!"));
            myRunnableThreads[i].start();
        }

        // Αναμονή των νημάτων MyThread και MyRunnable να τελειώσουν
        try {
            for (int i = 0; i < TOTAL_THREADS; i++) {
                myThreads[i].join();
                myRunnableThreads[i].join();
            }
        }
        catch (InterruptedException e) { e.printStackTrace(); }

        System.out.println("Main thread finished.");
    }
}
