public class SieveThread extends Thread {
    private boolean[] prime;
    private int threadId;
    private int numThreads;
    private int limit;

    SieveThread(boolean[] prime, int threadId, int numThreads, int limit) {
        this.prime = prime;
        this.threadId = threadId;
        this.numThreads = numThreads;
        this.limit = limit;
    }

    @Override
    public void run() {
        for (int i = threadId + 2; i < limit; i += numThreads) {
            if (prime[i]) {
                for (int j = i * i; j < prime.length; j += i) {
                    prime[j] = false;
                }
            }
        }
    }

    public void printThreadWork() {
        System.out.println("Thread " + threadId + " is working on numbers " + (threadId + 2) + " to " + (limit - 1));
    }
}
