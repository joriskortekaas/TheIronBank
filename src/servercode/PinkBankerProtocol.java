package servercode;

import common.*;
import common.Package;

import java.util.Properties;

public class PinkBankerProtocol {

    public static String handleMessage(ClientThread caller, String message) {
        Package packageToHandle;
        try {
            packageToHandle = readMessage(message);
        } catch(BadFormatException e) {
            return (new ErrorPackage("FormatError")).toString();
        }
        if((packageToHandle instanceof RequestLogin &&
                ((RequestLogin) packageToHandle).getRuban().equals("074201"))
                || caller.getRuban().equals("074201")) {
            return handleMessageClientServer(caller, packageToHandle);
        } else {
            return handleMessageRiftServer(caller, packageToHandle);
        }
    }

    public static String handleMessageRiftServer(ClientThread caller, Package packageToHandle) {
        Properties xmlProperties = new Properties();
        xmlProperties.setProperty("afzender", "074201");
        if(packageToHandle instanceof RequestLogin) {
            return handleLoginRift(caller, (RequestLogin) packageToHandle, xmlProperties);
        } else if(caller.isLoggedIn()) {
            if(packageToHandle instanceof RequestWithdrawal) {
                return handleWithdrawalRift(caller, (RequestWithdrawal) packageToHandle, xmlProperties);
            } else if(packageToHandle instanceof RequestLogout) {
                return handleLogoutPackage(caller, (RequestLogout) packageToHandle);
            } else {
                return (new ErrorPackage("UnImplementedPackage")).toString();
            }
        } else {
            return (new ErrorPackage("NotLoggedIn")).toString();
        }
    }

    public static String handleMessageClientServer(ClientThread caller, Package packageToHandle) {
        if(packageToHandle instanceof RequestLogin) {
            return handleLoginPackage(caller, (RequestLogin) packageToHandle);
        } else if(caller.isLoggedIn()) {
            if(packageToHandle instanceof RequestWithdrawal) {
                return handleWithdrawalPackage(caller, (RequestWithdrawal) packageToHandle);
            } else if(packageToHandle instanceof RequestLogout) {
                return handleLogoutPackage(caller, (RequestLogout) packageToHandle);
            } else {
                return (new ErrorPackage("UnImplementedPackage")).toString();
            }
        } else {
            return (new ErrorPackage("NotLoggedIn")).toString();
        }
    }

    private static Package readMessage(String message) throws BadFormatException {
        Package messagePackage = PackageInfo.getPackageFromString(message);
        if(messagePackage == null) {
            throw new BadFormatException();
        } else {
            return messagePackage;
        }
    }

    private static String handleWithdrawalRift(ClientThread caller, RequestWithdrawal withdrawalPackage, Properties xmlProperties) {
        xmlProperties.setProperty("actionType", "withdraw");
        xmlProperties.setProperty("pasnumber", caller.getBankNumber());
        xmlProperties.setProperty("banknumber", caller.getRuban());
        xmlProperties.setProperty("pincode", "" + caller.getPincode());
        xmlProperties.setProperty("amount", "" + withdrawalPackage.getAmount());
        xmlProperties.setProperty("errorNote", "");

        String xmlReply = caller.getRift().sendToRift(RiftProtocol.encrypt(RiftProtocol.parsePropertiesToXML(xmlProperties), ServerCode.getKey()));
        System.out.println(RiftProtocol.decrypt(xmlReply, ServerCode.getKey()));
        return (new AnswerWithdrawal(Boolean.valueOf(RiftProtocol.parseXMLToProperties(RiftProtocol.decrypt(xmlReply, ServerCode.getKey())).getProperty("succes")))).toString();
    }

    private static String handleWithdrawalPackage(ClientThread caller, RequestWithdrawal withdrawalPackage) {
        ServerCode.getDatabaseManager().addLogEntry(caller.getBankNumber(), withdrawalPackage.getAmount());
        return (new AnswerWithdrawal(ServerCode.getDatabaseManager().withdrawMoney(caller.getBankNumber(), withdrawalPackage.getAmount()))).toString();
    }

