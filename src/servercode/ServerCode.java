package servercode;

import com.sun.net.ssl.internal.ssl.Provider;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.security.Security;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Joris on 11-3-2017.
 */
public class ServerCode {
    private static DatabaseManager database = new DatabaseManager();
    private static String key = "770A8A65DA156D24";

    public static void main(String[] args) throws IOException {
        //checks if the program was started with the right amount of arguments
        if(args.length != 1) {
            System.err.println("Expected argument: <port-number>");
            System.exit(1);
        }

        //Terminates the database connection and the connection with all the clients
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    database.getConnection().close();
                } catch(SQLException e) {
                    System.err.println("Couldn't close the connection to the database on system exit");
                    e.printStackTrace();
                }
            }
        });

        Security.addProvider(new Provider());
        System.setProperty("javax.net.ssl.keyStore", "/home/ubuntu-0935462/Project/servercode/resources/keystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "changeit");

        int portNumber = Integer.parseInt(args[0]);
        ServerSocketFactory serverSocketFactory = ServerSocketFactory.getDefault();
        ServerSocket serverSocket = serverSocketFactory.createServerSocket(portNumber);

        // keeps looping and accepting clients and putting them on a different thread.
        System.out.println("started accepting connections on: " + serverSocket.getLocalSocketAddress().toString());
        while(true) {
            Socket client =  serverSocket.accept();

            if(client.getInetAddress().toString().substring(1).equals("145.24.222.69")) {
                System.out.println("Rift Connection Opened");
                HandleRiftRequest thread = new HandleRiftRequest(client);

                thread.run();
            } else {
                System.out.println("Client Connection Opened");
                SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
                SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(client, null, client.getPort(), false);
                sslSocket.setUseClientMode(false);

                ClientThread thread = new ClientThread(sslSocket);

                thread.run();
            }
        }
    }

    public static DatabaseManager getDatabaseManager() {
        return database;
    }

    public static String getKey() {
        return key;
    }
}