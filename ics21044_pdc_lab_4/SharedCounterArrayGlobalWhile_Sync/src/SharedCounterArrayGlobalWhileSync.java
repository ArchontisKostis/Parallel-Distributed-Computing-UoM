

public class SharedCounterArrayGlobalWhileSync {
  
    static int end = 10000;
    static int numThreads = 4;

	static SharedData sharedData = new SharedData();

    public static void main(String[] args) {
		// Λήψη των ορισμάτων από τη γραμμή εντολών
		if (args.length != 2) {
			System.out.println("Usage: java SharedCounterArrayGlobalWhileSync <end> <numThreads>");
			System.exit(1);
		}

		end = Integer.parseInt(args[0]);
		numThreads = Integer.parseInt(args[1]);

		int realNumThreads = Runtime.getRuntime().availableProcessors();

		// Έλεγχος αν το numThreads που εισήγαγε ο χρήστης είναι μεγαλύτερο από τον πραγματικό αριθμό των διαθέσιμων επεξεργαστών του μηχανήματος του χρηστη
		if (numThreads > realNumThreads) {
			System.out.println("Warning: numThreads > availableProcessors. Setting numThreads = availableProcessors");
			System.out.println("Available processors: " + realNumThreads + System.lineSeparator());
			numThreads = realNumThreads;
		}

		System.out.println("Running with: end = " + end + ", numThreads = " + numThreads);

		// Δημιουργια Δομης για αποθηκευση των threads
        CounterThread threads[] = new CounterThread[numThreads];

		// Δημιουργία των threads και εκκίνηση
		for (int i = 0; i < numThreads; i++) {
			threads[i] = new CounterThread();
			threads[i].start();
		}

		// Αναμονή των threads να τελειώσουν
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
			if (sharedData.getArrayItem(i) != 1) {
				errors++;
				System.out.printf("%d: %d should be 1\n", i, sharedData.getArrayItem(i));
			}         
		}
        System.out.println (errors+" errors.");
    }

	// Αντικείμενο που χρησιμοποιείται για αποθήκευση και διαμοιρασμό δεδομένων μεταξύ των threads
	static class SharedData {
		int[] array = new int[end];
		int counter = 0;

		public SharedData() { }

		// Τοποθέτηση του Κρίσιμου Τμήματος του κώδικα (array[i]++) μέσα σε synchronized μέθοδο. Τώρα όλη η μέθοδος είναι το "κρίσιμο τμήμα".
		public synchronized boolean increment() {
			if (counter >= end) return false;
			array[counter]++;
			counter++;

			return true;
		}

		// Τοποθέτηση της μεθόδου getArrayItem μέσα σε synchronized μέθοδο. Τώρα όλη η μέθοδος είναι το "κρίσιμο τμήμα".
		public synchronized int getArrayItem(int i) {
			return array[i];
		}
	}


    static class CounterThread extends Thread {
  	
       public CounterThread() {
       }
  	
       public void run() {
		   boolean run = true;
		   while (run) {
			   run = sharedData.increment();
		   }
		}            	
    }
}
  
