package server;

import java.io.*;
import java.net.Socket;

public class Connection {
    public Socket socket;
    InputStream inputStream;
    OutputStream outputStream;
    BufferedReader reader;
    public PrintWriter writer;
    public String username;
    int clientID;

    Connection(Socket socket, int clientID) throws IOException {
        this.socket = socket;
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
        this.reader = new BufferedReader(new InputStreamReader(inputStream));
        this.writer = new PrintWriter(outputStream, true);
        this.clientID = clientID;
    }

    // Equals is based on clientID
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Connection that = (Connection) obj;
        return clientID == that.clientID;
    }
}
