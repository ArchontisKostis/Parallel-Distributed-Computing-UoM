public class Main {
    public static void main(String[] args) {

        int numThreads = 2;

        if (args.length != 1) {
            System.out.println("Usage: java barrierMain <number of threads>");
            System.exit(1);
        }

        try {
            numThreads = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException nfe) {
            System.out.println("Integer argument expected");
            System.exit(1);
        }

        CyclicBarrier testBarrier = new CyclicBarrier(numThreads);

        TestThread testThreads[] = new TestThread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            testThreads[i] = new TestThread(i, testBarrier);
            testThreads[i].start();
        }
    }
}
