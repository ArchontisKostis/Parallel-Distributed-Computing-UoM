// Αντιπροσωπεύει το προκαθορισμένο πρωτόκολλο επικοινωνίας που πρεπει να ακολουθήσει ο client
public class ClientProtocol {
    public String prepareRequest(int numSteps) {
        return String.valueOf(numSteps);
    }

    public String prepareExit() {
        return "-1";
    }

    public void processReply(String reply) {
        System.out.println(reply);
    }
}
