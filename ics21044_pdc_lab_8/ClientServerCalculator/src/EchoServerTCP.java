import java.net.*;
import java.io.*;

public class EchoServerTCP {
    private static final int PORT = 1234;
    private static final String EXIT = "!";

    public static void main(String[] args) throws IOException {
        // Create a ServerSocket to listen for incoming connections
        ServerSocket connectionSocket = new ServerSocket(PORT);
        System.out.println("Server is listening on port: " + PORT);

        // Accept a connection from a client
        Socket dataSocket = connectionSocket.accept();
        System.out.println("Received request from " + dataSocket.getInetAddress() + ":" + dataSocket.getPort());

        // Set up input and output streams
        InputStream is = dataSocket.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        OutputStream os = dataSocket.getOutputStream();
        PrintWriter out = new PrintWriter(os, true);

        // Initialize the server protocol
        ServerProtocolCalculator app = new ServerProtocolCalculator();

        String inmsg;
        while ((inmsg = in.readLine()) != null) {
            // Print received message and client info
            System.out.println("Received message from client: " + inmsg);

            if (inmsg.equals(EXIT)) {
                // Log the exit condition and send a confirmation message to the client
                System.out.println("Exit command received from client. Server shutting down...");
                out.println("Server shutting down.");
                break;
            }

            // Process the request and send the response
            String outmsg = app.processRequest(inmsg);
            System.out.println("Sending response to client: " + outmsg);
            out.println(outmsg);
        }

        // Close the socket and server resources
        dataSocket.close();
        connectionSocket.close();
        System.out.println("Data socket closed. Server stopped.");
    }
}
