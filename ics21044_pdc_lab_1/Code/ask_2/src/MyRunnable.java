// Η κλάση αυτή δεν κληρονομεί την κλάση Thread, αλλά υλοποιεί το interface Runnable.
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
