import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Σε αυτό το πρόγραμμα θα χρησιμοποιήσουμε ένα ξεχωριστό αντικέιμενο για τον διαμοιρασμό του πίνακα. Το Lock θα βρισκεται μεσα σε αυτο το αντικείμενο.

// Το παρόν πρόγραμμα δεν είναι thread-safe.
// Ο λόγος είναι ότι τα threads αυξάνουν τον πίνακα array[i] και δεν χρησιμοποιούν κάποιον τρόπο για να αποφύγουν τον ανταγωνισμό μεταξύ τους.
// Έτσι, το αποτέλεσμα του προγράμματος δεν είναι προβλέψιμο.
//
// Στην πράξη η μεταβλητή array και τα περιεχόμενά της μοιράζονται στα νήματα και κάθε νήμα αυξάνει το περιεχόμενο της array[i].
// Σε χαμηλό επίπεδο έχουμε κάτι τέτοιο:
// 1. Το Νήμα 1 διαβάζει την τιμή της array[i] (π.χ. 0)
// 2. Το Νήμα 2 διαβάζει την τιμή της array[i] (π.χ. 0)
// 3. Το Νήμα 1 αυξάνει την τιμή της array[i] κατά 1 (τώρα array[i] = 1 για το νήμα 1)
// 4. Το Νήμα 2 αυξάνει την τιμή της array[i] κατά 1 (τώρα array[i] = 1 για το νήμα 2)
// 5. Το Νήμα 1 αποθηκεύει την τιμή της array[i] (array[i] = 1)
// 6. Το Νήμα 2 αποθηκεύει την τιμή της array[i] (array[i] = 1 - η αύξηση του νήμα 2 "χάθηκε"!)
//
// Η ενέργειες αυτές δεν είναι ατομικές, δηλαδή δεν εκτελούνται ως μια αδιαίρετη λειτουργία. Αντ' αυτού, διαδέχονται η μία την άλλη, και έτσι πολλά νήματα
// μπορούν να διαβάσουν την ίδια αρχική τιμή της array[i], πρίν αυξηθεί και να γράψουν την ίδια αυξημένη τιμή πίσω, οδηγώντας σε λανθασμένες τελικές τιμές της array[i].
//
// Για να λύσουμε αυτό το πρόβλημα θα χρησιμοποιήσουμε locks. Στην πράξη θα έχουμε ενα αντικείμενο κλειδώματος τύπου ReentrantLock το οποίο θα απαγορεύει την πρόσβαση
// στην array[i] αν ένα νήμα έχει κλειδώσει το lock. Έτσι, όταν ένα νήμα αυξάνει το περιεχόμενο της array[i], το κλείδωμα αποτρέπει την πρόσβαση άλλων νημάτων στην array[i].
// Ετσι καταλήγουμε με κάτι τέτοιο:
// 1. Νήμα 1 κλειδώνει το lock (ΚΑΝΕΝΑ ΑΛΛΟ ΝΗΜΑ ΔΕΝ ΜΠΟΡΕΙ ΝΑ ΑΠΟΚΤΗΣΕΙ ΤΟ LOCK ΚΑΙ ΝΑ ΔΙΑΒΑΣΕΙ Η ΑΥΞΗΣΕΙ ΤΟ array[i])
// 2. Νήμα 2 προσπαθεί να κλειδώσει το lock (ΑΠΟΤΥΧΙΑ)
// 3. Νήμα 1 διαβάζει την τιμή του array[i] (π.χ. 0)
// 4. Νήμα 1 αυξάνει την τιμή του array[i] κατά 1 (τώρα array[i] = 1 για το νήμα 1)
// 5. Νήμα 1 αποθηκεύει την τιμή του array[i] (array[i] = 1)
// 6. Νήμα 2 προσπαθεί να κλειδώσει το lock (ΕΠΙΤΥΧΙΑ)
// 7. Νήμα 2 διαβάζει την τιμή του array[i] (array[i] = 1)
// 8. Νήμα 2 αυξάνει την τιμή του array[i] κατά 1 (τώρα array[i] = 2 για το νήμα 2)
// 9. Νήμα 2 αποθηκεύει την τιμή του array[i] (array[i] = 2)
// 10. Νήμα 2 ξεκλειδώνει το lock
//
// Πλέον το Νήμα 2 δεν θα μπορεί να εκτελέσει την εντολή array[i] = array[i] + 1 μέχρι το Νήμα 1 να ξεκλειδώσει το lock.
public class SharedCounterArrayGlobal {

    static int end = 1000;
    static int numThreads = 4;

    static SharedData sharedData = new SharedData();

    public static void main(String[] args) {

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
            if (sharedData.getArray(i) != numThreads*i) {
                errors++;
                System.out.printf("%d: %d should be %d\n", i, sharedData.getArray(i), numThreads*i);
            }
        }
        System.out.println (errors+" errors.");
    }

    static class SharedData {
        int[] array = new int[end];
        Lock lock = new ReentrantLock();

        public SharedData() { }

        public void incrementArray(int i) {
            lock.lock();
            try { array[i]++; }
            finally { lock.unlock(); }
        }

        public int getArray(int i) {
            lock.lock();
            try { return array[i]; }
            finally { lock.unlock(); }
        }
    }


    static class CounterThread extends Thread {

        public CounterThread() { }

        public void run() {

            for (int i = 0; i < end; i++) {
                for (int j = 0; j < i; j++) {
                    sharedData.incrementArray(i);
                }
            }
        }
    }
}
  
