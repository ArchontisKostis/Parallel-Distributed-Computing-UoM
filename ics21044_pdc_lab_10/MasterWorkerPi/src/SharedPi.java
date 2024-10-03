
/**
 * Η κλάση αυτή χρησιμοποιείται για την αποθήκευση του συνολικού Pi που υπολογίζεται αφού ενώσουμε τα αποτελέσματα των νημάτων.
 */
public class SharedPi {
    private double result;

    public SharedPi() {
        result = 0.0;
    }

    /**
     * Η μέθοδος αυτή προσθέτει τον αριθμό toAdd στο αποτέλεσμα.
     * @param toAdd Ο αριθμός που θα προστεθεί στο αποτέλεσμα.
     */
    public synchronized void addTo(double toAdd) {
        result += toAdd;
    }

    /**
     * Η μέθοδος αυτή εκτυπώνει το τελικό αποτέλεσμα της προσέγγισης του Pi.
     */
    public synchronized void printResult() {
        result = result / 10000;
        System.out.println("Result = " + result);
    }
}
