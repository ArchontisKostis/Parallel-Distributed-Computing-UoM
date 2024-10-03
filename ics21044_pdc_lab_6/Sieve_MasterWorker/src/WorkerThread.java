
// Αυτή η κλάση μοντελοποιεί έναν Εργάτη που χρησιμοποιεί ο Master.
public class WorkerThread extends Thread {
    private boolean[] prime;

    WorkerThread(boolean[] prime) {
        this.prime = prime;
    }

    @Override
    public void run() {
        // Επειδή το πρώτος αριθμός που ελέγχεται είναι το 2, ξεκινάμε από το 2.
        // Η μέθοδος αυτή ψάχνει τους αριθμούς που δεν είναι πρώτοι για ένα νήμα εργάτη.
        int p;
        while ((p = Main.getNextPrime()) <= Math.sqrt(Main.totalTasks)) {
            if (p >= 2 && prime[p]) {
                for (int i = p * p; i <= Main.totalTasks; i += p) {
                    prime[i] = false;
                }
            }
        }
    }
}
