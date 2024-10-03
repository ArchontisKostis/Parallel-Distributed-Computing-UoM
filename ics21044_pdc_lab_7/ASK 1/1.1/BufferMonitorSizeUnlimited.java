// Όσον αφορά το πρόβλημα του Παράγωγου-Καταναλωτή πρακτικά θέλουμε 3 πράγματα:
// 1. Ο Producer ΔΕΝ πρέπει να μπορει να βάλει στοιχεια στον buffer αν αυτός είναι γεμάτος.
// 2. Ο Consumer ΔΕΝ πρέπει να μπορει να βγάλει στοιχεια απο τον buffer αν αυτός είναι κενός.
// 3. Ο Consumer και ο Producer ΔΕΝ πρέπει να μπορούν να προσπελάσουν τον buffer ταυτόχρονα.
//
// Μιας και θα χρησιμοποιήσουμε επόπτες θα χρησιμοποιήσουμε τις μεθόδους wait() και notify() για την επικοινωνία μεταξύ των νημάτων
// οι οποίες είναι μέθοδοι που κληρονομούνται από την κλάση Object σε όλα τα αντικείμενα στην Java
// Για κάνουμε τις μεθόδους put και get thread-safe θα χρησημοποιήσουμε την λέξη κλειδί synchronized.
// Ετσι δεν χρειαζόμαστε τους επόπτες bufferEmpty και mutex για τον ελεγχο και ενημέρωση των νημάτων.
public class Buffer{
	// Πλεον δεν εχουμε μονο ενα στοιχείο οπότε χρειαζόμαστε μια δομή οπως ο πίνακας για τηαποθήκευση >1 στοιχείων
	private int[] contents;
	private final int infinity;
	private int front, back;
	private int counter;

	// Constructor
	public Buffer(int s) {
		this.unlimited = s;
		this.front = 0;
		this.back = -1;
		this.counter = 0;
		initContents();
	}

	private void initContents() {
		contents = new int[unlimited];

		for (int i=0; i<infinity; i++)
			contents[i] = 0;
	}

	// Put an item into buffer
	public synchronized void put(int data) {
		// Ο buffer έχει μέγεθος θεωρητικά άπειρο οπότε δεν υπάρχει λόγος να γίνει έλεγχος για το αν είναι γεμάτος
		back = (back + 1);
		contents[back] = data;
		counter++;
		System.out.println("Prod " + Thread.currentThread().getName() + " No "+ data + " Loc " + back + " Count = " + counter);

		// Ενημέρωση των καταναλωτών που περιμένουν οτι υπάρχει στοιχείο στον buffer που μπορούν να πάρουν.
		notifyAll();
	}

	// Get an item from bufffer
	public synchronized int get() {
		int data = 0;

		// Ελεγχος αν ο buffer είναι κενός (προφανως δεν μπορούμε να βγάλουμε στοιχείο αν δεν υπάρχει)
		while (counter == 0) {
			try { wait(); } catch (InterruptedException e) { }
		}

		// Θα φτάσουμε εδώ μόνο αν υπάρχει τουλάχιστον ένα στοιχείο στον buffer
		data = contents[front];
		counter--;

		System.out.println("  Cons " + Thread.currentThread().getName() + " No "+ data + " Loc " + front + " Count = " + counter);
		if (counter == 0) System.out.println("The buffer is empty");

		// Το στοιχείο "βγήκε" από τον buffer οπότε το front πρέπει να αλλάξει
		front = (front + 1);

		// Ενημέρωση των παραγωγών που περιμένουν οτι υπάρχει χώρος στον buffer που μπορούν να βάλουν στοιχείο.
		notifyAll();

		// Επιστροφή του στοιχείου που πηραμε
		return data;
	}
}