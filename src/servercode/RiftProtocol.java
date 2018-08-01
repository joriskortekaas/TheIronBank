package servercode;

import common.AnswerBlocked;
import common.AnswerLogin;
import common.AnswerWithdrawal;
import common.RequestLogin;

import java.security.Key;
import java.util.Base64;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class RiftProtocol {

    private static byte[] ivBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x00, 0x01, 0x02, 0x03, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x01 };
    public static String encrypt(String dataIn,String key) {
        try {
            // Create key and cipher
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            // encrypt the text
            cipher.init(Cipher.ENCRYPT_MODE, aesKey, ivSpec);

            byte[] encrypted = cipher.doFinal(dataIn.getBytes());
            encrypted = Base64.getEncoder().encode(encrypted);

            return new String(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String dataIn,String key) {
        try {
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            cipher.init(Cipher.DECRYPT_MODE, aesKey, ivSpec);
            byte[] decodedBytes = Base64.getDecoder().decode(dataIn.getBytes());
            String decrypted = new String(cipher.doFinal(decodedBytes));

            return decrypted;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String parsePropertiesToXML(Properties properties) {
        String XMLString = "<ATM_Request>";
        Enumeration e = properties.propertyNames();
        while(e.hasMoreElements()) {
            String key = (String) e.nextElement();
            XMLString += "<" + key + ">" + properties.getProperty(key) + "</" + key + ">";
        }
        XMLString += "<pinautomaatNr>1</pinautomaatNr>";
        XMLString += "</ATM_Request>";
        return XMLString;
    }

    public static Properties parseXMLToProperties(String XMLString) {
        Properties propertiesToAdd = new Properties();
        if(XMLString.contains("<ATM_Request>")){
            int index = 0;
            XMLString = XMLString.substring(13, XMLString.length() - 14);
            while(index != -1) {
                String propertyName = XMLString.substring(index + 1, XMLString.indexOf(">", index));
                String propertyValue = XMLString.substring(XMLString.indexOf(">", index) + 1, XMLString.indexOf("</" + propertyName + ">", index));
                propertiesToAdd.setProperty(propertyName, propertyValue);

                index = XMLString.indexOf("<", XMLString.indexOf("</" + propertyName + ">", index) + 1);
            }
        } else {
            propertiesToAdd.setProperty("errorNote", "Not a proper XMLString");
        }

        return propertiesToAdd;
    }

    public static Properties handleRiftRequest(Properties request) {
        Properties reply = new Properties();
        reply.setProperty("afzender", "074201");
        if(request.getProperty("actionType").equals("login")) {
            return handleLoginRequest(request, reply);
        } else if (request.getProperty("actionType").equals("getSaldo")) {
            return handleSaldoRequest(request, reply);
        } else if (request.getProperty("actionType").equals("withdraw")) {
            return handleWithdrawRequest(request, reply);
        } else {
            request.setProperty("errorNote", "UNEXPECTED_ERROR");
            request.setProperty("banknumber", request.getProperty("afzender"));
            request.setProperty("afzender", "074201");
            return request;
        }
    }

    private static Properties handleLoginRequest(Properties request, Properties reply) {
        Properties properties = ServerCode.getDatabaseManager().getLoginData(request.getProperty("pasnumber"));
        reply.setProperty("actionType", "loginReply");
        reply.setProperty("pasnumber", request.getProperty("pasnumber"));
        reply.setProperty("banknumber", request.getProperty("afzender"));
        reply.setProperty("pincode", "" + request.getProperty("pincode"));

        if(!ServerCode.getDatabaseManager().isBlocked(request.getProperty("pasnumber"))) {
            if(properties.getProperty("failed") == null && request.getProperty("pincode").equals(properties.getProperty("pincode"))) {
                if(Integer.valueOf(properties.getProperty("loginAttempts")) > 0) {
                    ServerCode.getDatabaseManager().updateLoginAttempts(request.getProperty("pasnumber"), 0);
                }
                reply.setProperty("succes", "true");
                reply.setProperty("errorNote", "");
                return reply;
            } else {
                if(Integer.valueOf(properties.getProperty("loginAttempts")) < 1) {
                    System.out.println("Setting loginAttempts for: " + request.getProperty("pasnumber") + " to: " + Integer.valueOf(properties.getProperty("loginAttempts")));
                    ServerCode.getDatabaseManager().updateLoginAttempts(request.getProperty("pasnumber"), (Integer.valueOf(properties.getProperty("loginAttempts")) + 1));
                } else {
                    ServerCode.getDatabaseManager().setBlocked(request.getProperty("pasnumber"));
                }
                reply.setProperty("succes", "false");
                reply.setProperty("errorNote", "BAD_PIN");
                return reply;
            }
        } else {
            reply.setProperty("succes", "false");
            reply.setProperty("errorNote", "TOO_MANY_INCORRECT");
            return reply;
        }
    }

    private static Properties handleWithdrawRequest(Properties request, Properties reply) {
        Properties properties = ServerCode.getDatabaseManager().getLoginData(request.getProperty("pasnumber"));

        reply.setProperty("actionType", "withdrawConfirm");
        reply.setProperty("pasnumber", request.getProperty("pasnumber"));
        reply.setProperty("banknumber", request.getProperty("afzender"));
        reply.setProperty("pincode", "" + request.getProperty("pincode"));

        if(request.getProperty("pincode").equals(properties.getProperty("pincode"))) {
            ServerCode.getDatabaseManager().addLogEntry(request.getProperty("pasnumber"), Integer.valueOf(request.getProperty("amount")));
            boolean databaseValue = ServerCode.getDatabaseManager().withdrawMoney(request.getProperty("pasnumber"), Integer.valueOf(request.getProperty("amount")));
            reply.setProperty("succes", "" + databaseValue);
            if(!databaseValue) {
                reply.setProperty("errorNote", "NOT_ENOUGH_BALANCE");
            } else {
                reply.setProperty("errorNote", "");
            }
        } else {
            reply.setProperty("succes", "false");
            reply.setProperty("errorNote", "BAD_PIN");
        }
        return reply;
    }

    private static Properties handleSaldoRequest(Properties request, Properties reply) {
        Properties properties = ServerCode.getDatabaseManager().getLoginData(request.getProperty("pasnumber"));

        reply.setProperty("actionType", "saldoReply");
        reply.setProperty("pasnumber", request.getProperty("pasnumber"));
        reply.setProperty("banknumber", request.getProperty("afzender"));
        reply.setProperty("pincode", "" + request.getProperty("pincode"));

        if(request.getProperty("pincode").equals(properties.getProperty("pincode"))) {
            reply.setProperty("succes", "true");
            reply.setProperty("errorNote", "");
            reply.setProperty("amount", properties.getProperty("balance"));
        } else {
            reply.setProperty("succes", "false");
            reply.setProperty("errorNote", "BAD_PIN");
            reply.setProperty("amount", "");
        }

        return reply;
    }
}