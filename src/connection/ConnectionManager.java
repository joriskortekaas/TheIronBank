package connection;

import com.sun.net.ssl.internal.ssl.Provider;
import common.*;
import common.Package;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.Security;

import static java.lang.Thread.sleep;

/**
 * filler for the connection file
 * Created by Joris on 21-2-2017.
 */
public class ConnectionManager {
    private SSLSocket connection;
    private BufferedReader in;
    private PrintWriter out;

    public ConnectionManager(String ip, int port) {

        Security.addProvider(new Provider());
        System.setProperty("javax.net.ssl.trustStore", "keystore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

        System.out.println("Attempting to conncect to the server...");

        try {
            SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            System.out.println("making socket");
            connection = (SSLSocket) socketFactory.createSocket(ip, port);

            System.out.println("shaking hands");
            connection.startHandshake();
            System.out.println("handshake done!");

            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            out = new PrintWriter(connection.getOutputStream(), true);
        } catch(UnknownHostException e) {
            System.err.println("Unknown host for " + ip);
            System.exit(1);
        } catch(IOException e) {
            System.err.println("Couldnt get the I/O for the connection with '" + ip + ":" + port + "'");
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("Succesfully connected to the server");
    }

    public SSLSocket getConnection(){
        return connection;
    }

    private void writeMessage(String message) {
        out.println(message);
    }

    private String receiveMessage() throws IOException{
        String message;
        while(true){
            message = in.readLine();
            if(message != null) {
                System.out.print("Received message: " + message);
                return message;
            }
        }
    }

    public Package sendRequest(Package requestPackage) {
        writeMessage(requestPackage.toString());
        try {
            return PackageInfo.getPackageFromString(receiveMessage());
        } catch(IOException e) {
            e.printStackTrace();
            return new ErrorPackage("IOException");
        }
    }

    public void closeStreams() throws IOException{
        writeMessage("CloseStream");
        in.close();
        out.close();
    }
}