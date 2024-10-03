public class SharedCounter {
    // Κάθε αντικείμενο τύπου SafeCounter έχει το δικό του αντικείμενο κλειδώματος.
    SafeCounter counterA;
    SafeCounter counterB;

    public SharedCounter() {
        this.counterA = new SafeCounter();
        this.counterB = new SafeCounter();
    }

    // Δεν χρειαζομαστε synchronized block εδω, γιατι η μεθοδος increment της SafeCounter κλασης ειναι synchronized.
    public int getCounterA() {
        return counterA.get();
    }

    // Δεν χρειαζομαστε synchronized block εδω, γιατι η μεθοδος increment της SafeCounter κλασης ειναι synchronized.
    public int getCounterB() {
        return counterB.get();
    }

    // Αυτή η μέθοδος αυξάνει τον μετρητή που δίνεται ως όρισμα.
    // Δεν χρειαζομαστε synchronized block εδω, γιατι η μεθοδος increment της SafeCounter κλασης ειναι synchronized.
    // Αν το όρισμα δεν είναι "A" ή "B" τότε κάνουμε throw μια εξαίρεση για να ειδοποιήσουμε τον καλούντα ότι έδωσε λάθος όρισμα.
    public void increment(String counter) {
        if (counter.equals("A")) counterA.increment();
        else if (counter.equals("B")) counterB.increment();
        else throw new IllegalArgumentException("Invalid counter - must be \'A\' or \'B\'");
    }
}
