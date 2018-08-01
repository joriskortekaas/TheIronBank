package common;

public class RequestWithdrawal implements Package {
    private final static int PACKAGEID = 3;
    private final int amount;

    /**
     * constructor
     * @param _amount
     */
    public RequestWithdrawal(int _amount) {
        amount = _amount;
    }

    @Override
    /**
     * getter to get ID
     */
    public int getID() {
        return PACKAGEID;
    }

    /**
     * getter to get amount
     * @return
     */
    public int getAmount() {
        return amount;
    }

    @Override
    /**
     * converst package to String
     */
    public String toString() {
        return "<PackageID=" + PACKAGEID + "<amount=" + amount + ">";
    }

    /**
     * converts requestwithDrawal object to a string object
     * @param objectString
     * @return
     */
    public static RequestWithdrawal fromString(String objectString) {
        int _amount = Integer.valueOf(objectString.substring(objectString.indexOf("amount=") + 7, objectString.indexOf(">", objectString.indexOf("amount=") + 7)));

        return new RequestWithdrawal(_amount);
    }
}
