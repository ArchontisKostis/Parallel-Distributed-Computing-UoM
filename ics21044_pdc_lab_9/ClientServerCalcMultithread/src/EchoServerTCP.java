import java.net.*;
import java.io.*;

public class EchoServerTCP {
    private static final int PORT = 1234;
    private static final String EXIT = "!";

    public static void main(String[] args) throws IOException {
        // Δημιουργία του ServerSocket για την αναμονή σύνδεσης με τους Clients
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server is listening on port: " + PORT);

        // Ο Server δεχεται συνδέσεις συνεχώς
        while (true) {
            // Αναμονή για σύνδεση από Client
            Socket clientSocket = serverSocket.accept();
            InetAddress clientAddress = clientSocket.getInetAddress();
            System.out.println("Received request from " + clientAddress + ":" + clientSocket.getPort());

            // Δημιουργία νέου Thread για την εξυπηρέτηση του Client
            ServerThreadCalculator clientThread = new ServerThreadCalculator(clientSocket);
            clientThread.start();
        }
    }
}
