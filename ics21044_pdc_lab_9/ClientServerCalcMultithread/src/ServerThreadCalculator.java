import java.io.*;
import java.net.*;

// Η κλαση αυτη χρησιμοποιειται για να μπορουμε να εξυπηρετουμε πολλαπλους clients ταυτοχρονα
// ΛΕΙΤΟΥΡΓΙΑ ΤΗΣ ΚΛΑΣΗΣ:
// Η κλαση αυτη ειναι υπευθυνη για την εξυπηρετηση των αιτησεων των clients. Καθε φορα που ενας client
// συνδεεται στον server, δημιουργειται ενα instance της κλασης αυτης για τον συγκεκριμενο client.
// Η κλαση αυτη κληρονομει την κλαση Thread και υλοποιει τη μεθοδο run. Στη μεθοδο run, διαβαζουμε
// τα δεδομενα που μας στελνει ο client, τα επεξεργαζομαστε και στη συνεχεια στελνουμε την απαντηση
// πισω στον client. Καθώς η κλάση αυτή κληρονομεί την κλάση Thread, μπορούμε να δημιουργήσουμε
// πολλαπλά instances της και να εξυπηρετήσουμε πολλαπλούς clients ταυτόχρονα.
public class ServerThreadCalculator extends Thread {
    private Socket clientSocket;
    private InputStream is;
    private BufferedReader in;
    private OutputStream os;
    private PrintWriter out;
    private static final String EXIT = "!";

    public ServerThreadCalculator(Socket socket) {
        this.clientSocket = socket;
        try {
            is = clientSocket.getInputStream();
            in = new BufferedReader(new InputStreamReader(is));
            os = clientSocket.getOutputStream();
            out = new PrintWriter(os, true);
        } catch (IOException e) {
            System.out.println("I/O Error " + e);
        }
    }

    @Override
    public void run() {
        try {
            // Αρχικοποίηση του πρωτοκόλλου επικοινωνίας που θα ακολουθήσει ο Server
            ServerProtocolCalculator app = new ServerProtocolCalculator();

            String inmsg;
            while ((inmsg = in.readLine()) != null) {
                if (inmsg.equals(EXIT)) {
                    // Εμφανίζουμε μήνυμα στον server ότι ο client ζήτησε να τερματίσει την επικοινωνία
                    System.out.println("Exit command received from client. Server shutting down...");
                    out.println("Server shutting down.");
                    break;
                }

                // Θα φτάσουμε εδώ μόνο αν το μήνυμα που λάβαμε δεν είναι το EXIT

                // Επεξεργασία του request που λαμβάνει ο Server από τον Client και εκτέλεση της αντίστοιχης πράξης
                String outmsg = app.processRequest(inmsg);
                System.out.println("Sending response <" + outmsg + "> to client : " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
                out.println(outmsg);
            }
        } catch (IOException e) {
            System.out.println("I/O Error " + e); // (Κατι πηγε στραβα στην επικοινωνία με τον client)
        } finally {
            // Κλείσιμο των Streams και του Socket
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Error closing socket: " + e); // (Κατι πηγε στραβα στο κλείσιμο του socket)
            }
        }
    }
}
