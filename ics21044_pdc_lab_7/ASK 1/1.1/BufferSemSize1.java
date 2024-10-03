import java.util.concurrent.Semaphore;

// Όσον αφορά το πρόβλημα του Παράγωγου-Καταναλωτή πρακτικά θέλουμε 3 πράγματα:
// 1. Ο Producer ΔΕΝ πρέπει να μπορει να βάλει στοιχεια στον buffer αν αυτός είναι γεμάτος.
// 2. Ο Consumer ΔΕΝ πρέπει να μπορει να βγάλει στοιχεια απο τον buffer αν αυτός είναι κενός.
// 3. Ο Consumer και ο Producer ΔΕΝ πρέπει να μπορούν να προσπελάσουν τον buffer ταυτόχρονα.
public class Buffer{
	// Πλεον ο Buffer μας έχει μονο ένα στοιχειο οπότε δεν χρειαζόμαστε κάποια δομή σαν (π.χ. πίνακα)
	// και αρα δεν χρειαζόμαστε μεταβλητή size για το μέγεθος ουτε και τις front, back για την προσπέλαση του πίνακα
	// Αντι αυτού θα χρησιμοποιήσουμε μια μεταβλητή content που θα περιέχει το στοιχείο που βρίσκεται στο buffer
	// Αρα αυτές οι δύο ιδιότητες δεν χρειάζονται πλέον
	// private int size;
	// private int front, back;
	private int content;
	private int counter = 0;

	// Σηματοφορος για τον αμοιβαιο αποκλεισμο
	private Semaphore mutex = new Semaphore(1);

	// Σηματοφορος για τον έλεγχο αν ο buffer είναι κενος (κρατάει το πλήθος των κενών θέσεων)
	private Semaphore empty = new Semaphore(1);

	// Σηματοφορος για τον έλεγχο αν ο buffer είναι γεμάτος (κρατάει το πλήθος των γεμάτων θέσεων)
	private Semaphore full = new Semaphore(0);
	

	// Constructor
	public Buffer() {
	contents = 0;
	}

	// Put an item into buffer
	public void put(int data) {
		// Αν ο buffer είναι γεμάτος περιμένουμε αφου δεν υπαρχει χώρος για νέο στοιχείο
		try { empty.acquire(); } catch (InterruptedException e) { }

		// Εισοδος στην κρισιμη περιοχη
		try { mutexPut.acquire(); } catch (InterruptedException e) { }

		contents = data;
		counter++;
		System.out.println("Prod " + Thread.currentThread().getName() + " No "+ data + " Count = " + counter);

		// Βγαινουμε από την κρίσιμη περιοχή
		mutexPut.release();

		// Ενημερώνουμε τους καταναλωτές οτι υπάρχει στοιχείο στον buffer που μπορούν να πάρουν
		full.release();
	}

	// Get an item from bufffer
	public int get() {
		int data = 0;
		// Αν ο buffer είναι κενός περιμένουμε αφου δεν υπάρχει στοιχείο για να πάρουμε
		try { full.acquire(); } catch (InterruptedException e) { }

		// Εισοδος στην κρισιμη περιοχη
		try { mutexGet.acquire();	} catch (InterruptedException e) { }

		data = contents;
		counter--;
		System.out.println("  Cons " + Thread.currentThread().getName() + " No "+ data + " Count = " + (counter-1));

		// Βγαινουμε από την κρίσιμη περιοχή
		mutexGet.release();

		// Ενημερώνουμε τους παραγωγούς οτι υπάρχει χώρος στον buffer που μπορούν να βάλουν στοιχείο
		empty.release();

		// Επιστροφή του στοιχείου που πήραμε
		return data;
	}
}