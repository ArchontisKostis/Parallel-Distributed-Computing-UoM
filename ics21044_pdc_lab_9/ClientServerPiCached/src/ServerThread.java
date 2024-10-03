import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// Κλάση ServerThread που επεκτείνει την κλάση Thread. Κάθε client που συνδέεται στον server
// εξυπηρετείται από ένα αντικείμενο τύπου ServerThread που δημιουργείται από τον server.
// Ετσι μπορούν να εξυπηρετηθούν πολλαπλοί clients ταυτόχρονα.
class ServerThread extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private ServerProtocol protocol;

    public ServerThread(Socket socket, ServerCache serverCache) {
        this.socket = socket;
        this.protocol = new ServerProtocol(serverCache);
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("I/O Error: " + e);
        }
    }

    @Override
    public void run() {
        String input;
        try {
            while ((input = in.readLine()) != null) {
                String response = protocol.processRequest(input);
                out.println(response);
                if ("EXIT".equals(response)) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("I/O Error: " + e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("I/O Error: " + e);
            }
        }
    }
}
