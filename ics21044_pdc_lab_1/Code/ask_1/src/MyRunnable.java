// Η κλάση αυτή δεν κληρονομεί την Thread αλλα υλοποιεί το Runnable interface.
public class MyRunnable implements Runnable {
    private String message;

    public MyRunnable(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        System.out.println("MyRunnable: " + message);
    }
}