    private static String handleLoginRift(ClientThread caller, RequestLogin loginPackage, Properties xmlProperties) {
        xmlProperties.setProperty("actionType", "login");
        xmlProperties.setProperty("pasnumber", loginPackage.getPassNumber());
        xmlProperties.setProperty("banknumber", loginPackage.getRuban());
        xmlProperties.setProperty("pincode", "" + loginPackage.getPassword());
        xmlProperties.setProperty("errorNote", "");

        String xmlReply = caller.getRift().sendToRift(RiftProtocol.encrypt(RiftProtocol.parsePropertiesToXML(xmlProperties), ServerCode.getKey()));
        System.out.println(RiftProtocol.decrypt(xmlReply, ServerCode.getKey()));
        Properties answerProperties = RiftProtocol.parseXMLToProperties(RiftProtocol.decrypt(xmlReply, ServerCode.getKey()));

        if(Boolean.valueOf(answerProperties.getProperty("succes"))) {
            caller.setPincode(Integer.valueOf(xmlProperties.getProperty("pincode")));
            caller.setRuban(answerProperties.getProperty("banknumber"));
            caller.setLoggedIn(true);
            caller.setBankNumber(loginPackage.getPassNumber());

            xmlProperties.setProperty("actionType", "getSaldo");
            String xmlSaldoReply = caller.getRift().sendToRift(RiftProtocol.encrypt(RiftProtocol.parsePropertiesToXML((xmlProperties)), ServerCode.getKey()));
            System.out.println(RiftProtocol.decrypt(xmlSaldoReply, ServerCode.getKey()));
            Properties answerWithdrawProperties = RiftProtocol.parseXMLToProperties(RiftProtocol.decrypt(xmlSaldoReply, ServerCode.getKey()));
            return (new AnswerLogin(true, "", Double.valueOf(answerWithdrawProperties.getProperty("amount")).intValue()).toString());
        } else {
            if(answerProperties.getProperty("errorNote").equals("BAD_PIN")) {
                return (new AnswerLogin(false, "", 0)).toString();
            } else if(answerProperties.getProperty("errorNote").equals("TOO_MANY_INCORRECT")) {
                return (new AnswerBlocked()).toString();
            } else {
                return (new ErrorPackage("Rift to Server communication sent unexpected error")).toString();
            }
        }
    }

    private static String handleLoginPackage(ClientThread caller, RequestLogin loginPackage) {
        Properties properties = ServerCode.getDatabaseManager().getLoginData(loginPackage.getPassNumber());
        if(!ServerCode.getDatabaseManager().isBlocked(loginPackage.getPassNumber())) {
            if(properties.getProperty("failed") == null && loginPackage.getPassword() == Integer.valueOf(properties.getProperty("pincode"))) {
                caller.setLoggedIn(true);
                caller.setBankNumber(loginPackage.getPassNumber());
                caller.setRuban(loginPackage.getRuban());

                if(Integer.valueOf(properties.getProperty("loginAttempts")) > 0) {
                    ServerCode.getDatabaseManager().updateLoginAttempts(caller.getBankNumber(), 0);
                }
                return (new AnswerLogin(true, properties.getProperty("firstName"), Integer.valueOf(properties.getProperty("balance")))).toString();
            } else {
                if(Integer.valueOf(properties.getProperty("loginAttempts")) < 1) {
                    System.out.println("Setting loginAttempts for: " + loginPackage.getPassNumber() + " to: " + Integer.valueOf(properties.getProperty("loginAttempts")));
                    ServerCode.getDatabaseManager().updateLoginAttempts(loginPackage.getPassNumber(), (Integer.valueOf(properties.getProperty("loginAttempts")) + 1));
                } else {
                    ServerCode.getDatabaseManager().setBlocked(loginPackage.getPassNumber());
                }
                caller.setLoggedIn(false);
                caller.setBankNumber("");
                return (new AnswerLogin(false, "", 0)).toString();
            }
        } else {
            return (new AnswerBlocked()).toString();
        }
    }

    private static String handleLogoutPackage(ClientThread caller, RequestLogout logoutPackage) {
        caller.setBankNumber("");
        caller.setLoggedIn(false);
        caller.setPincode(0);
        caller.setRuban("");
        return (new AnswerLogout()).toString();
    }
}