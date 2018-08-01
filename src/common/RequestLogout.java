package common;

public class RequestLogout implements Package{
    private final static int PACKAGEID = 5;

    @Override
    /**
     * getter to get ID
     */
    public int getID() {
        return PACKAGEID;
    }

    @Override
    /**
     * converts package object to string
     */
    public String toString() {
        return "<PackageID=" + PACKAGEID + ">";
    }
    /**
     * converts Requestlogout object to String object
     * @param objectString
     * @return
     */
    public static RequestLogout fromString(String objectString) {
        return new RequestLogout();
    }
}
