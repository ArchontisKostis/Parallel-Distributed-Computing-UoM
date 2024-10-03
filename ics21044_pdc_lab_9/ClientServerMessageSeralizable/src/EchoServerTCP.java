import java.io.*;
import java.net.*;

public class EchoServerTCP {
    private static final int PORT = 1234;

    public static void main(String[] args) throws Exception {
        // Δημιουργία του ServerSocket
        ServerSocket connectionSocket = new ServerSocket(PORT);
        System.out.println("Server is listening to port: " + PORT);

        // Αποδοχή του αιτήματος του Client
        Socket dataSocket = connectionSocket.accept();
        System.out.println("Received request from " + dataSocket.getInetAddress());

        // Δημιουργία των streams για την ανταλλαγή δεδομένων με τον Client
        ObjectOutputStream out = new ObjectOutputStream(dataSocket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(dataSocket.getInputStream());

        // Δημιουργία αντικειμένου της κλάσης ServerProtocol
        ServerProtocol app = new ServerProtocol();
        ClientRequest request;
        ServerResponse response;

        // Επανάληψη για να επικοινωνήσει ο Server με τον Client
        while ((request = (ClientRequest) in.readObject()) != null) {
            // Λήψη του αιτήματος από τον Client
            response = app.processRequest(request);

            // Αποστολή της απάντησης στον Client
            out.writeObject(response);

            // Αν το αίτημα είναι EXIT, τερματίζει την επανάληψη
            if (request.getAction().equalsIgnoreCase("EXIT")) {
                break;
            }
        }

        dataSocket.close();
        System.out.println("Data socket closed");
    }
}
