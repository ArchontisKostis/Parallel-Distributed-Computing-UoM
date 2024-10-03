import java.io.*;
import java.net.*;

public class EchoClientTCP {
    private static final String HOST = "localhost";
    private static final int PORT = 1234;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Σύνδεση με τον Server
        Socket dataSocket = new Socket(HOST, PORT);
        ObjectInputStream in = new ObjectInputStream(dataSocket.getInputStream());
        ObjectOutputStream out = new ObjectOutputStream(dataSocket.getOutputStream());

        System.out.println("Connection to " + HOST + " established");

        // Δημιουργία αντικειμένου της κλάσης ClientProtocol
        ClientProtocol app = new ClientProtocol();
        ClientRequest request;
        ServerResponse response;

        // Επανάληψη για να επικοινωνήσει ο Client με τον Server
        while (true) {
            // Προετοιμασία του αιτήματος προς τον Server
            request = app.prepareRequest();

            // Αν το αίτημα είναι null, συνεχίζει στο επόμενο βήμα της επανάληψης
            if (request == null) continue;

            // Αποστολή του αιτήματος στον Server
            out.writeObject(request);

            // Αν το αίτημα είναι EXIT, τερματίζει την επανάληψη
            if (request.getAction().equalsIgnoreCase("EXIT")) {
                break;
            }

            // Θα φτάσει εδώ μόνο αν το αίτημα δεν είναι EXIT

            // Λήψη της απάντησης από τον Server
            response = (ServerResponse) in.readObject();

            // Επεξεργασία της απάντησης τπυ Server από τον Client
            app.processReply(response);
        }

        // Κλείσιμο των streams και του socket
        out.writeObject(new ClientRequest("EXIT", "", 0));
        dataSocket.close();
        System.out.println("Data Socket closed");
    }
}
