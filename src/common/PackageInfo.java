package common;

public class PackageInfo {
    /**
     * Convert package string representing the package to a package
     * @param packageString
     * @return
     */
    public static Package getPackageFromString(String packageString) {
        int packageID;
        try {
            packageID = Integer.valueOf(packageString.substring(11, packageString.indexOf("<", 11)));
        } catch(StringIndexOutOfBoundsException e) {
            packageID = Integer.valueOf(packageString.substring(11, packageString.indexOf(">", 11)));
        }
        switch(packageID){
            case -1:    return ErrorPackage.fromString(packageString);
            case 1:     return RequestLogin.fromString(packageString);
            case 2:     return AnswerLogin.fromString(packageString);
            case 3:     return RequestWithdrawal.fromString(packageString);
            case 4:     return AnswerWithdrawal.fromString(packageString);
            case 5:     return RequestLogout.fromString(packageString);
            case 6:     return AnswerLogout.fromString(packageString);
            case 7:     return AnswerBlocked.fromString(packageString);
            default:    return null;
        }
    }
}
