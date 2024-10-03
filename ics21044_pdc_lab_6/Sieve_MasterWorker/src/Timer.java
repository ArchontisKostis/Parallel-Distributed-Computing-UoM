public class Timer {
    private String id;
    private long startTime;
    private long endTime;

    public Timer(String id) {
        this.id = id;
        // Start the timer
        this.startTime = System.currentTimeMillis();
    }

    public void stop() {
        // Stop the timer
        this.endTime = System.currentTimeMillis();
    }

    public long getTotalTimeInMillis() {
        return this.endTime - this.startTime;
    }

    public void printTotalTime() {
        System.out.println("Total time for " + this.id + ": " + getTotalTimeInMillis() + "ms");
    }
}
