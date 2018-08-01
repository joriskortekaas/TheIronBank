package common;

public class RequestLogin implements Package{
    private final static int PACKAGEID = 1;
    private final String passNumber;
    private final int password;
    private final String ruban;

    /**
     * constructor
     * @param _passNumber
     * @param _password
     */
    public RequestLogin(String _passNumber, int _password, String _ruban) { //TODO @Tommie zorg dat de client bij inloggen ook de ruban meegeeft
        passNumber = _passNumber;
        password = _password;
        ruban = _ruban;
    }

    @Override
    /**
     * getter to get packageID
     */
    public int getID() {
        return PACKAGEID;
    }

    /**
     * getter to get passNumber
     * @return passNumber
     */
    public String getPassNumber() {
        return passNumber;
    }

    /**
     * getter to get password
     * @return password
     */
    public int getPassword() {
        return password;
    }

    public String getRuban() {
        return ruban;
    }

    @Override
    /**
     * converts package to string
     */
    public String toString() {
        return "<PackageID=" + PACKAGEID + "<passNumber=" + passNumber + ",password=" + password + ",ruban=" + ruban + ">>";
    }
    /**
     * returns requestlogin object from string object
     * @param objectString
     * @return
     */
    public static RequestLogin fromString(String objectString) {
        String _passNumber = objectString.substring(objectString.indexOf("passNumber=") + 11, objectString.indexOf(",", objectString.indexOf("passNumber=") + 11));
        int _password = Integer.valueOf(objectString.substring(objectString.indexOf("password=") + 9, objectString.indexOf(",", objectString.indexOf("password=") + 9)));
        String _ruban = objectString.substring(objectString.indexOf("ruban=") + 6, objectString.indexOf(">", objectString.indexOf("ruban=") + 6));

        return new RequestLogin(_passNumber, _password, _ruban);
    }
}