// Σε αυτή την υλποποίηση αντί για την χρήση ReentrantLock θα χρησιμοποιήσουμε synchronized blocks.
// Τα synchronized blocks στην Java είναι ένας τρόπος για να αποφύγουμε τον ανταγωνισμό μεταξύ νημάτων για την πρόσβαση σε κοινόχρηστους πόρους.

public class SharedCounterArrayLockObj {
  
    static int end = 1000;
    static int numThreads = 4;

	static SharedData sharedData = new SharedData();

    public static void main(String[] args) {
		// Λήψη ορισμάτων από την γραμμή εντολών αντί για σταθερές τιμές.
		if (args.length != 2) {
			System.out.println("Usage: SharedCounterArrayLockObj <end> <numThreads>");
			System.exit(1);
		}
		end = Integer.parseInt(args[0]);
		numThreads = Integer.parseInt(args[1]);

		int realNumThreads = Runtime.getRuntime().availableProcessors();

		// Έλεγχος αν το numThreads που εισήγαγε ο χρήστης είναι μεγαλύτερο από τον πραγματικό αριθμό των διαθέσιμων επεξεργαστών του μηχανήματος του
		if (numThreads > realNumThreads) {
			System.out.println("Warning: numThreads > availableProcessors. Setting numThreads = availableProcessors");
			System.out.println("Available processors: " + realNumThreads + System.lineSeparator());
			numThreads = realNumThreads;
		}

		CounterThread threads[] = new CounterThread[numThreads];
		
		for (int i = 0; i < numThreads; i++) {
			threads[i] = new CounterThread();
			threads[i].start();
		}
	
		for (int i = 0; i < numThreads; i++) {
			try {
				threads[i].join();
			}
			catch (InterruptedException e) {}
		} 
		check_array ();
    }
     
    static void check_array ()  {
		int i, errors = 0;

		System.out.println ("Checking...");

        for (i = 0; i < end; i++) {
			if (sharedData.getArrayItem(i) != numThreads*i) {
				errors++;
				System.out.printf("%d: %d should be %d\n", i, sharedData.getArrayItem(i), numThreads*i);
			}         
        }
        System.out.println (errors+" errors.");
    }

	// Αντικείμενο χρησιμοποιείται για την αποθήκευση και διαμοιρασμό δεδομένων μεταξύ νημάτων
	static class SharedData {
		int[] array = new int[end];

		// Αντί να χρησιμοποιήσουμε το γονικό κλείδωμα της κλάσης, μπορούμε να χρησιμοποιήσουμε ένα άλλο αντικείμενο για το κλείδωμα.
		// Στην java κάθε αντικείμενο έχει ένα κλείδωμα που μπορεί να χρησιμοποιηθεί για την συγχρονισμό των νημάτων.
		Object lockObj = new Object();

		public SharedData() { }

		// Τοποθέτηση του Κρίσιμου Τμήματος του κώδικα (array[i]++) μέσα σε synchronized μέθοδο.
		// Η διαφορά είναι ότι όλη η μέθοδος είναι synchronized και όχι μόνο ένα τμήμα του κώδικα μας
		public void increment(int i) {
			synchronized (lockObj) {
				array[i]++;
			}
		}

		// Τοποθέτηση του Κρίσιμου Τμήματος του κώδικα (return array[i]) μέσα σε synchronized μέθοδο.
		// Η διαφορά είναι ότι όλη η μέθοδος είναι synchronized και όχι μόνο ένα τμήμα του κώδικα μας
		public int getArrayItem(int i) {
			synchronized (lockObj) {
				return array[i];
			}
		}
	}

    static class CounterThread extends Thread {
  	
       public CounterThread() {
       }
  	
       public void run() {
  
            for (int i = 0; i < end; i++) {
				for (int j = 0; j < i; j++)
					sharedData.increment(i);
            } 
		}            	
    }
}
  
