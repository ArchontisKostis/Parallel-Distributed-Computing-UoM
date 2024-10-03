import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientProtocolCalculator {
    BufferedReader user = new BufferedReader(new InputStreamReader(System.in));

    public String prepareRequest() throws IOException {
        String request = "";
        boolean done = false;   // Flag για να ξέρουμε πότε τερματίζει η επικοινωνία

        while (!done) {
            String data = showUserPrompt();
            done = checkData(data);
            if (done) request = buildRequestMessage(data);
        }
        return request;
    }

    private String showUserPrompt() throws IOException {
        System.out.println("Enter operation and operands (e.g., + 5 3): ");

        return user.readLine();
    }

    private boolean checkData(String data) {
        // Αν λαβαμε "!" τερματίζουμε την επικοινωνία
        if (data.equals("!")) return true;

        // Έλεγχος αν τα δεδομένα είναι στη σωστή μορφή (prefix notation -> operator operand1 operand2)
        String[] parts = data.split(" ");

        if (parts.length != 3) return false;

        try {
            char op = parts[0].charAt(0);
            boolean validOperator = op == '+' || op == '-' || op == '*' || op == '/';

            return validOperator;
        }
        catch (NumberFormatException e) { return false; }
    }

    private String buildRequestMessage(String data) {
        return data;
    }

    public void processReply(String reply) {
        String[] parts = reply.split(" ");
        if (parts[0].equals("R")) {
            System.out.println("Result: " + parts[1]);
        } else if (parts[0].equals("E")) {
            System.out.println("Error code: " + parts[1]);
        }
    }
}
