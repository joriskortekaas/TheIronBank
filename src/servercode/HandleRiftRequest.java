package servercode;

import java.io.*;
import java.net.Socket;
import java.util.Properties;

/**
 * Created by Joris on 18-6-2017.
 */
public class HandleRiftRequest extends Thread {
    private static int THREADID = 1;
    private int threadID = THREADID;

    private Socket rift;

    public HandleRiftRequest(Socket _rift) {
        rift = _rift;
        THREADID++;
    }

    @Override
    public void run() {
        System.out.println("RiftRequest: " + threadID + "\nExecuting now");
        try {
            InputStream inFromServer = rift.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);
            OutputStream outToServer = rift.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            Properties serverRequest = RiftProtocol.parseXMLToProperties(RiftProtocol.decrypt(in.readUTF(), ServerCode.getKey()));
            System.out.println("received message: " + RiftProtocol.parsePropertiesToXML(serverRequest));
            String outgoingMessage = RiftProtocol.parsePropertiesToXML(RiftProtocol.handleRiftRequest(serverRequest));
            System.out.println("send message: " + outgoingMessage);
            out.writeUTF(RiftProtocol.encrypt(outgoingMessage, ServerCode.getKey()));
        } catch(IOException e){
            e.printStackTrace();
        }

        try {
            rift.close();
        } catch(Exception e) {
            System.out.println("failed closing the connection");
        }
    }
}
