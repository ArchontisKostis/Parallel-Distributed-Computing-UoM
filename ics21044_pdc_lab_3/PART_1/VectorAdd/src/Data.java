// Αυτή η κλάση περιέχει τα δεδομένα που θα χρησιμοποιηθούν στον υπολογισμό του παραδείγματος.
// Στην πράξη οι πίνακες a, b, c μοιράζονται στο πρόγραμμα μας μέσω της κλάσης Data.
public class Data {
    private final int size;

    public double[] a;
    public double[] b;
    public double[] c;

    public Data(int size) {
        this.size = size;
        a = new double[size];
        b = new double[size];
        c = new double[size];
    }

    public void initialize(double valueA, double valueB, double valueC) {
        // Η αρχικοποίηση των πινάκων δεν χρειάζεται να γίνει σε παράλληλο τμήμα
        // αφού δεν υπάρχει κίνδυνος αλληλεπικάλυψης και γιατι η συγκεκριμένη λειτουργία
        // ειναι "χαμηλού κόστους" σε σχέση με τον υπολογισμό που θα ακολουθήσει. Η παραλληλοποίηση
        // αυτής της λειτουργίας θα επιβαρύνει το πρόγραμμα μας με extra overhead και πολυπλοκότητα.
        for(int i = 0; i < size; i++) {
            a[i] = valueA;
            b[i] = valueB;
            c[i] = valueC;
        }
    }
}
