package utils;

import server.Connection;
import server.MultithreadedChatServerTCP;
import server.ServerThread;

import java.io.IOException;

public class CommandHandler {
    public static final String EXIT = "/exit";
    public static final String USERNAME_CMD = "/username";
    public static final String HELP_CMD = "/help";

    public boolean isCommand(String message) {
        return message.startsWith(USERNAME_CMD) || message.equals(HELP_CMD) || message.equals(EXIT);
    }

    public void handleCommand(String message, Connection clientConn, ServerThread serverThread) {
        if (message.startsWith(USERNAME_CMD)) handleUpdateUsername(message, clientConn);
        else if (message.equals(HELP_CMD)) handleShowCommands(clientConn);
        else if (message.equals(EXIT)) handleExit(clientConn, serverThread);
        else handleInvalidCommand(clientConn);
    }

    private boolean handleUpdateUsername(String message, Connection clientConnection) {
        String[] parts = message.split(" ", 2);

        boolean partsIsSize2 = parts.length == 2;
        boolean partsIsEmpty = parts[1].trim().isEmpty();

        if (!partsIsSize2 && partsIsEmpty) {
            clientConnection.writer.println("Invalid username. Usage: /username <new_username>");
            return false;
        }

        String newUsername = parts[1].trim();
        clientConnection.username = newUsername;
        clientConnection.writer.println("Username changed to " + newUsername);
        System.out.println("Client changed username to " + newUsername);

        return true;
    }

    private void handleShowCommands(Connection clientConn) {
        clientConn.writer.println("Available commands:");
        clientConn.writer.println("/username <new_username> - Change your username");
        clientConn.writer.println("/help - Show this help message");
        clientConn.writer.println("/exit - Leave the chat");
    }

    private void handleExit(Connection clientConn, ServerThread serverThread) {
        try {
            clientConn.socket.close();
            System.out.println("You have left the chat.");
            System.exit(0);
        }
        catch (IOException e) { e.printStackTrace(); }
        finally { serverThread.removeFromConnections(clientConn); }
    }

    private void handleInvalidCommand(Connection clientConn) {
        clientConn.writer.println("Invalid command. Type /help for a list of commands.");
    }
}
