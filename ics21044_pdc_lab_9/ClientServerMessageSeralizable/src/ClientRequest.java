import java.io.Serializable;

// Η κλάση αυτή αντιπροσωπεύει το αίτημα που κάνει ο Client στον Server.
// Για να μην γίνεται με strings η επικοινωνία μεταξύ των δύο, χρησιμοποιείται αυτή η κλάση για να αντιπροσωπεύει το αίτημα του Client.
// Για να μπορει να μεταφερθεί μέσω του δικτύου, η κλάση αυτή υλοποιεί το interface Serializable ωστε να μπορεί να μεταφερθεί μέσω του ObjectOutputStream και ObjectInputStream
// και να το "διαβάσει" ο Server και ο Client αντίστοιχα.
public class ClientRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String action;
    private String message;
    private int key;

    public ClientRequest(String action, String message, int key) {
        this.action = action;
        this.message = message;
        this.key = key;
    }

    public String getAction() {
        return action;
    }

    public String getMessage() {
        return message;
    }

    public int getKey() {
        return key;
    }
}
