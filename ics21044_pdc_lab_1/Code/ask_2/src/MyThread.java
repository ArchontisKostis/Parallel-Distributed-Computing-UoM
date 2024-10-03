// Η κλάση MyThread κληρονομεί την κλάση Thread.
public class MyThread extends Thread {
    private String message;

    public MyThread(String message) { this.message = message; }

    @Override
    public void run() { System.out.println("MyThread: " + message); }
}
