import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Η κλάση MultithreadedSumMasterTCP δημιουργεί έναν server socket στην πόρτα 1234 και δημιουργεί 4 worker threads.
 * Αντιπροσωπεύει τον Master και αναλαμβάνει να επικοινωνήσει με τους workers.
 * Μολις δημιουργηθούν οι worker threads, ο Master περιμένει να τελειώσουν και μετά εμφανίζει το αποτέλεσμα, συνδυάζοντας τα αποτελέσματα των workers.
 */
public class MultithreadedSumMasterTCP {
    private static final int PORT = 1234;
    public static final int numWorkers = 4; // Number of worker threads
    private static final long numSteps = 10000L; // Number of steps for π calculation
    private static final double step = 1.0 / (double) numSteps;

    public static void main(String args[]) throws IOException {
        ServerSocket connectionSocket = new ServerSocket(PORT);
        SharedPi sharedPi = new SharedPi();
        MasterThread[] mthread = new MasterThread[numWorkers];

        for (int i = 0; i < numWorkers; i++) {
            long myStart = i * (numSteps / numWorkers);
            long myEnd = (i + 1) * (numSteps / numWorkers);

            Socket dataSocket = connectionSocket.accept();
            mthread[i] = new MasterThread(dataSocket, i, sharedPi, myStart, myEnd, step);
            mthread[i].start();
        }
        System.out.println("All Started");

        for (int i = 0; i < numWorkers; i++) {
            try {
                mthread[i].join();
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted: " + e.getMessage());
            }
        }

        sharedPi.printResult();
    }
}
