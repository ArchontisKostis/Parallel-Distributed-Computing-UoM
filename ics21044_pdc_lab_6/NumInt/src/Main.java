
// Για να παραλληλοποιήσουμε το πρόγραμμα θα φτιάξουμε μία κλάση RecursiveTask η οποια θα χειρίζεται την αναδρομική κατανομή των εργασιϋν.
public class Main {
    public static final int NUM_STEPS = 100000000;
    // 200000
    public static final int LIMIT = 1000000;


    public static void main(String[] args) {
        int numSteps = NUM_STEPS;
        int limit = LIMIT;

        /* parse command line
        if (args.length != 1) {
            System.out.println("arguments:  number_of_steps");
            System.exit(1);
        }
        try {
            numSteps = Long.parseLong(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("argument " + args[0] + " must be a long int");
            System.exit(1);
        }
        */

        long startTime = System.currentTimeMillis();

        // Υπολογισμός του βήματος της σειράς
        double step = 1.0 / (double) numSteps;
        RecursivePiTask rootTask = new RecursivePiTask(0, numSteps, step, limit);
        rootTask.start();

        // Αναμονή του Root Task να τελειώσει
        try {
            rootTask.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        double pi = rootTask.getMyResult() * step;

        long endTime = System.currentTimeMillis();

        System.out.println("Parallel program results with " + numSteps + " steps and limit " + limit);
        System.out.printf("Computed pi = %22.20f\n", pi);
        System.out.printf("Difference between estimated pi and Math.PI = %22.20f\n", Math.abs(pi - Math.PI));
        System.out.printf("Time to compute = %f seconds\n", (double) (endTime - startTime) / 1000);
    }
}