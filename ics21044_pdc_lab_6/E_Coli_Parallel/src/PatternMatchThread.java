public class PatternMatchThread extends Thread {
    private int start;
    private int end;
    private final SharedData sharedData;
    private final CountHolder countHolder;

    public PatternMatchThread(SharedData sharedData, CountHolder countHolder, int start, int end) {
        this.start = start;
        this.end = end;
        this.sharedData = sharedData;
        this.countHolder = countHolder;
    }

    @Override
    public void run() {
        String geneSequence = sharedData.getGeneSequence();
        String pattern = sharedData.getPattern();
        int patternLength = pattern.length();
        int geneSequenceLength = geneSequence.length();

        for (int position = start; position < end; position++) {
            boolean shouldExit = position + patternLength > geneSequenceLength;
            if (shouldExit) break;

            // Θα φτασουμε εδω μονο αν position + patternLength <= geneSequenceLength
            boolean isMatch = isMatch(patternLength, geneSequence, position, pattern);
            if (isMatch) {
                // Θα φτασουμε εδω μονο αν υπαρχει αντιστοιχια
                countHolder.incrementCount();
                countHolder.setMatchAt(position, '1');
            }
        }

    }

    private static boolean isMatch(int patternLength, String geneSequence, int position, String pattern) {
        boolean isMatch = true;
        for (int i = 0; i < patternLength; i++) {
            if (geneSequence.charAt(position + i) != pattern.charAt(i)) {
                isMatch = false;
                break;
            }
        }
        return isMatch;
    }
}
