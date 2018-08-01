package common;

public class AnswerLogout implements Package {
    private final static int PACKAGEID = 6;

    @Override
    /**
     * getter to get ID
     */
    public int getID() {
        return PACKAGEID;
    }

    @Override
    /**
     * converts package to String
     */
    public String toString() {
        return "<PackageID=" + PACKAGEID + ">";
    }

    /**
     * converts answerLogout object to String object
     * @param objectString
     * @return
     */
    public static AnswerLogout fromString(String objectString) {
        return new AnswerLogout();
    }
}
