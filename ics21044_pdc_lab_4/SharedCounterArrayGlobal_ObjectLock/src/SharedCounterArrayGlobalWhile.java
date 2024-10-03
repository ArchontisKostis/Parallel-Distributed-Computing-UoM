
// Σε αυτό το πρόγραμμα Κάθε νήμα αυξάνει τον κοινόχρηστο μετρητή και ενημερώνει το αντίστοιχο στοιχείο του πίνακα.
// Λόγω της έλλειψης συγχρονισμού, δημιουργούνται συνθήκες ανταγωνισμού. Στην πράξη Πολλαπλά νήματα μπορεί να διαβάζουν
// και να τροποποιούν ταυτόχρονα την ίδια τιμή μετρητή, με αποτέλεσμα λανθασμένες ενημερώσεις στον πίνακα και ασυνεπείς τελικές τιμές.
//
// Για να εξασφαλίσουμε σωστές ενημερώσεις του πίνακα, θα εισάγουμε ένα ReentrantLock για να συγχρονίσουμε την πρόσβαση τόσο στον μετρητή όσο και στον πίνακα.
// Κάθε νήμα θα κλειδώνει το κρίσιμο τμήμα όπου ενημερώνεται ο μετρητής και ο πίνακας, εξασφαλίζοντας οτι μόνο ένα νήμα έχει πρόσβαση κάθε φορά.

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SharedCounterArrayGlobalWhile {
  
    static int end = 10000;
    static int numThreads = 4;

	static SharedData sharedData = new SharedData();

    public static void main(String[] args) {
		// Δημιουργία Δομής για αποθήκευση των νημάτων
        CounterThread threads[] = new CounterThread[numThreads];

		// Δημιουργία των νημάτων κ εκκίνηση
		for (int i = 0; i < numThreads; i++) {
			threads[i] = new CounterThread();
			threads[i].start();
		}

		// Αναμονή των νημάτων να τελειώσουν
		for (int i = 0; i < numThreads; i++) {
			try {
				threads[i].join();
			}
			catch (InterruptedException e) {}
		}

		// Ελεγχος του πίνακα
        check_array ();
    }
     
    static void check_array ()  {
		int i, errors = 0;

		System.out.println ("Checking...");

        for (i = 0; i < end; i++) {
			if (sharedData.array[i] != 1) {
				errors++;
				System.out.printf("%d: %d should be 1\n", i, sharedData.array[i]);
			}         
		}
        System.out.println (errors+" errors.");
    }

	static class SharedData {
		int[] array = new int[end];
		int counter = 0;

		// Για την διασφάλιση οτι μονο ενα νήμα μπορει να βρίσκεται στο κρισιμο τμημα καθε φορα θα χρησιμοποιηθεί μια κλειδαριά
		Lock lock = new ReentrantLock();

		public SharedData() { }

		public boolean increment() {
			// Κλείδωμα της κλειδαριάς ωστε να μην μπορει αλλο νημα να μπει στο κριτικο τμημα
			lock.lock();
			try {
				// Κρισιμο Τμημα
				if (counter >= end) return false;
				array[counter]++;
				counter++;
				// Τελος Κρισιμου Τμηματος
			} finally {
				// Ξεκλειδωμα της κλειδαριας
				lock.unlock();
			}

			return true;
		}
	}


    static class CounterThread extends Thread {
  	
       public CounterThread() {
       }

	   // Καλεί την increment() της sharedData μέχρι να επιστρέψει false (δηλ κατι να πάει στραβά)
       public void run() {
		   boolean run = true;
		   while (run) run = sharedData.increment();
	   }
    }
}
  
