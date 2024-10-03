
// Helper Class για την κρυπτογράφηση και αποκρυπτογράφηση μηνυμάτων
public class Cipher {
    // private constructor ωστε να μην μπορεί να δημιουργηθεί αντικείμενο της κλάσης
    private Cipher() { }

    // Κρυπτογραφεί ένα μήνυμα με βάση το offset και το επιστρέφει
    public static String encrypt(String message, int offset) {
        System.out.println("Encrypting Message");
        StringBuilder result = new StringBuilder();

        for (char character : message.toCharArray()) {
            if (Character.isLetter(character)) {
                char base = Character.isLowerCase(character) ? 'a' : 'A';
                int originalAlphabetPosition = character - base;
                int newAlphabetPosition = (originalAlphabetPosition + offset) % 26;
                char newCharacter = (char) (base + newAlphabetPosition);
                result.append(newCharacter);
            } else {
                result.append(character);
            }
        }
        return result.toString();
    }

    // Αποκρυπτογραφεί ένα μήνυμα με βάση το offset και το επιστρέφει
    public static String decrypt(String message, int offset) {
        System.out.println("Decrypting Message");
        StringBuilder result = new StringBuilder();
        for (char character : message.toCharArray()) {
            if (Character.isLetter(character)) {
                char base = Character.isLowerCase(character) ? 'a' : 'A';
                int originalAlphabetPosition = character - base;
                int newAlphabetPosition = (originalAlphabetPosition - offset + 26) % 26;
                char newCharacter = (char) (base + newAlphabetPosition);
                result.append(newCharacter);
            } else {
                result.append(character);
            }
        }
        return result.toString();
    }
}
