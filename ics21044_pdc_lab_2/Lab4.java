
public class Lab4 {

    public static void main(String[] args) {

        int numThreads = 4; // Τοπική Μεταβλητή στην μέθοδο main. Δεν μοιράζεται εκτός της main
	
	    int n = 0;  // Τοπική Μεταβλητή στην μέθοδο main. Δεν μοιράζεται εκτός της main
		CounterThread counterThreads[] = new CounterThread[numThreads]; // Τοπική Μεταβλητή στην μέθοδο main. Δεν μοιράζεται εκτός της main

		for (int i = 0; i < numThreads; i++) {  // Τοπική στήν main, η `i` μοιράζεται ως όρισμα τιμής όπου κάθε νήμα λαμβάνει διαφορετική τιμή
		    counterThreads[i] = new CounterThread(i, n);
		    counterThreads[i].start();
		}
	
        for (int i = 0; i < numThreads; i++) {  // Η i είναι τοπική μεταβλητή στο scope του block της for
            try {
				counterThreads[i].join();
				n += counterThreads[i].threadN;
            }
            catch (InterruptedException e) {}
		} 
		System.out.println("Main n = "+n);

        // Εκτίμηση Εκτυπώσεων:
        // Thread 0 n = 1
        // Thread 1 n = 2
        // Thread 3 n = 4
        // Thread 2 n = 3
        // Main n = 10
        //
        // Πραγματικά Αποτελέσματα κατά την εκτέλεση:
        // ΕΚΤΕΛΕΣΗ 1:
        // Thread 0 n = 1
        // Thread 2 n = 3
        // Thread 3 n = 4
        // Thread 1 n = 2
        // Main n = 10
        //
        // ΕΚΤΕΛΕΣΗ 2:
        // Thread 1 n = 2
        // Thread 3 n = 4
        // Thread 2 n = 3
        // Thread 0 n = 1
        // Main n = 10
        //
        // ΠΑΡΑΤΗΡΗΣΕΙΣ:
        // Αντίστοιχα με τα προηγούμενα προγράμματα παρατηρούμε οτι οι εκτιμήσεις μας διαφέρουν απο τα αποτελέσματα των εκτελέσεων,
        // καθώς και ότι κάθε εκτέλεση παρήγαγε διαφορετικά αποτελέσματα. Αυτό οφείλεται στο γεγονός ότι τα νήματα εκτελούνται
        // απο την εικονική μηχανή της JAVA και η πραγματική σειρά εκτέλεσης των νημάτων εξαρτάται απο διάφορους παράγοντες.
    }

}

class CounterThread extends Thread {
  	
    int threadID;   // Μεταβλητή Αντικειμένου, κάθε νήμα λαμβάνει διαφορετική τιμή
    int threadN;    // Μεταβλητή Αντικειμένου, μοιράζεται ως όρισμα τιμής και κάθε νήμα λαμβάνει διαφορετική τιμή
    
    public CounterThread(int tid, int n) {
        this.threadID = tid;
        this.threadN = n;
    }
  	
    public void run() {
        threadN = threadN + 1 + threadID;
        System.out.println("Thread "+threadID+ " n = "+threadN); 
    }
}
