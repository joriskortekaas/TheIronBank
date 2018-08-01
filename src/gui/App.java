package gui;

import common.AnswerLogin;
import common.RequestLogin;
import connection.ConnectionManager;

import java.io.IOException;

/**
 * Contains main method.
 */
public class App {
    private static ConnectionManager connectionManager;

    public static void main(String [] args){
        //checks if the program was started with the right amount of arguments
        if(args.length != 2) {
            System.err.println("Expected arguments: <IP-adress> <Portnumber>");
            System.exit(1);
        }

        connectionManager = new ConnectionManager(args[0], Integer.parseInt(args[1]));

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                try {
                    connectionManager.closeStreams();
                    System.out.println("Succesfully closed all streams");
                } catch(IOException e) {
                    System.err.println("Couldnt close all the connection streams!");
                    e.printStackTrace();
                }
            }
        });

        System.out.println("Printing the print");

        Printer printer = new Printer();
        Dispenser dispenser = new Dispenser();
        Frame x = new Frame(printer, dispenser);
        SerialRead main = new SerialRead();
        SessionTimeThread session = new SessionTimeThread(x);
        session.start();
        main.initialize(x);
        System.out.println("Started");
    }

    public static ConnectionManager getConnectionManager() {
        return connectionManager;
    }

}