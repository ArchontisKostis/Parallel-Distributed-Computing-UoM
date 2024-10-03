// Σε αυτή την υλποποίηση αντί για την χρήση ReentrantLock θα χρησιμοποιήσουμε synchronized blocks.
// Τα synchronized blocks στην Java είναι ένας τρόπος για να αποφύγουμε τον ανταγωνισμό μεταξύ νημάτων για την πρόσβαση σε κοινόχρηστους πόρους.

public class SharedCounterArraySyncBlock {
  
    static int end = 1000;
    static int numThreads = 4;

	static SharedData sharedData = new SharedData();

    public static void main(String[] args) {
		// Χρήση ορισμάτων από την γραμμή εντολών αντί για σταθερές τιμές.
		if (args.length != 2) {
			System.out.println("Usage: SharedCounterArraySyncBlock <end> <numThreads>");
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

		public SharedData() { }

		public void increment(int i) {
			// Τοποθέτηση του Κρίσιμου Τμήματος του κώδικα (array[i]++) μέσα σε synchronized block.
			// Ώστε να αποφευχθεί ο ανταγωνισμός μεταξύ νημάτων για την πρόσβαση στον πίνακα.
			synchronized (this) {
				array[i]++;
			}

			// Ότι είναι εκτός του synchronized block δεν είναι ατομική ενέργεια.
			// Τα synchronized blocks χρησιμοποιούν το βασικό κλείδωμα που έχει κάθε αντικείμενο στην Java.
			// Αυτό σημαίνει ότι όταν ένα νήμα αποκτά το κλείδωμα ενός αντικειμένου, κανένα άλλο νήμα δεν μπορεί να αποκτήσει το κλείδωμα του αντικειμένου.
		}

		public int getArrayItem(int i) {
			// Τοποθέτηση του Κρίσιμου Τμήματος του κώδικα (return array[i]) μέσα σε synchronized block.
			synchronized (this) {
				return array[i];
			}

			// Ότι είναι εκτός του synchronized block δεν είναι ατομική ενέργεια.
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
  
