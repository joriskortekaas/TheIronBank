package servercode;

import common.BadInputException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

/**
 * Created by Joris on 20-3-2017.
 */
@SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
public class DatabaseManager {
    private Connection connection;

    public DatabaseManager() {
        try {
            String url = "jdbc:mysql://localhost/THEPINKBANKER";
            Properties properties = new Properties();
            properties.setProperty("user", "pinkbankerserver");
            properties.setProperty("password", "d90bbe956deedb6f371dbecd5e6fb772");
            properties.setProperty("useSSL", "false");

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(url, properties);
        } catch(SQLException e) {
            e.printStackTrace();
            System.exit(1);
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public Properties getLoginData(String accountnumber) {
        try {
            Statement query = connection.createStatement();
            ResultSet queryResult = query.executeQuery("SELECT pincode, loginAttempts, balance, firstName FROM BANKACCOUNTS RIGHT JOIN CLIENTS ON BANKACCOUNTS.clientID = CLIENTS.clientID WHERE BANKACCOUNTS.cardNumber =  \'" + accountnumber + "\'");
            Properties prop = new Properties();
            if(queryResult.next()) {
                prop.setProperty("pincode", queryResult.getString(1));
                prop.setProperty("loginAttempts", queryResult.getString(2));
                prop.setProperty("balance", queryResult.getString(3));
                prop.setProperty("firstName", queryResult.getString(4));
            } else {
                prop.setProperty("failed", "true");
            }
            queryResult.close();
            query.close();
            return prop;
        }  catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateLoginAttempts(String accountNumber, int changeAttemptsTo) {
        try {
            Statement query = connection.createStatement();
            query.executeUpdate("UPDATE BANKACCOUNTS SET loginAttempts = \'" + changeAttemptsTo + "\' WHERE BANKACCOUNTS.cardNumber = \'" + accountNumber + "\'");
            query.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void setBlocked(String accountNumber) {
        try {
            Statement query = connection.createStatement();
            query.executeUpdate("UPDATE BANKACCOUNTS SET blocked = 1 WHERE BANKACCOUNTS.cardNumber = \'" + accountNumber + "\'");
            query.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isBlocked(String accountNumber) {
        try {
            Statement query = connection.createStatement();
            ResultSet queryStatement = query.executeQuery("SELECT blocked FROM BANKACCOUNTS WHERE BANKACCOUNTS.cardNumber = \'" + accountNumber + "\'");
            queryStatement.next();
            boolean temp = Integer.valueOf(queryStatement.getString(1)) == 1;
            queryStatement.close();
            query.close();
            return temp;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean withdrawMoney(String accountNumber, int moneyToDeduct){
        try {
            Statement query = connection.createStatement();
            int affectedRows = query.executeUpdate("UPDATE BANKACCOUNTS SET balance = balance - \'" + moneyToDeduct + "\' WHERE BANKACCOUNTS.cardNumber =  \'" + accountNumber + "\'");
            boolean temp = ((affectedRows == 1) ? true : false);
            query.close();
            return temp;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addLogEntry(String accountNumber, int moneyDeducted) {
        String dateTime = LocalDate.now().format(DateTimeFormatter.ofPattern("uuuu-MM-dd"));
        try {
            Statement query = connection.createStatement();
            int affectedRows = query.executeUpdate("INSERT INTO TRANSACTIONS (accountNumber, atmID, amount, transactionDate) VALUES ( (SELECT accountNumber FROM BANKACCOUNTS WHERE BANKACCOUNTS.cardNumber = \'" + accountNumber + "\'), '0', \'" + moneyDeducted + "\', \'" + dateTime + "\')");
            boolean temp = ((affectedRows == 1) ? true : false);
            query.close();
            return temp;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Connection getConnection() {
        return connection;
    }
}
