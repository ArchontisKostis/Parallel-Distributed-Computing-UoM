

public class SharedCounterArrayGlobalWhileLockObj {
  
    static int end = 10000;
    static int numThreads = 4;

	static SharedData sharedData = new SharedData();

    public static void main(String[] args) {
		// Λήψη ορισμάτων από την γραμμή εντολών αντί για σταθερές τιμές.
		if (args.length != 2) {
			System.out.println("Usage: java SharedCounterArrayGlobalWhileLockObj <end> <numThreads>");
			System.exit(1);
		}

		end = Integer.parseInt(args[0]);
		numThreads = Integer.parseInt(args[1]);

		int realNumThreads = Runtime.getRuntime().availableProcessors();

		if (numThreads > realNumThreads) {
			System.out.println("Warning: numThreads > availableProcessors. Setting numThreads = availableProcessors");
			System.out.println("Available processors: " + realNumThreads + System.lineSeparator());
			numThreads = realNumThreads;
		}

		System.out.println("Running with: end = " + end + ", numThreads = " + numThreads);

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
			if (sharedData.getArrayItem(i) != 1) {
				errors++;
				System.out.printf("%d: %d should be 1\n", i, sharedData.getArrayItem(i));
			}         
		}
        System.out.println (errors+" errors.");
    }

	static class SharedData {
		int[] array = new int[end];
		int counter = 0;

		// Αντί να χρησιμοποιήσουμε το γονικό κλείδωμα της κλάσης, μπορούμε να χρησιμοποιήσουμε ένα άλλο αντικείμενο για το κλείδωμα.
		// Στην java κάθε αντικείμενο έχει ένα κλείδωμα που μπορεί να χρησιμοποιηθεί για την συγχρονισμό των νημάτων.
		Object lockObj = new Object();

		public SharedData() { }

		// Τοποθέτηση του Κρίσιμου Τμήματος του κώδικα (array[i]++) μέσα σε synchronized block.
		public boolean increment() {
			// Κρισιμο τμημα
			synchronized (lockObj) {
				if (counter >= end) return false;
				array[counter]++;
				counter++;
			}
			// Τέλος Κρίσιμου Τμήματος

			return true;
		}

		public int getArrayItem(int i) {
			// Κρισιμο τμημα
			synchronized (lockObj) {
				return array[i];
			}
			// Τέλος Κρίσιμου Τμήματος
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
  
