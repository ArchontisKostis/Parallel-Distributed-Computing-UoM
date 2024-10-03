public class ServerProtocol {
    private Eliza elizaChatbot = new Eliza();

    public String processRequest(String theInput) {
        System.out.println("Received Request <" + theInput + ">");

        String[] parts = theInput.split(" ", 2);

        if (parts.length < 2)
            return "Invalid Request Format.";

        String action = parts[0];
        String message = parts[1];

        switch (action) {
            case "UP":
                return message.toUpperCase();
            case "LOW":
                return message.toLowerCase();
            case "ENC":
                return handleCiphering(message, true);
            case "DEC":
                return handleCiphering(message, false);
            case "CHAT":
                return elizaChatbot.respond(message);
            default:
                return "Usupported Action. Supported Actions are (UP, LOW, ENC, DEC - Case Sensitive): ";
        }
    }

    private String handleCiphering(String message, boolean isEncryption) {
        // Ensure there is at least one space between the message and the key
        int keyIndex = message.lastIndexOf(' ');
        if (keyIndex == -1) return "Invalid Action format. Must include a message in <> and a key.";

        String keyPart = message.substring(keyIndex + 1).trim();
        String messagePart = message.substring(0, keyIndex).trim();

        // Check if message part is enclosed in < and >
        if (!messagePart.startsWith("<") || !messagePart.endsWith(">"))
            return "Invalid message format. Message must be enclosed in <>.";

        String text = messagePart.substring(1, messagePart.length() - 1).trim();
        int key = parseKey(keyPart);

        if (key == -1) return "Invalid key format.";

        return isEncryption ? Cipher.encrypt(text, key) : Cipher.decrypt(text, key);
    }

    private int parseKey(String keyString) {
        try {
            return Integer.parseInt(keyString);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}
