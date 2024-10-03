public class CyclicBarrier {
    private final int totalThreads;

    // Μετρητής ωστε να ξέρουμε πόσα threads έχουν φτάσει στο barrier
    private int arrived = 0;

    // Σημαιες για τον ελεγχο αν κάποιο Thread μπορει να "φύγει" ή να "μέινει"
    private boolean waiting = false;
    private boolean leaving = false;

    public CyclicBarrier(int totalThreads) {
        this.totalThreads = totalThreads;
    }

    public synchronized void barrier() {
        // Πρωτα χειριζόμαστε την αναμονή
        try { waitBarrier(); } catch (InterruptedException e) { }

        // Μετα την αναχώρηση
        try { leaveBarrier(); } catch (InterruptedException e) { }
    }

    private synchronized void waitBarrier() throws InterruptedException {
        // Καποιος εφτασε στο φραγμα αρα αυξάνουμε τον μετρητή
        arrived++;

        // Ελεγχουμε αν το Thread που εφτασε ειναι το τελευταιο
        if (arrived == totalThreads) {
            // Το τελευταιο Thread εφτασε, και αρα τα υπολοιπα threads μπορουν να συνεχισουν
            waiting = true;
            leaving = false;
            // Ενημερώνουμε όλα τα threads που περιμένουν
            notifyAll();
        } else {
            // Αν φτασουμε εδω σημαινει οτι δεν εφτασαν ολα τα Threads και αρα περιμενουμε
            while (!waiting) {
                wait(); // περιμενουμε
            }
        }
    }

    private synchronized void leaveBarrier() throws InterruptedException {
        // Καποιος εφυγε απο το φραγμα, αρα μειωνουμε τον μετρητη
        arrived--;

        // Ελέγχουμε αν το τελευταίο Thread εφυγε
        if (arrived == 0) {
            // Το τελευταιο Thread εφυγε, αρα κανουμε reset τα flags
            waiting = false;
            leaving = true;
            // Ενημερώνουμε όλα τα threads που περιμένουν για να φύγουν
            notifyAll();
        } else {
            // Αν δεν
            while (!leaving) {
                wait(); // περιμενουμε
            }
        }
    }
}
