import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

// Αντιπροσωπεύει την κρυφή μνήμη του server. Για να είναι thread safe δεν χρησιμοποιούμε εμείς Locks αλλά χρησιμοποιώ την κλάση ConcurrentHashMap που είναι thread safe απο μόνη της.
public class ServerCache {
    private final ConcurrentHashMap<Integer, Double> cache;     // Για πειραματισμό θα χρησιμοποιήσω ConcurrentHashMap για συγχρονισμό αντι για locks και synchronized

    public ServerCache() {
        cache = new ConcurrentHashMap<>();
    }

    // Προσθήκη στοιχείου στην κρυφή μνήμη
    public void put(int key, Double value) {
        // Early Returns για τα edge cases (αρνητικός αριθμός βημάτων ή ήδη υπολογισμένο π για τον δωθεν αριθμό βημάτων)
        if (key < 0) return;
        if (isCached(key)) return;

        cache.put(key, value);
    }

    // Επιστροφή τιμής από την κρυφή μνήμη
    public Optional<Double> get(int key) {
        if (!isCached(key)) return Optional.empty();

        // Θα φτάσουμε εδώ μόνο αν ο πίνακας cache περιέχει το key
        Double cachedValue = cache.get(key);
        return Optional.of(cachedValue);
    }

    // Έλεγχος αν το στοιχείο είναι ήδη στην κρυφή μνήμη
    public boolean isCached(int key) {
        return cache.containsKey(key);
    }

    // Επιστροφή του μεγέθους της κρυφής μνήμης
    public int size() { return cache.size(); }

    // Έλεγχος αν η κρυφή μνήμη είναι άδεια
    public boolean isEmpty() { return cache.isEmpty(); }
}
