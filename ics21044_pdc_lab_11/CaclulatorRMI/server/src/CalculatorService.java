public class CalculatorService {
    // Εξηγηση Error Codes (εμφανίζεται αν ο client στείλει `help`)
    private static final String HELP_MESSAGE = "Error Codes:" + System.lineSeparator() +
            "E 1: Incorrect number of arguments" + System.lineSeparator() +
            "E 2: Invalid number format" + System.lineSeparator() +
            "E 3: Division by zero" + System.lineSeparator() +
            "E 4: Invalid operator" + System.lineSeparator();

    public static final String HELP_CMD = "HELP";

    // Μέθοδος που δέχεται το αίτημα του πελάτη και επιστρέφει το αποτέλεσμα
    public String processRequest(String request) {
        if (request.equalsIgnoreCase(HELP_CMD)) return HELP_MESSAGE;

        String[] parts = splitRequest(request);

        // Error code 1: Incorrect number of arguments
        if (parts == null) return "E 1";

        Integer[] operands = parseOperands(parts);

        // Error code 2: Invalid number format
        if (operands == null) return "E 2";

        String result = performOperation(parts[0].charAt(0), operands[0], operands[1]);

        // Επιστροφή Error Code (αν υπάρχει)
        if (result.startsWith("E ")) return result;

        return "R " + result; // Return result with prefix 'R'
    }

    private String[] splitRequest(String request) {
        String[] parts = request.split(" ");
        if (parts.length != 3) return null;
        return parts;
    }

    private Integer[] parseOperands(String[] parts) {
        try {
            int a = Integer.parseInt(parts[1]);
            int b = Integer.parseInt(parts[2]);
            return new Integer[]{a, b};
        }
        catch (NumberFormatException e) { return null; }
    }

    private String performOperation(char operator, int a, int b) {
        switch (operator) {
            case '+':
                return String.valueOf(a + b);
            case '-':
                return String.valueOf(a - b);
            case '*':
                return String.valueOf(a * b);
            case '/':
                if (b == 0) return "E 3"; // Error code 3: Division by zero
                return String.valueOf(a / b);
            default:
                return "E 4"; // Error code 4: Invalid operator
        }
    }
}
