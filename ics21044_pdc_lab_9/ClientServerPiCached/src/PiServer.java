import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class PiServer {
    private static final int PORT = 1234;
    private static final boolean RUNNING = true;

    public static ServerCache serverCache = new ServerCache();

    public static void main(String args[]) throws IOException {
        // Δημιουργία ενός ServerSocket
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server is listening on port: " + PORT + System.lineSeparator());

        // Αναμονή για σύνδεση από client
        while (RUNNING) {
            // Αναμονή για σύνδεση από client
            Socket clientSocket = serverSocket.accept();
            System.out.println("> Client connected: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

            // Δημιουργία νέου thread για την εξυπηρέτηση του client
            ServerThread serverThread = new ServerThread(clientSocket, serverCache);
            serverThread.start();
        }
    }
}
