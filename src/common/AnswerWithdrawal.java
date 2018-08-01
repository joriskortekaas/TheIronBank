package common;

public class AnswerWithdrawal implements Package {
    private final static int PACKAGEID = 4;
    private final boolean succesfullWithdrawal;

    /**
     * constructor
     * @param _succesfullWithdrawal
     */
    public AnswerWithdrawal(boolean _succesfullWithdrawal) {
        succesfullWithdrawal = _succesfullWithdrawal;
    }

    @Override
    /**
     * getter to get ID
     */
    public int getID() {
        return PACKAGEID;
    }

    /**
     * getter to get isSuccesfullWithdrawal
     * @return
     */
    public boolean isSuccesfullWithdrawal() {
        return succesfullWithdrawal;
    }

    @Override
    /**
     * converst package to String
     */
    public String toString() {
        return "<PackageID=" + PACKAGEID + "<succesfullWithdrawal=" + succesfullWithdrawal + ">";
    }

    /**
     * converts AnswerWithdrawal object to String object
     * @param objectString
     * @return
     */
    public static AnswerWithdrawal fromString(String objectString) {
        boolean _succesfullWithdrawal = Boolean.valueOf(objectString.substring(objectString.indexOf("succesfullWithdrawal=") + 21, objectString.indexOf(">", objectString.indexOf("succesfullWithdrawal=") + 21)));

        return new AnswerWithdrawal(_succesfullWithdrawal);
    }
}
