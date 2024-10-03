import java.io.*;

// Η Κλάση αυτή αντιπροσωπεύει το πρωτόκολλο επικοινωνίας ανάμεσα στον Client και τον Server.
// Πιο συγκεκριμένα η κλάση ορίζει το πρωτόκολλο που θα ακολουθεί ο Client για να επικοινωνήσει με τον Server.
public class ClientProtocolCalculator {
    BufferedReader user = new BufferedReader(new InputStreamReader(System.in));

    // Προετοιμασία του request που θα σταλεί στον Server
    public String prepareRequest() throws IOException {
        String request = "";
        boolean done = false; // Flag to determine when to end communication

        while (!done) {
            String data = showUserPrompt();
            done = checkData(data);
            if (done) request = buildRequestMessage(data);
        }
        return request;
    }

    // Helper μέθοδος που ζητάει από τον χρήστη να εισάγει τα δεδομένα που θέλει να στείλει στον Server
    private String showUserPrompt() throws IOException {
        System.out.println("Enter operation and operands (e.g., + 5 3): ");
        return user.readLine();
    }

    // Έλεγχος των δεδομένων που εισήγαγε ο χρήστης
    private boolean checkData(String data) {
        // End communication if we receive "!"
        if (data.equals("!")) return true;

        // Validate the data format (prefix notation -> operator operand1 operand2)
        String[] parts = data.split(" ");
        if (parts.length != 3) return false;

        try {
            char op = parts[0].charAt(0);
            return op == '+' || op == '-' || op == '*' || op == '/';
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Κατασκευή του request που θα σταλεί στον Server
    private String buildRequestMessage(String data) {
        return data;
    }

    // Επεξεργασία της απάντησης που λαμβάνει ο Client από τον Server
    public void processReply(String reply) {
        String[] parts = reply.split(" ");
        if (parts[0].equals("R")) System.out.println("Result: " + parts[1]);
        else if (parts[0].equals("E")) System.out.println("Error code: " + parts[1]);
    }
}
