package common;

public class AnswerBlocked implements Package {
    private final static int PACKAGEID = 7;

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
     * converts AnswerBlocked to String object
     * @param objectString
     * @return
     */
    public static AnswerBlocked fromString(String objectString) {
        return new AnswerBlocked();
    }
}