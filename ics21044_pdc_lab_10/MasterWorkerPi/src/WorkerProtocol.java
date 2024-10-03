/**
 * WorkerProtocol.java
 * Αυτή η κλάση χρησιμοποιείται για να ορίσει το πρωτόκολλο για τους κόμβους εργαζομένων.
 * Περιέχει τη λογική για τον υπολογισμό του αθροίσματος του τμήματος που ανατίθεται στον εργαζόμενο.
 * Ο εργαζόμενος λαμβάνει το τμήμα για υπολογισμό και επιστρέφει το αποτέλεσμα.
 * Συνδιάζοντας το παραπάνω αποτέλεσμα μπορούμε να πάρουμε την τελική τιμή του π.
 */
public class WorkerProtocol {
    private long myStart;
    private long myEnd;
    private double step;

    public WorkerProtocol(long myStart, long myEnd, double step) {
        this.myStart = myStart;
        this.myEnd = myEnd;
        this.step = step;
    }

    /**
     * Μέθοδος που υπολογίζει το αποτέλεσμα του τμήματος που ανατίθεται στον εργαζόμενο.
     *
     * @param theInput Το αίτημα που λαμβάνει από τον Συντονιστή (Master).
     * @return Το αποτέλεσμα του τμήματος που ανατίθεται στον εργαζόμενο.
     */
    public String compute(String theInput) {
        String[] splited = theInput.split("\\s+");
        int id = Integer.parseInt(splited[1]);

        System.out.println("Worker " + id + " calculating π segment");

        double result = computeSumNow();
        System.out.println("Worker " + id + " result: " + result);

        return String.valueOf(result);
    }

    /**
     * Μέθοδος που υπολογίζει το άθροισμα του τμήματος που ανατίθεται στον εργαζόμενο.
     *
     * @return Το άθροισμα του τμήματος που ανατίθεται στον εργαζόμενο.
     */
    private double computeSumNow() {
        double sum = 0.0;
        for (long i = myStart; i < myEnd; ++i) {
            double x = ((double) i + 0.5) * step;
            sum += 4.0 / (1.0 + x * x);
        }
        return sum;
    }
}
