package common;

public class AnswerLogin implements Package {
    private final static int PACKAGEID = 2;
    private final boolean succesfullLogin;
    private final String accountName;
    private final int balance;

    /**
     * constructor
     * @param _succesfullLogin
     * @param _accountName
     * @param _balance
     */
    public AnswerLogin(boolean _succesfullLogin, String _accountName, int _balance) {
        succesfullLogin = _succesfullLogin;
        accountName = _accountName;
        balance = _balance;
    }
    @Override
    /**
     * getter to get ID
     */
    public int getID() {
        return PACKAGEID;
    }
    /**
     * getter to get isSuccesfulllogin
     * @return
     */
    public boolean isSuccesfullLogin() {
        return succesfullLogin;
    }
    /**
     * getter to get AccountName
     * @return
     */
    public String getAccountName() {
        return accountName;
    }
    /**
     * getter to get balance
     * @return
     */
    public int getBalance() {
        return balance;
    }
    @Override
    /**
     * converts package to String
     */
    public String toString() {
        return "<PackageID=" + PACKAGEID + "<succesfullLogin=" + succesfullLogin + ",accountName=" + accountName + ",balance=" + balance + ">>";
    }
    /**
     * converts an AnswerLogin object to String object
     * @param objectString
     * @return
     */
    public static AnswerLogin fromString(String objectString) {
        boolean _succesfullLogin = Boolean.valueOf(objectString.substring(objectString.indexOf("succesfullLogin=") + 16, objectString.indexOf(",", objectString.indexOf("succesfullLogin=") + 16)));
        String _accountName = null;
        int _balance = 0;
        if (_succesfullLogin) {
            _accountName = objectString.substring(objectString.indexOf("accountName=") + 12, objectString.indexOf(",", objectString.indexOf("accountName=") + 12));
            _balance = Integer.valueOf(objectString.substring(objectString.indexOf("balance=") + 8, objectString.indexOf(">", objectString.indexOf("balance=") + 8)));
        }
        return new AnswerLogin(_succesfullLogin, _accountName, _balance);
    }
}