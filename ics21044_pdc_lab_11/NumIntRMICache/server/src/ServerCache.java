import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ServerCache implements Serializable {
    private final Map<Integer, Double> cache;
    private final Object lock;

    public ServerCache() {
        this.cache = new HashMap<>();
        this.lock = new Object();
    }

    public void put(int numSteps, double pi) {
        // Early returns για τα edge cases (αρνητικός αριθμός βημάτων, ήδη υπολογισμένος αριθμός βημάτων)
        if (numSteps < 0) return;
        if (isCached(numSteps)) return;

        synchronized (lock) {
            cache.put(numSteps, pi);
        }
    }

    public Optional<Double> getItem(int numSteps) {
        synchronized (lock) {
            return Optional.ofNullable(cache.get(numSteps));
        }
    }

    public boolean isCached(int numSteps) {
        synchronized (lock) {
            return cache.containsKey(numSteps);
        }
    }

    public void clear() {
        synchronized (lock) {
            cache.clear();
        }
    }
}
