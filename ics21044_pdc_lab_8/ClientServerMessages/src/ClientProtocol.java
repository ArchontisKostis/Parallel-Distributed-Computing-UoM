import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientProtocol {
    BufferedReader user = new BufferedReader(new InputStreamReader(System.in));

    public String prepareRequest() throws IOException {
        showMenu();
        String action = user.readLine();
        return action.trim();
    }

    public void processReply(String theInput) throws IOException {
        System.out.println("Message Received from the server: " + theInput);
    }

    private void showMenu() throws IOException {
        System.out.println("Available Actions:");
        System.out.println("1. UP message - Convert to uppercase.");
        System.out.println("2. LOW message - Convert to lowercase.");
        System.out.println("3. ENC <message> key - Encode message with key.");
        System.out.println("4. DEC <message> key - Decode message with key.");
        System.out.print("Enter action: ");
    }



    public static void main(String[] args) throws IOException {
        ClientProtocol protocol = new ClientProtocol();

        String input = protocol.prepareRequest();

        System.out.println(input);
    }
}
