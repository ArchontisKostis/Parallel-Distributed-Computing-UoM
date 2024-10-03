public class DoubleCounterSync {
    // Για να αυξήσουμε τη διακριτότητα του syncronized, μπορούμε να χρησιμοποιήσουμε ξεχωριστά κλειδώματα για κάθε έναν από τους μας.
    // Με αυτόν τον τρόπο, οι αυξήσεις στα n1 και n2 μπορούν να γίνουν ανεξάρτητα χωρίς να μπλοκάρει ο ένας τον άλλον.
    // Στην πράξη θα ορίσουμε μια κλάση DoubleCounter με δύο ακέραιους μετρητές και δύο αντικείμενα τύπου Object για το κλείδωμα.
    // Κάθε Αντικείμενο Κλειδώματος θα χρησιμοποιηθεί για την αύξηση του αντίστοιχου μετρητή. Σε αυτή την υλοποίηση δεν θα χρησιμοποιήσουμε αντικείμενο τύπου
    // ReeentrantLock, αλλά απλά θα χρησιμοποιήσουμε το "γονικό" κλείδωμα που έχει κάθε αντικείμενο στην java.
    // Η αύξηση του κάθε μέτρητη θα γίνεται μέσα σε synchronized block με το αντίστοιχο αντικείμενο κλειδώματος.

    static SharedCounter counter = new SharedCounter();
    static int numThreads = 4;
    static int numIncrements = 1000;

    public static void main(String[] args) {
        CounterThread counterThreads[] = new CounterThread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            counterThreads[i] = new CounterThread(counter);
            counterThreads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try { counterThreads[i].join(); }
            catch (InterruptedException e) {}
        }

        checkCounts();
    }

    // Ελεγχει αν οι τελικοί μετρητές είναι σωστοί ή αν υπάρχει κάποιο λάθος.
    // Χρησιμευει για debugging κυρίως.
    public static void checkCounts() {
        int correctCount = numThreads * numIncrements;

        boolean counterOneOK = counter.getCounterA() == correctCount;
        boolean counterTwoOK = counter.getCounterB() == correctCount;

        if (counterOneOK && counterTwoOK) System.out.println("Both counters are OK.");
        else System.out.println("Error: Counters are not OK. Should be " +  correctCount + " for each counter.");

        System.out.println("Counter A = " + counter.getCounterA());
        System.out.println("Counter B = " + counter.getCounterB());
    }
}