/**
 * Αυτή η Κλάση χειρίζεται την επικοινωνία μεταξύ του Συντονιστή (Master) και των Εργατών (Workers).
 * Στην πράξη "προετοιμάζει" τα αιτήματα που στέλνονται στους Εργάτες και "επεξεργάζεται" τις απαντήσεις που λαμβάνει από αυτούς.
 * */
public class MasterProtocol {
    private SharedPi mySharedPi;
    private int myId;

    /**
     * Κατασκευαστής της κλάσης MasterProtocol.
     *
     * @param s Ένα αντικείμενο της κλάσης Sum που συγκεντρώνει τα αποτελέσματα από όλους τους εργάτες.
     * @param id  Μοναδικό Αναγνωριστικό του Συντονιστή (Master).
     */
    public MasterProtocol(SharedPi s, int id) {
        mySharedPi = s;
        myId = id;
    }

    /**
     * Μέθοδος που προετοιμάζει το αίτημα προς τους Εργάτες.
     *
     * @param numWorkers Ο αριθμός των Εργατών.
     * @param myStart Η αρχή του διαστήματος που θα υπολογίσει ο κάθε Εργάτης.
     * @param myEnd Το τέλος του διαστήματος που θα υπολογίσει ο κάθε Εργάτης.
     * @param step Το βήμα που θα ακολουθήσει ο κάθε Εργάτης.
     * @return Το αίτημα προς τους Εργάτες.
     */
    public String prepareRequest(int numWorkers, long myStart, long myEnd, double step) {
        return numWorkers + " " + myId + " " + myStart + " " + myEnd + " " + step;
    }

    /**
     * Μέθοδος που επεξεργάζεται την απάντηση που λαμβάνει από τους Εργάτες.
     *
     * @param theInput Η απάντηση που λαμβάνει από τους Εργάτες.
     */
    public void processReply(String theInput) {
        double repl = Double.parseDouble(theInput);
        mySharedPi.addTo(repl);
    }
}
