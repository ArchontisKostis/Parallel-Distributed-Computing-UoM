// Κλαση που χρησιμοποιείται για τον υπολογισμό του αθροίσματος από myStart έως myEnd χρησιμοποιώντας αναδρομή
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
        if (myWorkload <= limit) {
            // Base Case: Απευθείας υπολογισμός του αθροίσματος χωρις αναδρομή
            this.myResult = computeSumNow();
        } else {
            // Recursive Case: Υπολογισμός του αθροίσματος σπάζονατς το σε tasks με αναδρομή
            calculateRecursively();
        }
    }

    // Υπολογίζει το αθροίσμα απευθείας χωρίς να χρησιμοποιήσει αναδρομή
    private double computeSumNow() {
        double sum = 0.0;
        for (long i = myStart; i < myEnd; ++i) {
            double x = ((double)i + 0.5) * step;
            sum += 4.0 / (1.0 + x * x);
        }
        return sum;
    }

    // Εκτελει τον υπολογισμό με αναδρομη. Πρακτικα δημιουργει δυο αντικειμενα RecursivePiTask
    // Καθε αντικειμενο υπολογιζει το αθροισμα απο το myStart εως το myEnd και στη συνεχεια η μέθοδος συνδυαζει τα αποτελεσματα
    private void calculateRecursively() {
        long mid = myStart + myWorkload / 2;
        RecursivePiTask left = new RecursivePiTask(myStart, mid, step, limit);
        RecursivePiTask right = new RecursivePiTask(mid, myEnd, step, limit);

        // Start both subtasks
        left.start();
        right.start();

        try {
            // Wait for both subtasks to complete
            left.join();
            right.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Combine the results from both subtasks
        this.myResult = left.myResult + right.myResult;
    }

    public double getMyResult() {
        return myResult;
    }
}
