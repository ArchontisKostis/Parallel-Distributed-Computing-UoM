
   
public class Lab3 {
  
    public static void main(String[] args) {

        int numThreads = 4; // Τοπική Μεταβλητή στην μέθοδο main. Δεν μοιράζεται εκτός της main
		
		SharedCounter count = new SharedCounter(numThreads);  // Η count είναι τοπική μεταβλητή στην main και μοιράζεται ως όρισμα αναφοράς
			
		CounterThread counterThreads[] = new CounterThread[numThreads]; // Τοπική Μεταβλητή στην μέθοδο main. Δεν μοιράζεται εκτός της main
		for (int i = 0; i < numThreads; i++) {  // Τοπική στήν main, η `i` μοιράζεται ως όρισμα τιμής όπου κάθε νήμα λαμβάνει διαφορετική τιμή
		    counterThreads[i] = new CounterThread(i, count);
		    counterThreads[i].start();
		}

        for (int i = 0; i < numThreads; i++) {  // Τοπική στήν main, η `i` μοιράζεται ως όρισμα τιμής όπου κάθε νήμα λαμβάνει διαφορετική τιμή
            try {
				counterThreads[i].join();
				count.n = count.n + counterThreads[i].threadN;  // Η threadN είναι μεταβλητή αντικείμένου που επιστρέφει μια τιμή στην main
            }
            catch (InterruptedException e) {}
		} 
        for (int i = 0; i < numThreads; i++)    // Τοπική Μεταβλητή στην μέθοδο main. Δεν μοιράζεται εκτός της main
			System.out.println("a["+i+"] = "+count.a[i]);   // η count.a[i] μοιράζεται ώς όρισμα αναφοράς
			
		System.out.println("n = "+count.n); // η count.n μοιράζεται ώς όρισμα αναφοράς

        // Εκτίμηση Εκτυπώσεων:
        // Thread 3 n = 4  a[3] =4
        // Thread 0 n = 1  a[0] =1
        // Thread 1 n = 2  a[1] =2
        // Thread 2 n = 3  a[2] =3
        // a[0] = 1
        // a[1] = 2
        // a[2] = 3
        // a[3] = 4
        // n = 10
        //
        // Πραγματικά Αποτελέσματα κατά την εκτέλεση:
        // ΕΚΤΕΛΕΣΗ 1:
        // Thread 1 n = 2  a[1] =2
        // Thread 2 n = 3  a[2] =3
        // Thread 3 n = 4  a[3] =4
        // Thread 0 n = 1  a[0] =1
        // a[0] = 1
        // a[1] = 2
        // a[2] = 3
        // a[3] = 4
        // n = 10
        //
        // ΕΚΤΕΛΕΣΗ 2:
        // Thread 1 n = 2  a[1] =2
        // Thread 0 n = 1  a[0] =1
        // Thread 2 n = 3  a[2] =3
        // Thread 3 n = 4  a[3] =4
        // a[0] = 1
        // a[1] = 2
        // a[2] = 3
        // a[3] = 4
        // n = 10
        //
        // ΠΑΡΑΤΗΡΗΣΕΙΣ:
        // Όπως και στα προηγούμενα προγράμματα παρατηρούμε ότι η σειρά εκτέλεσης μοιάζει τυχαία.
        // Αυτό οφείλεται στο γεγονός ότι τα νήματα εκτελούνται από τον επεξεργαστή σε διαφορετική σειρά κάθε φορά.
        // Όπως αναφέρθηκε και στα προηγούμενα παραδείγματα η σειρά εκτέλεσης των νημάτων εξαρτάται από διάφορους παράγοντες
        // όπως το υλικό, τον ΛΣ και την συγκεκριμένη υλοποίηση της JVM. Επομένως η σειρά των εκτυπώσεων μοιάζει μη-ντετερμινιστική.
    }   
}

class SharedCounter {
	
	int n;  // Μεταβλητή Αντικειμένου, μοιράζεται ώς όρισμα αναφοράς
    int[] a; // Μεταβλητή Αντικειμένου, μοιράζεται ώς όρισμα αναφοράς
    
    public SharedCounter (int numThreads) {
		
			this.n = 0;
			this.a = new int[numThreads];
	    
			for (int i = 0; i < numThreads; i++)  
				this.a[i] = 0; 
	}		

}    

class CounterThread extends Thread {
  	
    int threadID;   // Μεταβλητή αντικειμένου, καθε νημα παιρνει διαφορετικη τιμή
    int threadN;    // Μεταβλητή Αντικειμένου
    SharedCounter threadCount;  // Μεταβλητή ΑΝτικειμένου που μοιράζεται ώς όρισμα αναφοράς
    
    public CounterThread(int tid, SharedCounter c) {
        this.threadID = tid;
        this.threadCount = c;
        this.threadN = threadCount.n;
        
    }
  	
    public void run() {
  
        threadN = threadN + 1 + threadID;  
        threadCount.a[threadID] = threadCount.a[threadID] + 1 + threadID;
		System.out.println("Thread "+threadID+" n = "+ threadN +"  a["+threadID+"] ="+ threadCount.a[threadID]); 
    }
}

