package server;

import java.net.*;
import java.io.*;
import java.util.*;

public class MultithreadedChatServerTCP {
    private static final int PORT = 1234;
    private static SharedConnections sharedConnections = new SharedConnections();

    public static void main(String args[]) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java server.MultithreadedChatServerTCP <number_of_clients>");
            return;
        }

        int numClients = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is waiting for clients on port: " + PORT);

            // Run Continuously
            while (true) {
                // Accept clients until we have the desired number
                runServerApplication(serverSocket, numClients);
            }
        }
    }

    private static void runServerApplication(ServerSocket serverSocket, int numClients) throws IOException {
        while (sharedConnections.getSize() < numClients) {
            // Start a new thread for each client
            int clientId = sharedConnections.getSize();

            Socket clientSocket = serverSocket.accept();

            Connection newConn = new Connection(clientSocket, clientId);
            sharedConnections.addConnection(newConn);

            ServerThread serverThread = new ServerThread(sharedConnections, clientId);
            new Thread(serverThread).start();
        }
    }
}
