public class Main {
    public static void main(String[] args) {
        // Δημιουργία αντικειμένου MyThread
        MyThread myThread = new MyThread("Hello from MyThread!");
        // Εκκίνηση του MyThread
        myThread.start();

        // Δημιουργία αντικειμένου MyRunnable
        Thread myRunnableThread = new Thread(new MyRunnable("Hello from MyRunnable!"));
        // Εκκίνηση του MyRunnable
        myRunnableThread.start();

        // Αναμονή των νημάτων MyThread και MyRunnable να τελειώσουν
        try {
            myThread.join();
            myRunnableThread.join();
        }
        catch (InterruptedException e) { e.printStackTrace(); }

        System.out.println("Main thread finished.");
    }
}
