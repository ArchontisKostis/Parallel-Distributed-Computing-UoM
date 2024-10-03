public class CounterThread extends Thread {
    SharedCounter count;

    public CounterThread(SharedCounter counter) {
        this.count = counter;
    }

    @Override
    public void run() {
        for (int i = 0; i < DoubleCounterSync.numIncrements; i++) {
            count.increment("A");
            count.increment("B");
        }
    }
}
