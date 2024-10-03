import java.util.Map;

public class ServerProtocolCalculator {

    public String processRequest(String request) {
        String[] parts = request.split(" ");

        if (parts.length != 3) return "E 1"; // Error code 1: Μη αποδεκτός αριθμός ορισμάτων

        // Εξαγωγή του τελεστή και των τελεστεών
        char op = parts[0].charAt(0);
        int a, b;
        try {
            a = Integer.parseInt(parts[1]);
            b = Integer.parseInt(parts[2]);
        }
        catch (NumberFormatException e) {
            return "E 2"; // Error code 2: Δεν πηραμε αριθμούς σε σωστή μορφή
        }

        int result;

        // Υπολογισμός του αποτελέσματος ανάλογα με τον τελεστή
        switch (op) {
            case '+':
                result = a + b;
                break;
            case '-':
                result = a - b;
                break;
            case '*':
                result = a * b;
                break;
            case '/':
                if (b == 0) return "E 3"; // Error code 3: Διαίρεση με το 0
                result = a / b;
                break;
            default:
                return "E 4"; // Error code 4: Μη αποδεκτός τελεστής
        }

        return "R " + result;
    }
}
