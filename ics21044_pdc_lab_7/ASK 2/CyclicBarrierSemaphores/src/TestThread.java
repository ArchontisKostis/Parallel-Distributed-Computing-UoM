public class TestThread extends Thread {
    private int threadID;
    private CyclicBarrier myBarrier;

    public TestThread(int tid, CyclicBarrier bar) {
        this.threadID = tid;
        this.myBarrier = bar;
    }

    public void run() {
        for (;;) {
            System.out.println("Thread"+ threadID+ " started");
            myBarrier.barrier();
            try {
                sleep((int)(Math.random()*1000));
            } catch (InterruptedException e) {}
            System.out.println("Thread"+ threadID+ " reached barrier");
            myBarrier.barrier();
            System.out.println("Thread"+ threadID+ " passed barrier");
            myBarrier.barrier();
        }
    }
}
