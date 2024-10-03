

public class Lab2 {

    public static void main(String[] args) {

        int numThreads = 4;  // Τοπική Μεταβλητή στην μέθοδο main. Δεν μοιράζεται εκτός της main

        int n = 0;  // Τοπική Μεταβλητή στην μέθοδο main. Δεν μοιράζεται εκτός της main
        int[] a = new int[numThreads];  // Τοπική Μεταβλητή στην μέθοδο main. Μοιράζεται σαν όρισμα αναφοράς
        for (int i = 0; i < numThreads; i++) // Η i είναι τοπική μεταβλητή στο scope του block της for
            a[i] = 0;

        CounterThread counterThreads[] = new CounterThread[numThreads]; // Τοπική Μεταβλητή στην μέθοδο main. Δεν μοιράζεται εκτός της main
        for (int i = 0; i < numThreads; i++) {  // Τοπική στήν main, η `i` μοιράζεται ως όρισμα τιμής όπου κάθε νήμα λαμβάνει διαφορετική τιμή
            counterThreads[i] = new CounterThread(i, n, a);
            counterThreads[i].start();
        }

        for (int i = 0; i < numThreads; i++) { // Η i είναι τοπική μεταβλητή στο scope του block της for
            try {
                counterThreads[i].join();
            }
            catch (InterruptedException e) {}
        }

        System.out.println("n = "+n);

        for (int i = 0; i < numThreads; i++)    // Η i είναι τοπική μεταβλητή στο scope του block της for
            System.out.println("a["+i+"] = "+a[i]);

        // Εκτίμηση Εκτυπώσεων:
        //
        // Thread 0 my a = 0
        // Thread 1 my a = 1
        // Thread 2 my a = 2
        // Thread 3 my a = 3
        // n = 0
        // a[0] = 0
        // a[1] = 1
        // a[2] = 2
        // a[3] = 3
        //
        // Πραγματικά Αποτελέσματα κατά την εκτέλεση:
        // ΕΚΤΕΛΕΣΗ 1:
        // Thread 1 my a = 1
        // Thread 0 my a = 0
        // Thread 3 my a = 3
        // Thread 2 my a = 2
        // n = 0
        // a[0] = 0
        // a[1] = 1
        // a[2] = 2
        // a[3] = 3
        //
        // ΕΚΤΕΛΕΣΗ 2:
        // Thread 0 my a = 0
        // Thread 1 my a = 1
        // Thread 3 my a = 3
        // Thread 2 my a = 2
        // n = 0
        // a[0] = 0
        // a[1] = 1
        // a[2] = 2
        // a[3] = 3
        //
        // ΠΑΡΑΤΗΡΗΣΕΙΣ:
        // Όπως και στο προηγούμενο πρόγραμμα παρατηρούμε ότι οι εκτιμήσεις μας διαφέρουν από τα πραγματικά αποτελέσματα.
        // Αυτό είναι κάτι το απόλυτα λογικό αφού τα νήματα εκτελούνται ταυτόχρονα και δεν μπορούμε να προβλέψουμε την σειρά εκτέλεσης τους
        // κάτι που κάνει τις εκτελέσεις να διαφέρουν από την μία στην άλλη και να μοιάζουν τυχαίες. Όπως αναφέρθηκε και στον κώδικα Lab1.java
        // αυτό γίνεται γιατι η σειρά εκτέλεσης του προγράμματος όταν χρησιμοποιούμε ταυτοχρονισμό φαίνεται μη-ντετερμινιστική καθώς η JVM διαχειρίζεται
        // την εκτέλεση των νημάτων και στην πράξη η σειρά εκτέλεσης των νημάτων μπορεί να επηρεαστεί από πολλούς παράγοντες όπως το λειτουργικό σύστημα
        // το φορτίο του συστήματος και το ΛΣ.
    }

}

class CounterThread extends Thread {

    int threadID;  // Μεταβλητή Αντικειμένου, κάθε νήμα λαμβάνει διαφορετική τιμή
    int n;         // Μεταβλητή Αντικειμένου, μοιράζεται ως όρισμα τιμής και κάθε νήμα λαμβάνει διαφορετική τιμή
    int[] a;       // Μεταβλητή Αντικειμένου, μοιράζεται ως όρισμα αναφοράς
    public CounterThread(int tid, int n, int[] a) {
        this.threadID = tid;
        this.n = n;
        this.a = a;
    }

    public void run() {
        n = n + threadID ;
        a[threadID] = a[threadID] + n ;
        System.out.println("Thread "+threadID+ " my a = "+a[threadID]);
    }
}
