package server;

import server.MultithreadedChatServerTCP;

import java.util.*;

public class SharedConnections {
    private List<Connection> connections;
    private Object lock;

    public SharedConnections() {
        this.connections = new ArrayList<>();
        this.lock = new Object();
    }

    public boolean addConnection(Connection connectionToAdd) {
        Optional<Connection> foundOpt = findConnectionById(connectionToAdd.clientID);
        if (foundOpt.isPresent()) return false;

        synchronized (lock) {
            this.connections.add(connectionToAdd);
            System.out.println("Received request from " + connectionToAdd.socket.getInetAddress() + ":" + connectionToAdd.socket.getPort());
            return true;
        }
    }

    public boolean removeConnection(Connection connToDelete) {
        synchronized (lock) {
            return this.connections.remove(connToDelete);
        }
    }

    public Optional<Connection> findConnectionById(int id) {
        return this.connections.stream()
            .filter(connection -> connection.clientID == id)
            .findFirst();
    }

    public int getSize() {
        return this.connections.size();
    }

    public Connection getConnectionByIndex(int index) {
        synchronized (lock) {
            return this.connections.get(index);
        }
    }

    public List<Connection> getConnections() {
        synchronized (lock) {
            return this.connections;
        }
    }
}
