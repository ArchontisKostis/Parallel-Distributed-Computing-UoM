// Η κλάση αντιπροσωπεύει τον server πρωτόκολλο για την επικοινωνία Server και Client.
// Πιο συγκεκριμένα είναι το πρωτόκολλο που θα ακολουθήσει ο Server για να επικοινωνήσει με τον Client.
public class ServerProtocolCalculator {
    // Επεξεργασία του request που λαμβάνει ο Server από τον Client και εκτέλεση της αντίστοιχης πράξης
    public String processRequest(String request) {
        String[] parts = request.split(" ");

        if (parts.length != 3) return "E 1"; // Error code 1: Incorrect number of operands

        char op = parts[0].charAt(0);
        int a, b;
        try {
            a = Integer.parseInt(parts[1]);
            b = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            return "E 2"; // Error code 2: Invalid number format
        }

        int result;
        // Calculate the result based on the operator
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
                if (b == 0) return "E 3"; // Error code 3: Division by zero
                result = a / b;
                break;
            default:
                return "E 4"; // Error code 4: Invalid operator
        }

        return "R " + result;
    }
}
