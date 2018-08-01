package servercode;

import java.net.*;
import java.io.*;


public class RiftConnector {
    public RiftConnector(String s, int i){
        serverName = s;
        port = i;
    }

    Socket client;
    DataOutputStream out;
    DataInputStream in;
    String serverName = "";
    int port;




    private void establishConnection(){
        try {
            client = new Socket(serverName, port);
            InputStream inFromServer = client.getInputStream();
            in = new DataInputStream(inFromServer);
            OutputStream outToServer = client.getOutputStream();
            out = new DataOutputStream(outToServer);

        }catch(IOException e) {

            System.out.println("no connection established");
        }
    }
    private void endConnection(){
        try {
            client.close();
        } catch(Exception e) {
            System.out.println("failed closing the connection");
        }
        client = null;
        in = null;
        out = null;
    }
    public String sendToRift(String s){
        establishConnection();
        try {
            out.writeUTF(s);
        } catch (IOException e)
        {
            System.out.println("something went wrong while sending to the server");
        }
        return receiveFromRift();
    }
    private String receiveFromRift(){
        try {

            return in.readUTF();

        } catch (IOException e)
        {
            System.out.println("something went wrong while receiving from the server");
        }
        endConnection();
        return null;
    }
}
