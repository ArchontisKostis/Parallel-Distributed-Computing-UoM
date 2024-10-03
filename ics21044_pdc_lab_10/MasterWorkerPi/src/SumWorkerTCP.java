import java.io.*;
import java.net.Socket;

/**
 * Η κλάση αυτή αναπαριστά έναν Worker που συνδέεται με τον Master.
 * Περιλαμβάνεται μια main κλάση που μπορούμε να "τρέξουμε" για όσους εργάτες υποστηρίζει ο Master.
 * Κάθε Worker υπολογίζει το άθροισμα των αριθμών από το start μέχρι το end και το στέλνει πίσω στον Master.
 */
public class SumWorkerTCP {
    private static final String HOST = "localhost";
    private static final int PORT = 1234;

    public static void main(String args[]) throws IOException {
        // Αρχικοποίηση του socket για την επικοινωνία με τον Master
        Socket dataSocket = new Socket(HOST, PORT);

        // Αρχικοποίηση των streams για την ανάγνωση και εγγραφή δεδομένων
        InputStream is = dataSocket.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        OutputStream os = dataSocket.getOutputStream();
        PrintWriter out = new PrintWriter(os, true);

        // Διάβασμα των παραμέτρων που λαμβάνει ο Worker από τον Master
        String inmsg = in.readLine();
        String[] parts = inmsg.split("\\s+");

        // Αρχικοποίηση των παραμέτρων που λαμβάνει ο Worker από τον Master
        long myStart = Long.parseLong(parts[2]);
        long myEnd = Long.parseLong(parts[3]);
        double step = Double.parseDouble(parts[4]);

        // Αρχικοποίηση του WorkerProtocol που χρησιμοποιείται για την επικοινωνία με τον Master
        WorkerProtocol app = new WorkerProtocol(myStart, myEnd, step);

        // Εκτέλεση του υπολογισμού και επιστροφή του αποτελέσματος στον Master
        String outmsg = app.compute(inmsg);
        out.println(outmsg);

        // Κλείσιμο του socket
        dataSocket.close();
    }
}
