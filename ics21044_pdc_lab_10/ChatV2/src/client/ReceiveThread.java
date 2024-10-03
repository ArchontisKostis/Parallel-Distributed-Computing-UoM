package client;

import protocols.ChatClientProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReceiveThread implements Runnable {
    private Socket dataSocket;
    private InputStream is;
    private BufferedReader in;
    private ChatClientProtocol app;

    public ReceiveThread(Socket soc) throws IOException {
        dataSocket = soc;
        is = dataSocket.getInputStream();
        in = new BufferedReader(new InputStreamReader(is));
        app = new ChatClientProtocol();
    }

    public void run() {
        try {
            String inmsg;
            while ((inmsg = in.readLine()) != null) {
                app.receiveMessage(inmsg);
            }
        } catch (IOException e) {
            System.err.println("ReceiveThread error: " + e.getMessage());
        } finally {
            try {
                dataSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
    }
}
