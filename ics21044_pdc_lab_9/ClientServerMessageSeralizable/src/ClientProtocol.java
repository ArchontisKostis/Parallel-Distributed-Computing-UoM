import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// Η Κλαση αυτή αντιπροσωπεύει το πρωτόκολλο επικοινωνίας του Client με τον Server. Πιο συγκεκριμένα ειναι το πρωτόκολλο που ακολουθεί ο Client για να επικοινωνήσει με τον Server.
public class ClientProtocol {
    private BufferedReader user = new BufferedReader(new InputStreamReader(System.in));

    // Προετοιμάζει το αίτημα προς τον Server (λήψη μηνύματος από τον χρήστη, ελεγχος του μηνύματος και επιστροφή του αιτήματος)
    // Επιστρέφει ενα αντικειμενο της κλάσης ClientRequest
    public ClientRequest prepareRequest() throws IOException {
        showMenu();
        String action = user.readLine().trim();
        String message = "";
        int key = 0;

        if (action.startsWith("ENC") || action.startsWith("DEC")) {
            String[] parts = action.split(" ", 3);
            if (parts.length != 3) {
                System.out.println("Invalid format for ENC or DEC.");
                return null;
            }

            action = parts[0];
            message = parts[1];
            key = Integer.parseInt(parts[2]);
        } else {
            String[] parts = action.split(" ", 2);
            if (parts.length == 2) {
                action = parts[0];
                message = parts[1];
            } else {
                System.out.println("Invalid format.");
                return null;
            }
        }

        return new ClientRequest(action, message, key);
    }

    // Επεξεργάζεται την απάντηση που λαμβάνει ο Client από τον Server και την εκτυπώνει
    public void processReply(ServerResponse serverResponse) {
        System.out.println("Message Received from the server: " + serverResponse.getResponse());
    }

    // Εμφανίζει το μενού επιλογών
    private void showMenu() {
        System.out.println("Available Actions:");
        System.out.println("1. UP message - Convert to uppercase.");
        System.out.println("2. LOW message - Convert to lowercase.");
        System.out.println("3. ENC <message> key - Encode message with key.");
        System.out.println("4. DEC <message> key - Decode message with key.");
        System.out.print("Enter action: ");
    }
}