public class SharedData {
    private String geneSequence;
    private String pattern;

    public SharedData(String geneSequence, String pattern) {
        this.geneSequence = geneSequence;
        this.pattern = pattern;
    }

    public String getGeneSequence() {
        return geneSequence;
    }

    public String getPattern() {
        return pattern;
    }
}
