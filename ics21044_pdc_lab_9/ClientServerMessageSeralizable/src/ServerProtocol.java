// Η Κλαση αυτή αντιπροσωπεύει το πρωτόκολλο επικοινωνίας του Client με τον Server. Πιο συγκεκριμένα ειναι το πρωτόκολλο που ακολουθεί ο Server για να επικοινωνήσει με τον Client
// και να κάνει τους κατάλληλους ελέγχους για την επεξεργασία των αιτημάτων του Client.
public class ServerProtocol {
    // Επεξεργάζεται το αίτημα που λαμβάνει ο Server από τον Client και επιστρέφει την απάντηση
    public ServerResponse processRequest(ClientRequest request) {
        System.out.println("Received Request <" + request.getAction() + ">");

        String action = request.getAction();
        String message = request.getMessage();
        int key = request.getKey();

        /* For debugging */
        // System.out.println("Action: " + action);

        switch (action) {
            case "UP":
                return new ServerResponse(message.toUpperCase());
            case "LOW":
                return new ServerResponse(message.toLowerCase());
            case "ENC":
                return new ServerResponse(handleCiphering(message, true, key));
            case "DEC":
                return new ServerResponse(handleCiphering(message, false, key));
            default:
                return new ServerResponse("Unsupported Action. Supported Actions are (UP, LOW, ENC, DEC - Case Sensitive): ");
        }
    }

    // Επεξεργάζεται το μήνυμα που λαμβάνει από τον Client και επιστρέφει το κρυπτογραφημένο ή αποκρυπτογραφημένο μήνυμα
    private String handleCiphering(String message, boolean isEncryption, int key) {
        // Check if message part is enclosed in < and >
        if (!message.startsWith("<") || !message.endsWith(">"))
            return "Invalid message format. Message must be enclosed in <>.";

        String text = message.substring(1, message.length() - 1).trim();

        return isEncryption ? Cipher.encrypt(text, key) : Cipher.decrypt(text, key);
    }
}
