import java.io.IOException;

public class CyclicBarrier {
    private final int totalThreads;
    // Μετρητής ωστε να ξέρουμε πόσα threads έχουν φτάσει στο barrier
    private int arrived = 0;
    // "Σημαία" για να "παρακολουθούμε" αν όλα τα threads έχουν φτάσει στο barrier
    private boolean allWaiting = false;

    public CyclicBarrier(int totalThreads) { this.totalThreads = totalThreads; }

    public synchronized void barrier() {
        // Καποιος εφτασε στο φραγμα αρα αυξάνουμε τον μετρητή
        arrived++;

        // Ελεγχουμε αν το Thread που εφτασε ειναι το τελευταιο
        if (arrived == totalThreads) {
            // Το τελευταιο Thread εφτασε
            // κανουμε update την σημαία ωστε να ενημερωθουν τα υπολοιπα threads και να μπορέσουν να συνεχίσουν
            allWaiting = true;

            // Ενημερώνουμε όλα τα threads που περιμένουν
            notifyAll();
        } else {
            // Αν δεν εχουν φτασει όλα τα νήματα, τότε περιμένουμε
            while (!allWaiting) {
                try { wait(); } catch (InterruptedException e) { }  // Περιμενουμε μεχρι να φτασουν στο φραγμα ολα τα νήματα
            }
        }
    }
}
