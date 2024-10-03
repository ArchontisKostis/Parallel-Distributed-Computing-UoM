// Σε αυτή την υλποποίηση αντί για την χρήση ReentrantLock θα χρησιμοποιήσουμε synchronized blocks.
// Τα synchronized blocks στην Java είναι ένας τρόπος για να αποφύγουμε τον ανταγωνισμό μεταξύ νημάτων για την πρόσβαση σε κοινόχρηστους πόρους.

public class SharedCounterArraySyncMethod {
  
    static int end = 1000;
    static int numThreads = 4;

	static SharedData sharedData = new SharedData();

    public static void main(String[] args) {
		// Χρήση ορισμάτων από την γραμμή εντολών αντί για σταθερές τιμές.
		if (args.length != 2) {
			System.out.println("Usage: SharedCounterArraySyncMethod <end> <numThreads>");
			System.exit(1);
		}

		// Λήψη των ορισμάτων από την γραμμή εντολών
		end = Integer.parseInt(args[0]);
		numThreads = Integer.parseInt(args[1]);

		// Λήψη του πραγματικού αριθμού των διαθέσιμων επεξεργαστών του μηχανήματος του χρηστη
		int realNumThreads = Runtime.getRuntime().availableProcessors();

		// Έλεγχος αν το numThreads που εισήγαγε ο χρήστης είναι μεγαλύτερο από τον πραγματικό αριθμό των διαθέσιμων επεξεργαστών του μηχανήματος του
		if (numThreads > realNumThreads) {
			System.out.println("Warning: numThreads > availableProcessors. Setting numThreads = availableProcessors");
			System.out.println("Available processors: " + realNumThreads + System.lineSeparator());
			numThreads = realNumThreads;
		}

		// Δημιουργία Δομής για αποθηκευση των νημάτων
		CounterThread threads[] = new CounterThread[numThreads];

		// Δημιουργία νημάτων και εκκίνηση
		for (int i = 0; i < numThreads; i++) {
			threads[i] = new CounterThread();
			threads[i].start();
		}

		// Αναμονή τερματισμού όλων των νημάτων
		for (int i = 0; i < numThreads; i++) {
			try {
				threads[i].join();
			}
			catch (InterruptedException e) {}
		}

		// Έλεγχος του πίνακα
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

	// Αντικείμενο που χρησιμοποιείται για την αποθήκευση και διαμοιρασμό των δεδομένων μεταξύ των νημάτων
	static class SharedData {
		int[] array = new int[end];

		public SharedData() { }

		// Τοποθέτηση του Κρίσιμου Τμήματος του κώδικα (array[i]++) μέσα σε synchronized μέθοδο.
		// Η διαφορά είναι ότι όλη η μέθοδος είναι synchronized και όχι μόνο ένα τμήμα του κώδικα μας
		public synchronized void increment(int i) {
			array[i]++;
		}

		public synchronized int getArrayItem(int i) {
			return array[i];
		}
	}

	// Η κλάση που ειναι υπεύθυνη για την αύξηση των τιμών του πίνακα (καθε αντικειμένο ειναι ενα νήμα)
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
  
