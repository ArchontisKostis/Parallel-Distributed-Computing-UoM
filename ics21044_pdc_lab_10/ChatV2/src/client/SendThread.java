package client;

import protocols.ChatClientProtocol;
import utils.CommandHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SendThread implements Runnable {
    private Socket dataSocket;
    private OutputStream os;
    private PrintWriter out;
    private ChatClientProtocol app;

    public SendThread(Socket soc) throws IOException {
        dataSocket = soc;
        os = dataSocket.getOutputStream();
        out = new PrintWriter(os, true);
        app = new ChatClientProtocol();
    }

    public void run() {
        try {
            askForUsername();

            String outmsg;
            boolean run = true;

            while (run) {
                outmsg = app.sendMessage();
                if (outmsg.equalsIgnoreCase(CommandHandler.EXIT)) {
                    out.println(outmsg);
                    run = false;
                }
                out.println(outmsg);
            }
            dataSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void askForUsername() throws IOException {
        System.out.println("Enter your username: ");
        String username = app.sendMessage();
        out.println(username); // Send username to the server
    }
}
