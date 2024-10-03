package client;

import java.io.IOException;
import java.net.Socket;

public class ChatClient {
    private static final int PORT = 1234;
    private static final String HOST = "localhost";

    public static void main(String args[]) throws IOException {
        Socket dataSocket = new Socket(HOST, PORT);
        System.out.println("Connection to " + HOST + ":" + PORT + " established");

        SendThread send = new SendThread(dataSocket);
        Thread sendThread = new Thread(send);
        sendThread.start();

        ReceiveThread receive = new ReceiveThread(dataSocket);
        Thread receiveThread = new Thread(receive);
        receiveThread.start();
    }
}
