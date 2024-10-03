import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServerTCP {
    private static final int PORT = 1234;

    public static void main(String[] args) throws IOException {
        ServerSocket connectionSocket = new ServerSocket(PORT);
        System.out.println("Server is listening to port: " + PORT);

        Socket dataSocket = connectionSocket.accept();
        System.out.println("Received request from " + dataSocket.getInetAddress());

        InputStream is = dataSocket.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        OutputStream os = dataSocket.getOutputStream();
        PrintWriter out = new PrintWriter(os, true);

        ServerProtocol app = new ServerProtocol();
        String inmsg, outmsg;

        while ((inmsg = in.readLine()) != null) {
            outmsg = app.processRequest(inmsg);
            out.println(outmsg);
            if (inmsg.equalsIgnoreCase("EXIT")) {
                break;
            }
        }

        dataSocket.close();
        System.out.println("Data socket closed");
    }
}
