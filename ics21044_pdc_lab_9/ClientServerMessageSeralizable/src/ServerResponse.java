import java.io.Serializable;

// Η κλάση αυτή αντιπροσωπεύει την απάντηση που δίνει ο Server στον Client.
// Για να μην γίνεται με strings η επικοινωνία μεταξύ των δύο, χρησιμοποιείται αυτή η κλάση για να αντιπροσωπεύει την απάντηση του Server.
// Για να μπορει να μεταφερθεί μέσω του δικτύου, η κλάση αυτή υλοποιεί το interface Serializable ωστε να μπορεί να μεταφερθεί μέσω του ObjectOutputStream και ObjectInputStream
// και να το "διαβάσει" ο Server και ο Client αντίστοιχα.
public class ServerResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private String response;

    public ServerResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}