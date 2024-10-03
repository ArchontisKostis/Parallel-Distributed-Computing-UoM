import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class Buffer {
    // Πλεον ο Buffer μας έχει μονο ένα στοιχειο οπότε δεν χρειαζόμαστε κάποια δομή σαν (π.χ. πίνακα)
    // και αρα δεν χρειαζόμαστε μεταβλητή size για το μέγεθος ουτε και τις front, back για την προσπέλαση του πίνακα
    // Αντι αυτού θα χρησιμοποιήσουμε μια μεταβλητή content που θα περιέχει το στοιχείο που βρίσκεται στο buffer
    // Αρα αυτές οι δύο ιδιότητες δεν χρειάζονται πλέον
    // private int size;
    // private int front, back;

    private int content;

    private int counter = 0;

    // Κλειδαρια για την προστασία της πρόσβασης στο buffer
    private Lock lock = new ReentrantLock();

    // Συνθήκες για τον έλεγχο αν ο buffer είναι γεμάτος ή κενός και για ειδοποίηση ή αναμονή των νημάτων που περιμένουν
    private Condition bufferFull = lock.newCondition();
    private Condition bufferEmpty = lock.newCondition();

    // Constructor
    public Buffer() {
        content = 0;
    }

    public void put(int data) {
        // Κλειδωμα του lock ωστε να μην μπορει καποιο άλλο νήμα να κάνει get ταυτόχρονα
        lock.lock();
        try {
            // Αν το buffer είναι γεμάτο περιμένουμε
            while (counter == 1) {
                try { bufferFull.await(); } catch (InterruptedException e) { }
            }

            // Θα φτάσουμε εδώ μόνο αν ο buffer είναι κενός
            content = data;
            counter++;

            System.out.println("Prod " + Thread.currentThread().getName() + " No "+ data + " Count = " + counter);

            // Ειδοποίηση των νημάτων που περιμένουν ότι το buffer είναι γεμάτο
            bufferEmpty.signalAll();
        }
        // Ξεκλειδώνουμε το lock ωστε να μπορεί να το "παρει" κάποιο άλλο νήμα
        finally { lock.unlock(); }
    }

    public int get() {
        int data = 0;

        // Κλειδωμα του lock ωστε να μην μπορει καποιο άλλο νήμα να προσπελάσει το buffer
        lock.lock();
        try {
            // Αν ο buffer ειναι κενος, δεν υπάρχει κάτι να πάρουμε οποτε περιμένουμε μεχρι κατι να μπει
            while (counter == 0) {
                try { bufferEmpty.await(); } catch (InterruptedException e) { }
            }

            // Θα φτάσουμε εδώ οταν ο buffer δεν είναι κενός
            data = content;
            counter--;

            System.out.println("  Cons " + Thread.currentThread().getName() + " No "+ data + " Count = " + (counter-1));

            bufferFull.signalAll(); // Ειδοποίηση των νημάτων που περιμένουν ότι ο buffer δεν είναι πλέον γεμάτος
        }

        // Ξεκλειδώνουμε το lock ώστε να μπορεί να το πάρει κάποιο άλλο νήμα
        finally { lock.unlock(); }

        // Επιστρέφουμε το στοιχείο που πήραμε από τον buffer
        return data;
    }
}
