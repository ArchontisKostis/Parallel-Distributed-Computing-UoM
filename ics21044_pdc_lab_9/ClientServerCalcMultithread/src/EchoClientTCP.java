import java.io.*;
import java.net.*;

public class EchoClientTCP {
    private static final String HOST = "localhost";
    private static final int PORT = 1234;
    private static final String EXIT = "!";

    public static void main(String[] args) {
        try {
            // Δημιουργία του Socket για την επικοινωνία με τον Server
            Socket dataSocket = new Socket(HOST, PORT);
            System.out.println("Connection to " + HOST + " established");

            // Δημιουργία των Streams για την ανταλλαγή δεδομένων με τον Server
            InputStream is = dataSocket.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            OutputStream os = dataSocket.getOutputStream();
            PrintWriter out = new PrintWriter(os, true);

            // Αρχικοποίηση του πρωτοκόλλου επικοινωνίας που θα ακολουθήσει ο Client
            ClientProtocolCalculator app = new ClientProtocolCalculator();

            // Επικοινωνία με τον Server (γινεται μέχρι ο Client να στείλει το μήνυμα "!")
            String outmsg = app.prepareRequest();
            while (!outmsg.equals(EXIT)) {
                out.println(outmsg);
                System.out.println("Sent to server: " + outmsg); // Debugging output

                String inmsg = in.readLine();
                System.out.println("Received from server: " + inmsg); // Debugging output

                app.processReply(inmsg);
                outmsg = app.prepareRequest();
            }

            // Κλείσιμο των Streams και του Socket
            dataSocket.close();
            System.out.println("Data Socket closed");

        } catch (IOException e) {
            System.out.println("I/O Error: " + e.getMessage());
        }
    }
}
