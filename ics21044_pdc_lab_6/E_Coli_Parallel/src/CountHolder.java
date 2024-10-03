import java.util.Arrays;

public class CountHolder {
    private int count;
    private final Object countLock;

    private char[] matchArray;
    private final Object matchLock;

    public CountHolder(int matchArraySize) {
        count = 0;
        countLock = new Object();

        matchArray = initMatchArray(matchArraySize);
        matchLock = new Object();
    }

    public int getCount() {
        synchronized (countLock) {
            return count;
        }
    }

    public void incrementCount() {
        // Κρίσιμο Τμήμα
        synchronized (countLock) {
            count++;
        }
    }

    public char[] getMatchArray() {
        synchronized (matchLock) {
            return matchArray;
        }
    }

    public void setMatchAt(int index, char value) {
        synchronized (matchLock) {
            matchArray[index] = value;
        }
    }

    public void incrementMatchAt(int index) {
        synchronized (matchLock) {
            matchArray[index]++;
        }
    }

    private char[] initMatchArray(int size) {
        // Η αρχικοποίηση του πίνακα δεν χρειαζεται να παραλληλοποιηθεί καθώς γίνεται μόνο μια φορά και είναι μια απλή λειτουργία.
        char[] matchArray = new char[size];
        Arrays.fill(matchArray, '0');
        return matchArray;
    }
}
