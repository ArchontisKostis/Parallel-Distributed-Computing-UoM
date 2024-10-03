// Αυτή η κλάση χρησιμοποιείται για να ενθυλακώσει την λογική και την κατάσταση του μετρητή.
// Στην πράξη, ο μετρητής αυτός αποτελείται από έναν ακέραιο αριθμό (value) και ένα αντικείμενο κλειδώματος (lockObj).
// Η Κλάση SharedCounter θα έχει 2 αντικέιμενα τύπου Counter, ένα για τον μετρητή Α και ένα για τον μετρητή Β.
// Η μέθοδος increment(String counter) αυξάνει τον μετρητή που δίνεται ως όρισμα.
// Η μέθοδος getCounterA() επιστρέφει την τιμή του μετρητή Α.
// Και οι δύο μέθοδοι χρησιμοποιούν synchronized blocks για να αποφύγουν τον ανταγωνισμό μεταξύ των νημάτων.
public class SafeCounter {
    int value;
    Object lockObj = new Object();

    public SafeCounter() {
        this.value = 0;
    }

    public int get() {
        synchronized (lockObj) {
            return value;
        }
    }

    public void increment() {
        synchronized (lockObj) {
            value = value + 1;
        }
    }
}
