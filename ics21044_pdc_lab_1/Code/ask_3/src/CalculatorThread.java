public class CalculatorThread extends Thread {
    // Αναγνωριστικό του νήματος + ο αριθμος που θα χρησιμοποιηθεί στους υπολογισμούς
    private int i;

    // Το αποτέλεσμα του υπολογισμού που έκανε το νήμα
    private int result;

    public CalculatorThread(int i) {
        this.i = i;
    }

    @Override
    public void run() {
        System.out.println("Thread " + i + " started.");

        for (int j = 1; j <= Main.CALCULATION_CEILING; j++) {
            result = j * (i + 1);

            System.out.printf("%d * %d = %d\n", j, i + 1, result);
        }

        System.out.println("Thread " + i + " finished.");
    }

}
