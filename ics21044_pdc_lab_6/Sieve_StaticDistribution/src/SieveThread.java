public class SieveThread extends Thread {
    private int start;
    private int end;
    private boolean[] prime;
    private int size;

    public SieveThread(int start, int end, boolean[] prime) {
        this.start = start;
        this.end = end;
        this.prime = prime;
        this.size = prime.length;
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            if (prime[i]) {
                for (int j = i * i; j < size; j += i) {
                    prime[j] = false;
                }
            }
        }
    }
}
