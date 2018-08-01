package common;

public class ErrorPackage implements Package {
    private final static int PACKAGEID = -1;
    private final String errorMessage;

    public ErrorPackage(String _errorMessage) {
        errorMessage = _errorMessage;
    }

    @Override
    public int getID() {
        return PACKAGEID;
    }

    public String getErrorMessage(){
        return errorMessage;
    }

    @Override
    public String toString() {
        return "<PackageID=" + PACKAGEID + "<errorMessage=" + errorMessage + ">>";
    }

    public static ErrorPackage fromString(String objectString) {
        String _errorMessage = objectString.substring(objectString.indexOf("errorMessage=") + 13, objectString.indexOf(">", objectString.indexOf("errorMessage=") + 13));
        return new ErrorPackage(_errorMessage);
    }
}
