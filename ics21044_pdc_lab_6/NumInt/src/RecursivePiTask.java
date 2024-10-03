public class RecursivePiTask extends Thread {
    private long myStart;
    private long myEnd;
    private double step;
    private long limit;
    private double myResult;

    private long myWorkload;

    public RecursivePiTask(long start, long end, double step, long limit) {
        this.myStart = start;
        this.myEnd = end;
        this.step = step;
        this.limit = limit;
        this.myWorkload = end - start;
    }

    @Override
    public void run() {
//        System.out.println("Thread " + Thread.currentThread().getName() + " is running");

        if (myWorkload <= limit) {  // Αν το μέγεθος του τμήματος εργασίας είναι μικρότερο από το όριο, τότε κάνουμε τον υπολογισμό απευθείας.
            // Base Case: Δεν χρειάζεται να κάνουμε αναδρομική κλήση
            this.myResult = computeSumNow();
        }
        else {
            // Recursive Case: Χρειάζεται να κάνουμε αναδρομική κλήση
            calculateRecursively();
        }
    }

    private double computeSumNow() {
        double sum = 0.0;
        for (long i = myStart; i < myEnd; ++i) {
            double x = ((double)i + 0.5) * step;
            sum += 4.0 / (1.0 + x * x);
        }
        return sum;
    }

    private void calculateRecursively() {
        long mid = myStart + myWorkload / 2;
        RecursivePiTask left = new RecursivePiTask(myStart, mid, step, limit);
        RecursivePiTask right = new RecursivePiTask(mid, myEnd, step, limit);

        left.start();
        right.start();

        try {
            left.join();
            right.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.myResult = left.myResult + right.myResult;
    }

    public double getMyResult() {
        return myResult;
    }
}
