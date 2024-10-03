
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
    static int counter = 0;
    static int[] array = new int[end];
    static int numThreads = 4;

	static Lock lock = new ReentrantLock(); // Κλειδαρία για να αποφευχθεί ο ανταγωνισμός μεταξύ των νημάτων

    public static void main(String[] args) {
		// Δημιουργία Δομής για αποθήκευση των νημάτων
        CounterThread threads[] = new CounterThread[numThreads];

		// Δημιουργία των νημάτων και εκκίνηση
		for (int i = 0; i < numThreads; i++) {
			threads[i] = new CounterThread();
			threads[i].start();
		}

		// Περιμένουμε να τελειώσουν όλα τα νήματα
		for (int i = 0; i < numThreads; i++) {
			try {
				threads[i].join();
			}
			catch (InterruptedException e) {}
		}

		// Ελέγχουμε τον πίνακα
        check_array ();
    }
     
    static void check_array ()  {
		int i, errors = 0;

		System.out.println ("Checking...");

        for (i = 0; i < end; i++) {
			if (array[i] != 1) {
				errors++;
				System.out.printf("%d: %d should be 1\n", i, array[i]);
			}         
		}
        System.out.println (errors+" errors.");
    }


    static class CounterThread extends Thread {
  	
       public CounterThread() {
       }
  	
       public void run() {
            while (true) {
				// Κλείδωμα του lock που έχει η main ώστε κανένα άλλο νήμα να μην μπορεί να αυξήσει το array[i]
				lock.lock();

				try {
					// Κρίσιμο τμήμα του κώδικα
					if (counter >= end) break;

					array[counter]++;
					counter++;

					// Τελος του κρίσιμου τμήματος
				} finally {
					// Ξεκλείδωμα του lock ώστε να μπορεί να αυξήσει το array[i] κάποιο άλλο νήμα
					lock.unlock();
				}
			}
	   }
    }
}
  
