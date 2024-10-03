import java.io.*;
import java.net.*;

public class PiClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             // Χρησιμοποιούμε το consoleIn για να διαβάσουμε την είσοδο του χρήστη από το πληκτρολόγιο
             BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            // Αντικείμενο τύπου ClientProtocol για την επικοινωνία με τον server
            ClientProtocol protocol = new ClientProtocol();
            String input;

            // Επανάληψη για την επικοινωνία με τον server
            while (true) {
                // Εμφάνιση μηνύματος στον χρήστη και ανάγνωση εισόδου
                System.out.print("Enter number of steps for π computation or -1 to exit: ");
                input = consoleIn.readLine();
                out.println(protocol.prepareRequest(Integer.parseInt(input)));

                // Έξοδος από το πρόγραμμα αν ο χρήστης εισάγει -1
                if ("-1".equals(input)) break;

                String response;

                // Διάβασμα απαντήσεων από τον server
                while ((response = in.readLine()) != null) {
                    // Επεξεργασία της απάντησης από τον server με βάση το πρωτόκολλο
                    protocol.processReply(response);

                    if (response.startsWith("Time to compute")) break;
                }
            }

        } catch (IOException e) {
            System.out.println("I/O Error: " + e);
        }
    }
}
