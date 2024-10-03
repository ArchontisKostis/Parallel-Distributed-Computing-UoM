import java.net.*;
import java.io.*;

public class EchoClientTCP {
    private static final String HOST = "localhost";
    private static final int PORT = 1234;

    public static void main(String[] args) throws IOException {
        Socket dataSocket = new Socket(HOST, PORT);
        InputStream is = dataSocket.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        OutputStream os = dataSocket.getOutputStream();
        PrintWriter out = new PrintWriter(os, true);

        System.out.println("Connection to " + HOST + " established");

        ClientProtocol app = new ClientProtocol();
        String outmsg;

        while (true) {
            outmsg = app.prepareRequest();
            if (outmsg.equalsIgnoreCase("EXIT")) {
                break;
            }
            out.println(outmsg);
            String inmsg = in.readLine();
            app.processReply(inmsg);
        }

        out.println("EXIT");
        dataSocket.close();
        System.out.println("Data Socket closed");
    }
}
