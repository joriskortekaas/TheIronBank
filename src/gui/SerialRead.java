package gui;
import org.apache.commons.lang.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.util.Enumeration;

/**
 * handles the Arduino serial data
 */
public class SerialRead implements SerialPortEventListener {
    private Frame frame;
    private SerialPort serialPort;
    private String ruban;
    private String cardNumber;
    /** The port we're normally going to use. */
    private static final String PORT_NAMES[] = {
            "/dev/cu.usbmodem1411", // Mac OS X
            "/dev/ttyACM0", // Raspberry Pi
            "/dev/ttyUSB0", // Linux
            "COM6", // Windows
    };
    private BufferedReader input;
    /** The output stream to the port */
    private OutputStream output;
    /** Milliseconds to block while waiting for port open */
    private static final int TIME_OUT = 2000;
    /** Default bits per second for COM port. */
    private static final int DATA_RATE = 9600;
    public static void main(String [] args){
        while(true){
            SerialRead main = new SerialRead();
            main.initialize(null);
        }
    }
    /**
     * initialises the class, creates a pointer to the frame.
     * @param x
     */
    public void initialize(Frame x) {
        ruban = "";
        cardNumber = "";
        frame = x;
        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        //First, Find an instance of serial port as set in PORT_NAMES.
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            for (String portName : PORT_NAMES) {
                if (currPortId.getName().equals(portName)) {
                    portId = currPortId;
                    break;
                }
            }
        }
        if (portId == null) {
            System.out.println("Could not find COM port.");
            return;
        }

        try {
            // open serial port, and use class name for the appName.
            serialPort = (SerialPort) portId.open(this.getClass().getName(),
                    TIME_OUT);

            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            // open the streams
            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            output = serialPort.getOutputStream();

            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    /**
     * This should be called when you stop using the port.
     * This will prevent port locking on platforms like Linux.
     */
    public synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }
    /**
     * Handle an event on the serial port. Read the data and print it.
     */
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                cardNumber = null;
                ruban = null;
                String inputLine = input.readLine();
                System.out.println(inputLine);
                switch (inputLine) {
                    case "0":
                        frame.numberPressed("0");
                        break;
                    case "1":
                        frame.numberPressed("1");
                        break;
                    case "2":
                        frame.numberPressed("2");
                        break;
                    case "3":
                        frame.numberPressed("3");
                        break;
                    case "4":
                        frame.numberPressed("4");
                        break;
                    case "5":
                        frame.numberPressed("5");
                        break;
                    case "6":
                        frame.numberPressed("6");
                        break;
                    case "7":
                        frame.numberPressed("7");
                        break;
                    case "8":
                        frame.numberPressed("8");
                        break;
                    case "9":
                        frame.numberPressed("9");
                        break;
                    case "*":
                        frame.nextPrevious(false);
                        break;
                    case "#":
                        frame.nextPrevious(true);
                        break;
                    case "A":
                        frame.lettersPressed("A");
                        break;
                    case "B":
                        frame.lettersPressed("B");
                        break;
                    case "C":
                        frame.lettersPressed("C");
                        break;
                    case "D":
                        frame.lettersPressed("D");
                        break;
                }
                if(inputLine.startsWith("*")){
                    ruban = StringUtils.substringBetween(inputLine, "*", "#");
                    ruban = ruban.substring(0,6);
                    System.out.println("ruban: " + ruban);
                }
                if(inputLine.contains("Card UID:")){
                    cardNumber = StringUtils.substringAfter(inputLine, "UID: ");
                    cardNumber = cardNumber.replaceAll("\\s+","");
                    System.out.println("cardID: " + cardNumber);
                }
                if(cardNumber != null && ruban != null) { //prevents the gui from blocking page 0
                    frame.cardScanned(cardNumber, ruban);
                    cardNumber = null;
                    ruban = null;
                }
                cardNumber = null;
                ruban = null;
            } catch (Exception e){e.printStackTrace();}
        }
        // Ignore all the other eventTypes, but you should consider the other ones.
    }
}

