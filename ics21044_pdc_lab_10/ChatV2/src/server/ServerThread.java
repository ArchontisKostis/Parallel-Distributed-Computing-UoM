package server;

import protocols.ServerProtocol;
import utils.CommandHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ServerThread implements Runnable {
    private SharedConnections connections;
    private CommandHandler cmdHandler;
    private int clientId;

    public ServerThread(SharedConnections connections, int clientId) {
        this.connections = connections;
        this.clientId = clientId;
        this.cmdHandler = new CommandHandler();
    }

    public void run() {
        try {
            Connection clientConnection = connections.getConnectionByIndex(clientId);
            BufferedReader reader = clientConnection.reader;
            PrintWriter writer = clientConnection.writer;
            ServerProtocol protocol = new ServerProtocol();

            // Read username from the client
            String username = reader.readLine();
            if (username != null && !username.trim().isEmpty()) {
                clientConnection.username = username;
                System.out.println("Client " + clientId + " set username to " + username);
            } else {
                username = "User" + clientId; // Default username if none provided
                clientConnection.username = username;
            }

            writer.println("Welcome, " + username + "! Type /username <new_username> to change your username.");
            writer.println(clientConnection.username + " hopped in. Say hi!");

            String message;
            while ((message = reader.readLine()) != null) {
                if (cmdHandler.isCommand(message))
                    cmdHandler.handleCommand(message, clientConnection, this);
                else
                    handleMessage(message, clientConnection, protocol);
            }

            cmdHandler.handleCommand(CommandHandler.EXIT, clientConnection, this);
        } catch (IOException e) {
            System.out.println("I/O Error: " + e);
        }
    }

    private void handleMessage(String message, Connection clientConnection, ServerProtocol protocol) {
        String formattedMessage = clientConnection.username + ": " + message;
        String response = protocol.processRequest(formattedMessage);

        synchronized (connections) {
            for (int i = 0; i < connections.getSize(); i++) {
                if (i != clientId) {
                    connections.getConnectionByIndex(i).writer.println(response);
                }
            }
        }
    }

    public void removeFromConnections(Connection client) {
        connections.removeConnection(client);
    }
}
