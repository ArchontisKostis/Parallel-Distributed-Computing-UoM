import java.util.concurrent.locks.Lock;

// Υλοποίηση με τη χρήση του μοντέλου Master-Worker.
//
// Εδώ χρησιμοποιούμε το μοντέλο Master-Worker για να υπολογίσουμε τους αριθμούς που δεν είναι πρώτοι.
// Το μοντέλο αυτό περιλαμβάνει 2 βασικούς Ρόλους:
// - Τον Master, ο οποίος είναι υπεύθυνος για την κατανομή των εργασιών στους Workers.
// - Τους Workers, οι οποίοι είναι υπεύθυνοι για την εκτέλεση των εργασιών που τους ανατίθενται από τον Master.
//
// Ο Master έχει μόνο μια δουλειά η οποία είναι να "σπάσει" το αρχικό πρόβλημα σε μικρότερα κομμάτια και να κατανέμει αυτά τα κομμάτια στους Workers.
// Στην πραγματικότητα ο Master παρακολουθεί την εξέλιξη της εργασίας των Workers και αλλάζει την κατανομή των εργασιών δυναμικά ωστε όλοι οι Workers να
// "χρησιμοποιούνται" όσο το δυνατόν πιο αποδοτικά. Σε αυτό το πρόγραμμα o Master δεν αλλάζει την κατανομή των εργασιών αλλά απλά περιμένει να τελειώσουν.
//
// Οι Workers (ή εργάτες) λαμβάνουν ένα ή παραπάνω Tasks από τον Master και δουλεια τους ειναι να τα εκτελέσουν. Σε πραγματικά προβλήματα οι Workers μπορεί να
// ζητήσουν νέες εργασίες από τον Master όταν τελειώσουν τις τρέχουσες κάτι που δεν γίνεται σε αυτό το πρόγραμμα.
public class Main {
    static int numThreads;
    static int currentPrime = 2;
    static int totalTasks;
    static int completedTasks = 0;
    static Lock lock = new java.util.concurrent.locks.ReentrantLock();

    public static void main(String[] args) {
        System.out.println("Parallel Sieve Running");

        // Ληψη ορισμάτων από τη γραμμή εντολών.
        if (args.length != 1) {
            System.out.println("Usage: java Main <size>");
            System.exit(1);
        }

        // Μετατροπή του ορίσματος σε ακέραιο.
        try {
            totalTasks = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid size");
            System.exit(1);
        }

        if (totalTasks <= 0) {
            System.out.println("Invalid size");
            System.exit(1);
        }

        boolean[] prime = new boolean[totalTasks + 1];

        // Αρχικοποίηση του πίνακα prime.
        // Η αρχικοποίηση του πίνακα δεν χρειάζεται να γίνει παράλληλα αφού είναι μια απλή εργασία.
        for (int i = 2; i <= totalTasks; i++) {
            prime[i] = true;
        }

        Timer timer = new Timer("Parallel Sieve");

        numThreads = Runtime.getRuntime().availableProcessors();
        Thread[] threads = new Thread[numThreads];

        // Κατανομή των εργασιών στα νήματα.
        for (int i = 0; i < numThreads; i++) {
            WorkerThread worker = new WorkerThread(prime);
            threads[i] = new Thread(worker);
            threads[i].start();

            // For debugging purposes.
            // threads[i].printThreadWork();
        }

        // Αναμονή των νημάτων.
        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        timer.stop();

        // Εύρεση των πρώτων αριθμών.
        int count = 0;
        for (int i = 2; i <= totalTasks; i++) {
            if (prime[i]) {
                count++;
            }
        }

        System.out.println("number of primes " + count);
        timer.printTotalTime();
    }

    // Get the next prime number to process
    public static int getNextPrime() {
        lock.lock();
        try {
            if (completedTasks < totalTasks) {
                int primeToProcess = currentPrime;
                currentPrime++;
                completedTasks++;
                return primeToProcess;
            } else {
                return -1; // No more tasks
            }
        } finally {
            lock.unlock();
        }
    }
}