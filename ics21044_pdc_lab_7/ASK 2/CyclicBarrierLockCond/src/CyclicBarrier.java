import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

// Για να υλοποιήσουμε Κυκλικό Φμα χρησιμοποιώντας μεταβλητες συνθήκης χρειαζόμαστε έναν μηχανισμό Κλειδαριας (Lock)
// και μεταβλητές συνθήκης (Condition Variables) ώστε να συντονίσουμε τον συγχρονισμό των νημάτων. Η λογική είναι οτι έχουμε να
// διαχειριστούμε 2 συνθήκες, μια για αναμονη και μια για αποχώρηση.
public class CyclicBarrier {
    private final int totalThreads;
    private int arrived = 0;
    private boolean waiting = true;
    private boolean leaving = true;

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition waitCond = lock.newCondition();
    private final Condition leaveCond = lock.newCondition();

    public CyclicBarrier(int totalThreads) {
        this.totalThreads = totalThreads;
    }

    public void barrier() {
        // "Παίρνουμε" την κλειδαριά πριν "πειράξουμε" τις μοιραζόμενες μεταβλητές
        lock.lock();
        try {
            arrived++;

            boolean lastThreadToArrive = arrived == totalThreads;
            if (lastThreadToArrive) {
                waiting = false;
                waitCond.signalAll();   // Ενημερώνουμε όλα τα νήματα που περιμένουν οτι μπορούν να συνεχίσουν
            }

            while (waiting) {
                try {
                    waitCond.await();
                } catch (InterruptedException e) { }
            }

            arrived--;
            if (arrived == 0) {
                leaving = false;
                leaveCond.signalAll();
            }

            while (leaving) {
                try {
                    leaveCond.await();
                } catch (InterruptedException e) { }
            }
        } finally {
            lock.unlock();
        }

    }
}
