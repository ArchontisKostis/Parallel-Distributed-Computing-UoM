import java.util.concurrent.Semaphore;

// Όσον αφορά το πρόβλημα του Παράγωγου-Καταναλωτή πρακτικά θέλουμε 3 πράγματα:
// 1. Ο Producer ΔΕΝ πρέπει να μπορει να βάλει στοιχεια στον buffer αν αυτός είναι γεμάτος.
// 2. Ο Consumer ΔΕΝ πρέπει να μπορει να βγάλει στοιχεια απο τον buffer αν αυτός είναι κενός.
// 3. Ο Consumer και ο Producer ΔΕΝ πρέπει να μπορούν να προσπελάσουν τον buffer ταυτόχρονα.
public class Buffer{
	private int[] contents;
	private final int unlimited;
	private int front, back;
	private int counter;

	// 1. Αφου ο buffer είναι άπειρος, δεν χρειάζεται να κάνουμε έλεγχο για το αν είναι γεμάτος ή όχι
	// 2. Σηματοφορος "μετρητής" για τον ελεγχο του αν ο buffer είναι άδειας
	private Semaphore bufferEmpty;
	// 3. Δυαδικός σηματοφόρος για την προστασία της πρόσβασης στο buffer
	private Semaphore mutex;
	

	// Constructor
	public Buffer(int s) {
		this.unlimited = s;
		contents = new int[unlimited];
		for (int i=0; i<unlimited; i++)
			contents[i] = 0;
		this.front = 0;
		this.back = -1;
		this.counter = 0;

		// Αρχικοποίηση των σηματοφόρων

		// Αρχικά το buffer είναι άδειο (άρα δεν έχει στοιχεία)
		this.bufferEmpty = new Semaphore(0);
		this.mutex = new Semaphore(1);
	}

	// Put an item into buffer
	public void put(int data) {
		// Ο buffer έχει μέγεθος άπειρο οπότε δεν υπάρχει λόγος να γίνει έλεγχος για το αν είναι γεμάτος

		// Εισοδος στην κρισιμη περιοχη
		try { mutex.acquire(); } catch (InterruptedException e) { }

		back = (back + 1);
		contents[back] = data;
		counter++;

		System.out.println("Prod " + Thread.currentThread().getName() + " No "+ data + " Loc " + back + " Count = " + counter);

		// Ξεκλειδώνουμε τον buffer ωστε να μπορεί να μπει αλλος παραγωγος (εξοδος από την κρισιμη περιοχη)
		mutex.release();

		// Ειδοποιούμε τον καταναλωτή ότι υπάρχει στοιχείο στον buffer
		bufferEmpty.release();
	}

	// Get an item from bufffer
	public int get() {
		int data = 0;

		// περιμένει μέχρι να μπει κάτι στον buffer (δηλ να υπάρχει στοιχείο για να πάρει)
		try { bufferEmpty.acquire(); } catch (InterruptedException e) { }

		// κλειδώνει το buffer ωστε να μην μπει αλλος καταναλωτής
		try { mutex.acquire(); } catch (InterruptedException e) { }

		// Θα φτάσουμε εδώ μόνο αν υπάρχει τουλάχιστον ένα στοιχείο στον buffer
		data = contents[front];
		counter--;

		System.out.println("  Cons " + Thread.currentThread().getName() + " No "+ data + " Loc " + front + " Count = " + (counter-1));

		front = (front + 1);

		// Ξεκλειδώνουμε τον buffer ωστε να μπορεί να μπει αλλος καταναλωτής (εξοδος από την κρισιμη περιοχη)
		mutex.release();

		// Θεωρητικα αφου εχουμε απειρο μεγεθος δεν υπαρχει περιπτωση να περιμένουν καταναλωτές και άρα δεν χρειάζεται να τους "ενημερώσουμε"

		// Επιστροφή του στοιχείου που πήραμε
		return data;
	}
}