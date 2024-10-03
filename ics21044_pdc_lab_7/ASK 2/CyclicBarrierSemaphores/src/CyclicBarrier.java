import java.util.concurrent.Semaphore;

// Για την υλοποιηση με σημαφορους θέλουμε 3 σημαφορους
// 1. Σηματοτοφόρο για την εξασφάλιση αμοιβαίου αποκλεισμού
// 2. Σηματοφόρο για την αναμονή των νημάτων στο σημείο συγχρονισμού
// 3. Σηματοφόρο για την αναμονή των νημάτων στο σημείο αποχώρησης
public class CyclicBarrier {
    private final int totalThreads;
    private int arrived = 0;
    private final Semaphore mutex = new Semaphore(1);
    private final Semaphore waiting = new Semaphore(0);
    private final Semaphore leaving = new Semaphore(1);

    public CyclicBarrier(int totalThreads) {
        this.totalThreads = totalThreads;
    }

    public void barrier() {
        // ΠΡΙΝ ΤΟ ΦΡΑΓΜΑ

        // Κάποιο νήμα μπαίνει για να αυξήσει το arrived και "παίρνει" το "κλείδωμα"
        try { mutex.acquire(); } catch (InterruptedException e) { }
        arrived++;

        if (arrived == totalThreads) {
            waiting.release(); // Επιτρέπει την είσοδο στο σημείο αναμονής

            // Μπλοκάρει το σημείο αποχώρησης
            try { leaving.acquire(); } catch (InterruptedException e) { }
        }
        mutex.release();    // Το "κλείδωμα" απελευθερώνεται

        // Είσοδος στο σημείο αναμονής
        try { waiting.acquire(); } catch (InterruptedException e) { }

        // Synchronization point

        // Κάποιο νήμα μπαίνει για να μειώσει το arrived και "παίρνει" το "κλείδωμα"
        try { mutex.acquire(); } catch (InterruptedException e) { }
        arrived--;

        if (arrived == 0) {
            // Μπλοκάρει το σημείο αναμονής
            try { waiting.acquire(); } catch (InterruptedException e) { }
            leaving.release(); // Επιτρέπει την είσοδο στο σημείο αποχώρησης
        }
        mutex.release();

        // Είσοδος στο σημείο αποχώρησης
        try { leaving.acquire(); } catch (InterruptedException e) { }
        leaving.release(); // Έξοδος από το σημείο αποχώρησης
    }
}
