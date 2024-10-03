public class Main {
    public static void main(String[] args) {
        checkArgs(args);

        int numSteps = Integer.parseInt(args[0]);
        double step = 1.0 / (double) numSteps;
        double sum = 0.0;

        long startTime = System.currentTimeMillis();

        // Μπορουμε να παραλληλοποιησουμε τον υπολογισμο του π παραλληλα με την χρηση threads
        // Στην πραξη αυτη η for μπορει να σπασει σε τμηματα και κάθε thread να υπολογιζει το τμημα του
        for (int i = 0; i < numSteps; i++) {
            double x = calculateX(i, step);
            sum += f(x);
        }

        double pi = sum * step;

        long endTime = System.currentTimeMillis();

        long elapsedTime = endTime - startTime;

        System.out.println("Approximation of pi: " + pi);
        System.out.println("Time taken: " + elapsedTime + " ms");
    }

    public static void checkArgs(String[] args) {
        // Make sure we have arguments
        if (args.length < 1) {
            System.out.println("Usage: java Main <numSteps>");
            System.exit(1);
        }
    }

    public static double calculateX(int i, double step) {
        return (i + 0.5) * step;
    }

    public static double f(double x) {
        return 4.0 / (1.0 + x * x);
    }
}