import java.io.*;
import java.net.Socket;

/**
 * Η κλάση αυτή αντιπροσωπευει τον MasterThread που εκτελείται στον Master.
 * Κάθε MasterThread εκτελείται σε ένα νήμα και αναλαμβάνει να επικοινωνήσει με τον έναν Worker.
 * O Worker αυτός υπολογίζει το άθροισμα των αριθμών από το myStart μέχρι το myEnd και επιστρέφει το αποτέλεσμα εδώ.
 */
class MasterThread extends Thread {
    private Socket dataSocket;
    private int myId;
    private InputStream is;
    private BufferedReader in;
    private OutputStream os;
    private PrintWriter out;
    private SharedPi mySharedPi;
    private long myStart;
    private long myEnd;
    private double step;

    public MasterThread(Socket socket, int id, SharedPi s, long myStart, long myEnd, double step) {
        this.dataSocket = socket;
        this.myId = id;
        this.mySharedPi = s;
        this.myStart = myStart;
        this.myEnd = myEnd;
        this.step = step;
        try {
            is = dataSocket.getInputStream();
            in = new BufferedReader(new InputStreamReader(is));
            os = dataSocket.getOutputStream();
            out = new PrintWriter(os, true);
        } catch (IOException e) {
            System.out.println("I/O Error " + e);
        }
    }

    /**
     * Η μέθοδος run εκτελείται όταν ξεκινάει το νήμα.
     * Αναλαμβάνει να επικοινωνήσει με τον Worker και να του στείλει τα στοιχεία που χρειάζεται για να υπολογίσει το άθροισμα.
     */
    public void run() {
        try {
            MasterProtocol app = new MasterProtocol(mySharedPi, myId);
            String outmsg = app.prepareRequest(MultithreadedSumMasterTCP.numWorkers, myStart, myEnd, step);
            out.println(outmsg);
            String inmsg = in.readLine();
            app.processReply(inmsg);
            dataSocket.close();
        } catch (IOException e) {
            System.out.println("I/O Error " + e);
        }
    }
}
