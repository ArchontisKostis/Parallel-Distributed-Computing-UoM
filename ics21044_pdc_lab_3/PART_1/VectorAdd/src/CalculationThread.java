public class CalculationThread extends Thread {
    private final int myID;
    private Data data;
    private int myStart;
    private int myStop;

    public CalculationThread(int i, Data data, int chunkSize) {
        this.myID = i;
        this.data = data;

        // Υπολογισμός του τμήματος που θα υπολογίσει ο κάθε νήμα (από myStart μέχρι myStop)
        this.myStart = i * chunkSize;
        this.myStop = (i == Main.NUM_THREADS - 1) ? Main.SIZE_DEFAULT : (i + 1) * chunkSize;
    }

    @Override
    public void run() {
        for (int i = myStart; i < myStop; i++) {
            // αυτό είναι το μέρος του κώδικα που μπορεί να επωφεληθεί από την παράλληλη εκτέλεση.
            // Αυτό γίνεται γιατι ο υπολογισμός του πίνακα a[] είναι ανεξάρτητος για κάθε i.
            data.a[i] = data.b[i] + data.c[i];
        }
    }

    public void printInfo() {
        System.out.println("-------------------------------------------------");
        System.out.println("Thread ID: " + myID);
        System.out.println("Start: " + myStart);
        System.out.println("Stop: " + myStop);
        System.out.println("-------------------------------------------------");
    }
}
