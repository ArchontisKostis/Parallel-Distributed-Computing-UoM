import java.util.concurrent.Semaphore;

// Όσον αφορά το πρόβλημα του Παράγωγου-Καταναλωτή πρακτικά θέλουμε 3 πράγματα:
// 1. Ο Producer ΔΕΝ πρέπει να μπορει να βάλει στοιχεια στον buffer αν αυτός είναι γεμάτος.
// 2. Ο Consumer ΔΕΝ πρέπει να μπορει να βγάλει στοιχεια απο τον buffer αν αυτός είναι κενός.
// 3. Ο Consumer και ο Producer ΔΕΝ πρέπει να μπορούν να προσπελάσουν τον buffer ταυτόχρονα.
//
// Μιας και θα χρησιμοποιήσουμε επόπτες θα χρησιμοποιήσουμε τις μεθόδους wait() και notifyAll() για
// την επικοινωνία μεταξύ των νημάτων οι οποίες είναι μέθοδοι που κληρονομούνται από την κλάση Object
// σε όλα τα αντικείμενα στην Java.
//
// Για κάνουμε τις μεθόδους put και get thread-safe θα χρησημοποιήσουμε την λέξη κλειδί synchronized.
// Ετσι δεν χρειαζόμαστε τους επόπτες bufferEmpty και mutex για τον ελεγχο και ενημέρωση των νημάτων.
public class Buffer{
	// Πλεον ο Buffer μας έχει μονο ένα στοιχειο οπότε δεν χρειαζόμαστε κάποια δομή σαν (π.χ. πίνακα)
	// και αρα δεν χρειαζόμαστε μεταβλητή size για το μέγεθος ουτε και τις front, back για την προσπέλαση του πίνακα
	// Αντι αυτού θα χρησιμοποιήσουμε μια μεταβλητή content που θα περιέχει το στοιχείο που βρίσκεται στο buffer
	// Αρα αυτές οι δύο ιδιότητες δεν χρειάζονται πλέον
	// private int size;
	// private int front, back;
	private int contents;
	private int counter = 0;

	// Constructor
	public Buffer() { contents = 0; }

	// Put an item into buffer
	public synchronized void put(int data) {
		// Αν o buffer είναι γεμάτος τότε περιμένουμε να μην είναι
		while (counter == 1) {
			try { wait(); } catch (InterruptedException e) { }
		}

		// Όπως είπαμε δεν χρειαζόμαστε το back γιατί έχουμε μόνο ένα στοιχείο (αρα ο υπολογισμός αυτός φεύγει)
		contents = data;
		counter++;

		System.out.println("Prod " + Thread.currentThread().getName() + " No "+ data + " Count = " + counter);
		if (counter == size) System.out.println("The buffer is full");

		// Ενημέρωση των καταναλωτών που περιμένουν οτι υπάρχει στοιχείο στον buffer που μπορούν να πάρουν.
		notifyAll();
	}

	// Get an item from bufffer
	public synchronized int get() {
		int data = 0;

		// Αν ο buffer είναι κενός τότε περιμένουμε να μην είναι
		while (counter == 0) {
			try { wait(); } catch (InterruptedException e) { }
		}

		data = contents;
		counter--;

		System.out.println("  Cons " + Thread.currentThread().getName() + " No "+ data + " Count = " + counter);
		if (counter == 0) System.out.println("The buffer is empty");

		// Ενημέρωση των παραγωγών που περιμένουν οτι υπάρχει χώρος στον buffer που μπορούν να βάλουν στοιχείο.
		notifyAll();

		return data;
	}
}