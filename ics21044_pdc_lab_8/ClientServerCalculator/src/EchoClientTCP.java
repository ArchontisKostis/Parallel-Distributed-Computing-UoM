import java.net.*;
import java.io.*;

public class EchoClientTCP {
    private static final String HOST = "localhost";
    private static final int PORT = 1234;
    private static final String EXIT = "!";

    public static void main(String[] args) throws IOException {
        Socket dataSocket = new Socket(HOST, PORT);

        InputStream is = dataSocket.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        OutputStream os = dataSocket.getOutputStream();
        PrintWriter out = new PrintWriter(os, true);

        System.out.println("Connection to " + HOST + " established");

        String inmsg, outmsg;
        ClientProtocolCalculator app = new ClientProtocolCalculator();

        outmsg = app.prepareRequest();
        while (!outmsg.equals(EXIT)) {
            out.println(outmsg);
            System.out.println("Sent to server: " + outmsg); // Debugging output
            inmsg = in.readLine();
            System.out.println("Received from server: " + inmsg); // Debugging output
            app.processReply(inmsg);
            outmsg = app.prepareRequest();
        }

        dataSocket.close();
        System.out.println("Data Socket closed");
    }
}
