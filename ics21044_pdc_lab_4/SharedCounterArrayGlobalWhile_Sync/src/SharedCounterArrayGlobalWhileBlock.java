

public class SharedCounterArrayGlobalWhileBlock {
  
    static int end = 10000;
    static int NUM_THREADS = 4;

	static SharedData sharedData = new SharedData();

    public static void main(String[] args) {
		// Λήψη του ορισμάτων για την εκτέλεση αντι για χρήση σταθερών τιμών.
		if (args.length != 2) {
			System.out.println("Usage: SharedCounterArrayGlobalWhileBlock <end> <numThreads>");
			System.exit(1);
		}

		end = Integer.parseInt(args[0]);
		int numThreads = Integer.parseInt(args[1]);

		int realNumThreads = Runtime.getRuntime().availableProcessors();

		// Ελεγχος αν το numThreads που εισηγαγε ο χρήστης είναι μεγαλύτερο από τον πραγματικό αριθμό των διαθέσιμων επεξεργαστών του μηχανήματος του
		if (numThreads > realNumThreads) {
			System.out.println("Warning: numThreads > availableProcessors. Setting numThreads = availableProcessors");
			System.out.println("Available processors: " + realNumThreads + System.lineSeparator());
			numThreads = realNumThreads;
		}

		// Δημιουργία Δομής για αποθήκευση των threads
        CounterThread threads[] = new CounterThread[numThreads];

		// Δημιουργία των threads και εκκίνηση
		for (int i = 0; i < numThreads; i++) {
			threads[i] = new CounterThread();
			threads[i].start();
		}

		// Αναμονή των threads να τελειώσουν
		for (int i = 0; i < numThreads; i++) {
			try { threads[i].join(); }
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

	// Αντικείμενο που χρησιμοποιειται για αποθηκευση και διαμοιρασμο δεδομένων μεταξύ των threads
	static class SharedData {
		int[] array = new int[end];
		int counter = 0;

		public SharedData() { }

		public boolean increment() {
			// Αντί για κλειδαρια τύπου Lock ή ένα ξεχωριστο αντικείμενο τύπου Object που χρησιμοποιεί το καθολικό κλείδωμα την Java.
			// Θα χρησιμοποιήσουμε μεν το κλεδωμα που κληρονομείται από την κλάση Object αλλά θα το κάνουμε με το this (δηλ θα παρουμε το κλειδωμα αυτού του αντικειμένου)
			synchronized (this) {
				if (counter >= end) return false;
				array[counter]++;
				counter++;
			}

			return true;
		}

		public int getArrayItem(int i) {
			// Αντί για κλειδαρια τύπου Lock ή ένα ξεχωριστο αντικείμενο τύπου Object που χρησιμοποιεί το καθολικό κλείδωμα την Java.
			// Θα χρησιμοποιήσουμε μεν το κλεδωμα που κληρονομείται από την κλάση Object αλλά θα το κάνουμε με το this (δηλ θα παρουμε το κλειδωμα αυτού του αντικειμένου)
			synchronized (this) {
				return array[i];
			}
		}
	}


    static class CounterThread extends Thread {
       public CounterThread() {}
  	
       public void run() {
		   boolean run = true;
		   while (run) {
			   run = sharedData.increment();
		   }
		}            	
    }
}
  
