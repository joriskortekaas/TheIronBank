package gui;

import common.*;
import common.Package;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

/**
 * This class creates the Frame for the ATM.
 */
public class Frame extends JFrame {
    /**
     * Fields
     */
    private boolean printReceipt;
    private boolean cardIsScannedOnce;
    private boolean languageSelected;
    private DefaultListModel listModel;
    private JList moneyList;

    private String pinCode;
    private String name;
    private String UID;
    private String ruban;

    private int moneyPinned;
    private int balance;
    private int previouspage;
    private int selecteditem;
    private int page;
    private int tenSelected;
    private int twentySelected;
    private int fiftySelected;

    private JLabel instructions;
    private JLabel brand;
    private JLabel text;
    private JLabel pin;
    private JLabel previous;
    private JLabel next;
    private JPanel inputPanel;
    private JPanel instructionPanel;
    private JPanel centerPanel;
    private JPanel bottom;
    private JPanel nextPanel;
    private JPanel previousPanel;
    private JPanel top;
    private Printer printer;
    private Dispenser dispenser;
    private Translations translations;

    /**
     * Constructor
     * @param _printer
     */
    public Frame(Printer _printer, Dispenser _dispenser) {
        translations = new Translations();
        dispenser = _dispenser;
        dispenser.restockInv();
        printer = _printer;
        tenSelected = 0;
        twentySelected = 0;
        fiftySelected = 0;
        selecteditem = 0;
        listModel = new DefaultListModel();
        setLayout(new java.awt.BorderLayout());
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("The pink banker");
        GridBagConstraints c = new GridBagConstraints();
        setBackground(new Color(252, 249, 249));
        moneyList = new JList(listModel);
        pinCode = "";
        languageSelected = true;
        text = new JLabel(translations.translate(0, languageSelected));
        pin = new JLabel();
        instructions = new JLabel("<html><p style='font-size: 15px; color: #e6005c'>A: English<BR> B: Nederlands<BR> C: Afbreken<</p><</html>");
        previous = new JLabel("");
        next = new JLabel("");
        brand = new JLabel("<html><p style='font-size: 40px; color: #e6005c'>Pink Banker</p><</html>");
        pin.setFont(new Font("Sans Serif", Font.PLAIN, 60));

        moneyList.setFont(new Font("Sans Serif", Font.PLAIN, 20));
        moneyList.setBackground(new Color(252, 249, 249));

        inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.PAGE_AXIS));
        inputPanel.setBackground(new Color(252, 249, 249));
        inputPanel.add(text);
        inputPanel.add(pin);

        centerPanel = new JPanel();
        centerPanel.setBackground(new Color(252, 249, 249));
        centerPanel.setLayout(new GridBagLayout());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 40;
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        centerPanel.add(inputPanel);
        centerPanel.add(moneyList, c);
        bottom = new JPanel();
        bottom.setBackground(new Color(252, 249, 249));

        instructionPanel = new JPanel();
        instructionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        instructionPanel.add(instructions);

        previousPanel = new JPanel();
        previousPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        previousPanel.add(previous);
        previousPanel.setBackground(new Color(252, 249, 249));

        nextPanel = new JPanel();
        nextPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        nextPanel.add(next);
        nextPanel.setBackground(new Color(252, 249, 249));

        top = new JPanel();
        top.setBackground(new Color(58, 58, 58));
        top.setLayout(new FlowLayout(FlowLayout.CENTER));
        top.add(brand);

        add(previousPanel, BorderLayout.WEST);
        add(nextPanel, BorderLayout.EAST);
        add(instructionPanel, BorderLayout.SOUTH);
        add(centerPanel);
        add(top, BorderLayout.NORTH);

        lettersPressed("B");
        lettersPressed("C");
        setVisible(true);
    }
    /**
     * This method handles input when a numeric key is pressed on the keypad
     * @param number
     */
    public void numberPressed(String number) { //if a number is pressed on the keypad.
        if ((page == 1 || page == 12) && pinCode.length() < 4) { //enables user to enter pin
            pin.setText(pin.getText() + "*");
            pinCode = pinCode + number;
        }
        if (page == 2 && number == "1") { //goes to show balance panel
            previouspage = 5;
            page = 5;
            updateText();
            text.setText(translations.translate(5, languageSelected));
        }
        if (page == 2 && number == "2") {//goes to pin money panel
            previouspage = 4;
            page = 4;
            updateText();
        }
        if (page == 4) { //show choose money options
            String temp;
            if (number == "4" && selecteditem < 2) {
                selecteditem++;
                moneyList.setSelectedIndex(selecteditem);
            }
            if (number == "1" && selecteditem > 0) {
                selecteditem--;
                moneyList.setSelectedIndex(selecteditem);
            }
            if (number == "3") {
                temp = moneyList.getSelectedValue().toString();
                if(temp == "10"){
                  	tenSelected++;
                  	dispenser.invTenminus();
                }
                if(temp == "20"){
                	twentySelected++;
                	dispenser.invTwentyminus();
                }
                if(temp == "50"){
                	fiftySelected++;
                	dispenser.invFiftyminus();
                }
                moneyPinned += Integer.parseInt(temp);
                System.out.println(moneyPinned);
                showMoneyOptions();
            }
            if (page == 2) {
                moneyPinned = 0; //reset money
            }
        }
        if (page == 3) { //receipt page.
            switch (number) {
                case "1":
                    printReceipt = true;
                    text.setText(translations.translate(9, languageSelected));
                    break;
                case "2":
                    printReceipt = false;
                    text.setText(translations.translate(10, languageSelected));
                    break;
            }
        }
    }
    /**
     * This method updates the text displayed on screen
     */
    public void updateText() { //To change text in panels
        if (page != 1 && page != 12) {
            pin.setText("");
            pinCode = "";
        }
        if (page != 4 || page != 5 || page != 2) {
            previous.setText("");
            next.setText("");
        }
        if (page == 8) {
            try {
                text.setText(translations.translate(page, languageSelected));
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }
        }
        if (page == 2) {
            text.setText(translations.welcomeScreen(name, languageSelected));
            return;
        }
        changeInstructions();
        showMoneyOptions();
        text.setText(translations.translate(page, languageSelected));
        instructions.setText(translations.translate(6, languageSelected));
    }
    /**
     * This method handles the next and previous actions to go to different panels
     * @param previousNext false is previous, next is true
     */
    public void nextPrevious(boolean previousNext) { //to go to previous or next page.
        if (!previousNext && page == 3) { //receipt page
            page = previouspage;
            updateText();
            return;
        } else if (previousNext && (page == 1 || page == 12)) { //enter pin
            Package packageReceived = App.getConnectionManager().sendRequest(new RequestLogin(UID, Integer.valueOf(pinCode), ruban));
            if(packageReceived instanceof AnswerLogin) {
                AnswerLogin answer = (AnswerLogin) packageReceived;
                if(answer.isSuccesfullLogin()) {
                    System.out.println("Succesfully logged in");
                    name = answer.getAccountName();
                    balance = answer.getBalance();
                    translations.setBalance(balance);
                    page = 2;
                    updateText();
                } else {
                    pinCode = "";
                    pin.setText("");
                    page = 12;
                    updateText();
                }
            } else if(packageReceived instanceof AnswerBlocked) {
                pinCode = "";
                page = 13;
                updateText();
                //thread to keep the gui on the "Account blocked" for 2 seconds
                try {
                    Thread.sleep(2000);
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                stopSession();
            } else {
                System.err.println("Error message: " + packageReceived.toString());
            }
        }
        else if (previousNext && page == 3) { //receipt page
            App.getConnectionManager().sendRequest(new RequestWithdrawal(moneyPinned));
            dispenser.dispenseMoney(tenSelected,twentySelected,fiftySelected);
            System.out.println("!!!!" + tenSelected + "," + twentySelected + "," + fiftySelected);
            if (printReceipt) {
                printer.parseInfoToPrint(moneyPinned, balance, name, LocalDate.now().toString(), languageSelected);
                printer.print(true);
            }
            else{}
            page = 8;
            updateText();
            //thread to keep the gui on the "thank you panel" for 2 seconds
            try {
                Thread.sleep(2000);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            stopSession();
            return;
        }
        else if(previousNext && (page == 5 || (page == 4 && moneyPinned <= balance))){ //if client presses next.
            page = 3;
            updateText();
        }
        else if(previousNext && (page == 4 && moneyPinned > balance)){ //if client presses next and moneypinned is too high.
            page = 11;
            updateText();
        }
        else if (previousNext && page < 2 && page > 0 && (page != 4 && page != 5 && page != 3 && page != 11)) {
            page++;
            System.out.println("page:" + page);
            updateText();
        }
        else if (!previousNext && page > 2 && (page != 4 && page != 5 && page != 3  && page != 11)) {
            page--;
            System.out.println("page:" + page);
            updateText();
        }
        else if (!previousNext && (page == 4 || page == 5)) { //if you want to go back from balance or pin money screen
            page = 2;
            if (languageSelected) {
                previous.setText("");
                next.setText("");
            }
            else if (!languageSelected) {
                previous.setText("");
                next.setText("");
            }
            showMoneyOptions();
            updateText();
        }
        else if(!previousNext && page == 11){ //not enough balance screen
            page = 4;
            updateText();
        }
    }
    /**
     * This method displays the money options which are displayed in panel 5.
     */
    public void showMoneyOptions() { //display money options in panel 5
        if (page == 4) {
            listModel.removeAllElements();
            if(dispenser.getInvTen() > 0){
                listModel.addElement("10");
            }
            if(dispenser.getInvTwenty() > 0){
                listModel.addElement("20");
            }
            if(dispenser.getInvFifty() > 0){
                listModel.addElement("50");
            }
            listModel.addElement(translations.moneyScreen(Integer.toString(moneyPinned), languageSelected));
            moneyList.setSelectedIndex(selecteditem);
        } else {
            listModel.removeAllElements();
        }
    }
    /**
     * This method parses the UID and banknumber from the SerialTest to the frame
     * @param _UID pasnumber
     * @param _ruban ruban
     */
    public void cardScanned(String _UID, String _ruban){
        if(!cardIsScannedOnce) {
            if(_UID.equals("moet hier ingevuld worden")){
                dispenser.restockInv();
                stopSession();
            }
            ruban = _ruban;
            UID = _UID;
            System.out.println("UID should be: " + UID + " = " + _UID);
            cardIsScannedOnce = true;
            page = 1;
            updateText();
        }
    }
    /**
     * This method handles input when an alphabetic key is pressed on the keypad
     * @param i input
     */
    public void lettersPressed(String i) { //if a letter is pressed on the keypad
        switch (i) {
            case "B"://translates to Dutch
                languageSelected = true;
                updateText();
                break;
            case "A": //translate to English.
                languageSelected = false;
                updateText();
                break;
            case "C": //cancels transaction
                stopSession();
                break;
            case "D":
                if (page == 1 || page == 12) {
                    pinCode = "";
                    pin.setText("");
                } else if (page == 4) {
                	dispenser.reset(tenSelected,twentySelected,fiftySelected);
                    tenSelected = 0;
                    twentySelected = 0;
                    fiftySelected = 0;
                    moneyPinned = 0;
                    selecteditem = 0;
                    moneyList.setSelectedIndex(selecteditem);
                    showMoneyOptions();
                }
        }
    }
    /**
     * This method stops the session and deletes all the session variables.
     */
    public void stopSession() {
        page = 0;
        moneyPinned = 0;
        dispenser.reset(tenSelected,twentySelected,fiftySelected);
        tenSelected =0;
        twentySelected = 0;
        fiftySelected = 0;
        cardIsScannedOnce = false;
        printReceipt = false;
        App.getConnectionManager().sendRequest(new RequestLogout());
        updateText();
    }
    /**
     * Getter to check if cardIsScannedOnce is true or false.
     * @return cardIsScannedOnce
     */
    public boolean isCardIsScannedOnce() {
        return cardIsScannedOnce;
    }
    /**
     * returns what page the gui is on
     * @return page
     */
    public int getPage(){return page;}
    /**
     * This method changes the instructions for the certain panels.
     */
    public void changeInstructions() {
        switch (page) {
            case 1:
            case 12:
                if (languageSelected) {
                    previous.setText("");
                    next.setText("<html><p style='font-size: 20px;color: #e6005c'>Bevestig pin #</p><</html>");
                }
                if (!languageSelected) {
                    previous.setText("");
                    next.setText("<html><p style='font-size: 20px;color: #e6005c'>Confirm pin #</p><</html>");
                }
                break;
            case 3:
                if (languageSelected) {
                    previous.setText("<html><p style='font-size: 20px;color: #e6005c'>Vorig *</p><</html>");
                    next.setText("<html><p style='font-size: 20px;color: #e6005c'>Eindig sessie #</p><</html>");
                }
                if (!languageSelected) {
                    previous.setText("<html><p style='font-size: 20px;color: #e6005c'>Previous *</p><</html>");
                    next.setText("<html><p style='font-size: 20px;color: #e6005c'>End session #</p><</html>");
                }
                break;
            case 4:
                if (languageSelected) {
                    previous.setText("<html><p style='font-size: 20px;color: #e6005c'>Hoofdmenu *</p><</html>");
                    next.setText("<html><p style='font-size: 20px;color: #e6005c'>Akkoord # </p><</html>");
                }
                if (!languageSelected) {
                    previous.setText("<html><p style='font-size: 20px;color: #e6005c'>Main menu *</p><</html>");
                    next.setText("<html><p style='font-size: 20px;color: #e6005c'>Accept #</p><</html>");
                }
                break;
            case 5:
                if (languageSelected) {
                    previous.setText("<html><p style='font-size: 20px;color: #e6005c'>Hoofdmenu *</p><</html>");
                    next.setText("<html><p style='font-size: 20px;color: #e6005c'>Print bon # </p><</html>");
                }
                if (!languageSelected) {
                    previous.setText("<html><p style='font-size: 20px;color: #e6005c'>Main menu *</p><</html>");
                    next.setText("<html><p style='font-size: 20px;color: #e6005c'>Print receipt #</p><</html>");
                }
                break;
            case 11:
                if (languageSelected) {
                    previous.setText("<html><p style='font-size: 20px;color: #e6005c'>Vorig *</p><</html>");
                    next.setText("");
                }
                if (!languageSelected) {
                    previous.setText("<html><p style='font-size: 20px;color: #e6005c'>Previous *</p><</html>");
                    next.setText("");
                }
                break;
        }
    }
}