import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

// Όσον αφορά το πρόβλημα του Παράγωγου-Καταναλωτή πρακτικά θέλουμε 3 πράγματα:
// 1. Ο Producer ΔΕΝ πρέπει να μπορει να βάλει στοιχεια στον buffer αν αυτός είναι γεμάτος.
// 2. Ο Consumer ΔΕΝ πρέπει να μπορει να βγάλει στοιχεια απο τον buffer αν αυτός είναι κενός.
// 3. Ο Consumer και ο Producer ΔΕΝ πρέπει να μπορούν να προσπελάσουν τον buffer ταυτόχρονα.
class Buffer{
    private int[] contents;
    private final int infinity; // το ιδιο με πριν αλλα αλλαξαμε το ονομα

    // Πλεον δεν εχουμε μόνο ένα στοιχείο στο buffer αλλά πολλά. Αρα χρειαζόμαστε τις μεταβλητές front, back και counter ωστε να μπορούμε να προσπελάσουμε στοιχεία του buffer.
    private int front, back;
    private int counter = 0;

    // Κλειδαρια για την προστασία της πρόσβασης στο buffer
    private Lock lock = new ReentrantLock();

    // Αφού ο buffer έχει μέγεθος άπειρο, θεωρητικά δεν μπορεί να γεμίσει ποτέ. Αρα δεν χρειάζεται εχουμε έλεγχο για το αν είναι γεμάτος ή όχι
    // private Condition bufferFull = lock.newCondition();

    // Αυτο το χρειαζομαστε για να περιμένουμε όταν ο buffer είναι κενός
    private Condition bufferEmpty = lock.newCondition();

    // Κατασκευαστής
    public Buffer(int s) {
        this.infinity = s;
        this.front = 0;
        this.back = infinity - 1;
        initContents();
    }

    private void initContents() {
        contents = new int[infinity];

        for (int i=0; i<infinity; i++)
            contents[i] = 0;
    }

    // Εισαγωγή στοιχείου
    public void put(int data) {
        // Κλειδωμα του lock ωστε να μην μπορει καποιο άλλο νήμα να το "παρει" ταυτόχρονα
        lock.lock();
        try {
            // Δεν υπάρχει περίπτωση το buffer να είναι γεμάτο αφού έχει μέγεθος άπειρο οποτε δεν χρειάζεται να κάνουμε έλεγχο και να περιμένουμε
            // while (counter == ...) {
            // 	 System.out.println("The buffer is full");
            // 	 try { bufferFull.await(); } catch (InterruptedException e) { }
            // }

            back = (back + 1);
            contents[back] = data;
            counter++;

            System.out.println("Prod " + Thread.currentThread().getName() + " No "+ data + " Loc " + back + " Count = " + counter);

            // Eνημέρωση των καταναλωτών που περιμένουν οτι υπάρχει στοιχείο στον buffer που μπορούν να πάρουν.
            bufferEmpty.signalAll();
        }

        // Ξεκλειδώνουμε το lock ωστε να μπορεί να το "παρει" κάποιο άλλο νήμα
        finally { lock.unlock(); }
    }

    // Ανάκτηση στοιχείου (pop)
    public int get() {
        int data = 0;

        // Κλειδωμα του lock ωστε να μην μπορει καποιο άλλο νήμα να το "παρει" ταυτόχρονα
        lock.lock();

        try {
            // Αν ο buffer ειναι κενος, δεν υπάρχει κάτι να πάρουμε οποτε περιμένουμε
            while (counter == 0) {
                try { bufferEmpty.await(); } catch (InterruptedException e) { }
            }

            // Θα φτάσουμε εδώ οταν ο buffer δεν είναι κενός
            data = contents[front];
            counter--;	// Μειώνουμε τον counter

            System.out.println("  Cons " + Thread.currentThread().getName() + " No "+ data + " Loc " + front + " Count = " + counter);
            if (counter == 0) System.out.println("The buffer is empty");

            // Υπολογισμός της επόμενης θέσης του front
            front = (front + 1);

            // Ο buffer ειναι οπως ειπαμε απειρος αρα θεωρητικα δεν θα υπαρχει κανενας καταναλωτης που περιμενει να αδειασει για να βαλει κατι
            // bufferFull.signalAll();
        }

        // Ξεκλειδώνουμε το lock ωστε να μπορεί να το "παρει" κάποιο άλλο νήμα
        finally { lock.unlock(); }

        // Επιστροφή του στοιχείου που πήραμε
        return data;
    }
}
