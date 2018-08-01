package servercode;

import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Properties;

/**
 * Created by Joris on 12-3-2017.
 */
public class ClientThread extends Thread {
    private static int THREADID = 1;
    private int threadID = THREADID;

    private SSLSocket client;

    private boolean loggedIn = false;

    private String bankNumber = "";
    private String ruban = "";
    private int pincode = 0;

    private RiftConnector rift = new RiftConnector("145.24.222.69", 8501);

    public ClientThread(SSLSocket _client) {
        client = _client;
        THREADID++;
    }

    @Override
    public void run() {
        System.out.println("Thread: " + threadID + "\nConnected to: " + client.getInetAddress().toString());
        try(
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))
        ) {

            String inputLine, outputLine;
           while(true) {
                inputLine = in.readLine();
                if(inputLine != null) {
                    if(inputLine.equals("CloseStream")) {
                        break;
                    }
                    System.out.println("received message: " + inputLine);
                    outputLine = PinkBankerProtocol.handleMessage(this, inputLine);
                    System.out.println(outputLine);
                    out.println(outputLine);
                }
            }

        } catch(IOException e) {
            e.printStackTrace();
        }

        try {
            client.close();
            System.out.println("Closed connection on thread: " + threadID);
        } catch(IOException e) {
            System.err.print("Couldn't close the client connection in thread " + threadID);
        }
    }

    public void runClientServer() {


    }

    public void setLoggedIn(boolean value) {
        loggedIn = value;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setBankNumber(String number) {
        bankNumber = number;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public SSLSocket getSocket(){
        return client;
    }

    public void setRuban(String _ruban) {
        ruban = _ruban;
    }

    public  String getRuban() {
        return ruban;
    }

    public void setPincode(int _pincode) {
        pincode = _pincode;
    }

    public int getPincode() {
        return pincode;
    }

    public RiftConnector getRift() {
        return rift;
    }
}