
// Υλοποίηση με Απλή Στατική Κατανομή.
//
// Η απλή στατική κατανομή είναι μια βασική στρατηγική που χρησιμοποιείται στον παράλληλο προγραμματισμό για την κατανομή εργασιών ή φόρτων εργασίας σε πολλαπλές
// μονάδες επεξεργασίας (π.χ. νήματα). Στόχος είναι να γινει αποτελεσματική κατανομή του φόρτου εργασίας στις διαθέσιμες μονάδες επεξεργασίας, ώστε να επιτευχθεί
// καλύτερη απόδοση. Στην πράξη αυτό σημαίνει ότι οι εργασίες που πρέπει να εκτελεστούν χωρίζονται σε έναν σταθερό αριθμό τμημάτων και κατανέμονται σε διαφορετικές
// μονάδες επεξεργασίας. Στην στατική κατανομή το πλήθος των τμημάτων είναι γνωστό εξαρχής και δεν αλλάζει κατά την εκτέλεση του προγράμματος.
public class Main {
    public static void main(String[] args) {
        System.out.println("Parallel Sieve Running");

        // Ληψη ορισμάτων από τη γραμμή εντολών.
        if (args.length != 1) {
            System.out.println("Usage: java Main <size>");
            System.exit(1);
        }

        int size = 0;

        // Μετατροπή του ορίσματος σε ακέραιο.
        try {
            size = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid size");
            System.exit(1);
        }

        if (size <= 0) {
            System.out.println("Invalid size");
            System.exit(1);
        }

        boolean[] prime = new boolean[size + 1];

        // Αρχικοποίηση του πίνακα prime.
        // Η αρχικοποίηση του πίνακα δεν χρειάζεται να γίνει παράλληλα αφού είναι μια απλή εργασία.
        for (int i = 2; i <= size; i++) {
            prime[i] = true;
        }

        Timer timer = new Timer("Parallel Sieve");

        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        int limit = (int) Math.sqrt(size) + 1;

        Thread[] threads = new Thread[numberOfThreads];

        // Κατανομή των εργασιών στα νήματα.
        for (int i = 0; i < numberOfThreads; i++) {
            int start = i * (limit / numberOfThreads) + 2;
            int end = (i == numberOfThreads - 1) ? limit : (i + 1) * (limit / numberOfThreads) + 2;

            threads[i] = new SieveThread(start, end, prime);
            threads[i].start();
        }

        // Αναμονή των νημάτων.
        for (int i = 0; i < numberOfThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        timer.stop();

        // Εύρεση των πρώτων αριθμών.
        int count = 0;
        for (int i = 2; i <= size; i++) {
            if (prime[i]) {
                count++;
            }
        }

        System.out.println("number of primes " + count);
        timer.printTotalTime();
    }
}