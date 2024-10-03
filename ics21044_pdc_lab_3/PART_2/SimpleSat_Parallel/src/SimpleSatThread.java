import java.awt.*;
import java.awt.image.BufferedImage;

public class SimpleSatThread extends Thread {
    private final int start;
    private final int end;
    private final int size;

    public SimpleSatThread(int threadStart, int threadEnd, int circuitSize) {
        this.start = threadStart;
        this.end = threadEnd;
        this.size = circuitSize;
    }

    // Κάθε νήμα αναλαμβάνει τον έλεγχο για ένα τμήμα των συνδυασμών
    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            Main.check_circuit(i, size);
        }
    }
}
