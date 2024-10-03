import java.util.Optional;

// Αντιπροσωπεύει το προκαθορισμένο πρωτόκολλο επικοινωνίας που πρεπει να ακολουθήσει ο server

public class ServerProtocol {
    public static final int STOP_FLAG = -1;
    private final ServerCache cache;

    public ServerProtocol(ServerCache cache) { this.cache = cache; }

    public String processRequest(String theInput) {
        try {
            int numSteps = Integer.parseInt(theInput.trim());

            // Ελεγχος αν η Cache δεν είναι άδεια
            // Αν ειναι αδεια, δεν υπάρχει λόγος να κάνουμε lookup και μπορουμε να υπολογίσουμε το π κατευθείαν
            // Πρακτικα η cache θα είναι άδεια την πρώτη φορά που θα υπολογίσουμε το π για έναν δεδομένο αριθμό βημάτων
            if (!cache.isEmpty()) {
                String cachedValue = checkCacheForPi(numSteps);
                if (cachedValue != null) return cachedValue;
            }

            // Αν ο χρήστης εισάγει -1 τότε τερματίζουμε το πρόγραμμα
            if (numSteps == ServerProtocol.STOP_FLAG) {
                System.out.println("Received EXIT command from client.");
                System.out.println("Closing connection with client...");
                return "EXIT";
            }

            System.out.println("Computing & Caching π for key: " + numSteps);

            long startTime = System.currentTimeMillis();
            double pi = computePi(numSteps);
            long endTime = System.currentTimeMillis();

            cache.put(numSteps, pi);

            double timeToCompute = (double) (endTime - startTime) / 1000;
            return String.format("Computed pi = %22.20f\nTime to compute = %f seconds\n", pi, timeToCompute);
        }
        catch (NumberFormatException e) { return "Invalid input. Please provide a valid number of steps."; }
    }

    // Βοηθητική μέθοδος για τον υπολογισμό του π χρησιμοποιώντας RecursivePiTask
    private double computePi(int numSteps) {
        final int LIMIT = 1000000;
        double step = 1.0 / (double) numSteps;
        RecursivePiTask rootTask = new RecursivePiTask(0, numSteps, step, LIMIT);
        rootTask.start();

        try { rootTask.join(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return rootTask.getMyResult() * step;
    }

    // Έλεγχος της cache για το που αντιστοιχεί στον δωθέν αριθμό βημάτων
    private String checkCacheForPi(int numSteps) {
        Optional<Double> cachedValueOpt = cache.get(numSteps);

        if (cachedValueOpt.isEmpty()) return null;

        System.out.println("Retrieving cached π value for key: " + numSteps);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Computed pi = %22.20f (cached)" + System.lineSeparator(), cachedValueOpt.get()));
        sb.append("Time to compute = 0 seconds"  + System.lineSeparator());

        return sb.toString();
    }
}
