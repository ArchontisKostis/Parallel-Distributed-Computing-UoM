
// Υλοποίηση με Απλή Στατική Κατανομή.
//
// Στην κυκλική κατανομή, οι εργασίες ή τα δεδομένα κατανέμονται με κυκλικό τρόπο. Αυτό σημαίνει ότι η πρώτη εργασία ανατέιθεται
// στον πρώτο επεξεργαστή, τη δεύτερη στον δεύτερο κ.ο.κ. Μόλις φτάσουμε στον τελευταίο επεξεργαστή, ξεκινάμε πάλι με τον πρώτο επεξεργαστή.
// Στην ουσία ακολουθεί την λογική Round Robin.
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
        SieveThread[] threads = new SieveThread[numberOfThreads];

        // Κατανομή των εργασιών στα νήματα.
        for (int i = 0; i < numberOfThreads; i++) {
            threads[i] = new SieveThread(prime, i, numberOfThreads, limit);
            threads[i].start();

            // For debugging purposes.
            // threads[i].printThreadWork();
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