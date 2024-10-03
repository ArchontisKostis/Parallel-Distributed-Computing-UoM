// Αντιπροσωπεύει το προκαθορισμένο πρωτόκολλο επικοινωνίας που πρεπει να ακολουθήσει ο server
public class ServerProtocol {
    public String processRequest(String theInput) {
        String response;
        try {
            // Ελέγχουμε αν η είσοδος είναι έγκυρος αριθμός
            int numSteps = Integer.parseInt(theInput.trim());

            // Αν ο χρήστης εισάγει -1 τότε τερματίζουμε το πρόγραμμα
            if (numSteps == -1) return  "EXIT";

            // Θα φτασουμε εδω μονο αν ο χρηστης εχει δωσει αριθμο != -1

            long startTime = System.currentTimeMillis();
            double pi = computePi(numSteps);
            long endTime = System.currentTimeMillis();
            response = String.format("Computed pi = %22.20f\nTime to compute = %f seconds\n", pi, (double) (endTime - startTime) / 1000);
        } catch (NumberFormatException e) {
            response = "Invalid input. Please provide a valid number of steps.";
        }
        return response;
    }

    // Βοηθητική μέθοδος για τον υπολογισμό του π χρησιμοποιώντας RecursivePiTask
    private double computePi(int numSteps) {
        final int LIMIT = 1000000;
        double step = 1.0 / (double) numSteps;
        RecursivePiTask rootTask = new RecursivePiTask(0, numSteps, step, LIMIT);
        rootTask.start();

        try {
            rootTask.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return rootTask.getMyResult() * step;
    }
}
