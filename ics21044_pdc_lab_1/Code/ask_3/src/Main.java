public class Main {
    // Static Variables so that they can be accessed by all threads
    public static final int CALCULATION_CEILING = 20;
    public static final int THREAD_COUNT = 10;

    public static void main(String[] args) {
        // Array to store the threads
        CalculatorThread[] threads = new CalculatorThread[THREAD_COUNT];

        // Δημιουργία και Εκκίνηση Νημάτων
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new CalculatorThread(i);
            threads[i].start();

            // Η μέθοδος start() εκκινεί το νήμα. Το νήμα κάνει τους υπολογισμούς του και εκτυπώνει τα αποτελέσματα.
            // Όταν τα νήματα εκτυπώνουν ταυτόχρονα, η έξοδος μπορεί να είναι μπερδεμένη επειδή πολλά νήματα
            // γράφουν στην κονσόλα ταυτόχρονα. Αυτό μπορεί να οδηγήσει στο να μπλέκονται οι γραμμές με τρόπο
            // που δυσκολεύει την κατανόηση της εξόδου κάποιου μεμονωμένου νήματος. Το φαινόμενο αυτό εμφανίζεται
            // λόγω της μη ντετερμινιστικής φύσης του χρονοπρογραμματισμού νημάτων από το ΛΣ.
            //
            // Αν απομονώσουμε την έξοδο των νημάτων, και βάλουμε την μέθοδο join() μετά την εκκίνηση των νημάτων,
            // τότε κάθε νήμα θα περιμένει την ολοκλήρωση των υπολογισμών του προηγούμενου νήματος πριν ξεκινήσει
            // τους δικούς του υπολογισμούς. Αυτό θα οδηγήσει σε μια πιο καθαρή έξοδο, όπου τα αποτελέσματα των
            // νημάτων δεν θα ανακατεύονται. Παρόλα αυτά, με την συγκεκριμένη τακτική το πρόγραμμα θα εκτελείται
            // πλέον ακολουθιακά, καθώς κάθε νήμα θα περιμένει την ολοκλήρωση του προηγούμενου πριν ξεκινήσει.
            // Έτσι θα χάσουμε το πλεονέκτημα του ταυτοχρονισμού.
            //
            // threads[i].join();

        }

        // Αναμονή για την ολοκλήρωση των νημάτων
        for (int i = 0; i < THREAD_COUNT; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("All threads have finished.");
        System.out.println("Main thread exiting.");
    }
}