// Το αντικείμενο αυτό χρησιμοποιείται για τον διαμοιρασμό των δεδομένων μεταξύ των νημάτων.
public class Data {
    public double[][] a;
    public double[][] b;
    public double[][] c;

    public Data(int size) {
        a = new double[size][size];
        b = new double[size][size];
        c = new double[size][size];
    }

    // Αρχικοποίηση των πινάκων με τιμές valueA, valueB, valueC.
    public void initialize(double valueA, double valueB, double valueC) {
        for(int i = 0; i < a.length; i++)
            for(int j = 0; j < a[i].length; j++) {
                a[i][j] = valueA;
                b[i][j] = valueB;
                c[i][j] = valueC;
            }
    }
}
