import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PiClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            // Δημιουργία αντικειμένου τύπου ClientProtocol για την επικοινωνία με τον server
            ClientProtocol protocol = new ClientProtocol();
            String input;

            // Επανάληψη για την επικοινωνία με τον server
            while (true) {
                // Εμφάνιση μηνύματος στον χρήστη και ανάγνωση εισόδου
                System.out.print("Enter number of steps for π computation or -1 to exit: ");
                input = consoleIn.readLine();

                // Αποστολή αιτήματος στον server
                out.println(protocol.prepareRequest(Integer.parseInt(input)));

                // Έξοδος από το πρόγραμμα αν ο χρήστης εισάγει -1
                if ("-1".equals(input)) break;

                // Διάβασμα απαντήσεων από τον server συνεχίζεται μέχρι να λάβουμε το μήνυμα "Time to compute"
                String response;
                while ((response = in.readLine()) != null) {
                    protocol.processReply(response);
                    if (response.startsWith("Time to compute")) break;
                }
            }

        } catch (IOException e) {
            System.out.println("I/O Error: " + e);
        }
    }
}
