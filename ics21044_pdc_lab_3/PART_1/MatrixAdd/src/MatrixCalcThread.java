public class MatrixCalcThread extends Thread {
    private final int myID;
    private Data data;
    private int myStart;
    private int myStop;

    public MatrixCalcThread(int i, Data data, int chunkSize) {
        this.myID = i;
        this.data = data;
        this.myStart = i * chunkSize;
        this.myStop = (i == Main.NUM_THREADS - 1) ? Main.SIZE_DEFAULT : (i + 1) * chunkSize;
    }

    @Override
    public void run() {
        // Αυτό είναι το μέρος του κώδικα που μπορεί να επωφεληθεί από την παράλληλη εκτέλεση.
        // Αυτό γίνεται γιατι ο υπολογισμός του πίνακα a[χ][y] είναι ανεξάρτητος.
        for (int i = myStart; i < myStop; i++)
            for (int j = 0; j < Main.SIZE_DEFAULT; j++)
                data.a[i][j] = data.b[i][j] + data.c[i][j];
    }
}
