package DBTO;

import java.sql.*;
import java.util.Arrays;

public class DataBaseConnector {
    private Connection connection;
    private Statement statement;
    private static boolean isAvailable;

    public DataBaseConnector() {
        try {
            startConnection();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println(Arrays.toString(ex.getStackTrace()));
        }
    }

    private void startConnection() throws SQLException, ClassNotFoundException {
        isAvailable = true;
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        connection = DriverManager
                .getConnection("jdbc:sqlserver://localhost:3366;instance=SQLEXPRESS;databaseName=ATM_DATA_DB", "sa", "21DIablo!@");
        statement = connection.createStatement();
        System.out.println("Connection opened.");
    }

    public boolean checkCard(String cardNumber) throws SQLException {
        boolean result;
        isAvailable = false;
        ResultSet resultSet = statement.executeQuery("select PIN from Card where Number=" + cardNumber);
        result = resultSet.next();
        resultSet.close();
        isAvailable = true;
        return result;
    }

    public String getPin(String cardNumber) throws SQLException {
        String result;
        isAvailable = false;
        ResultSet resultSet = statement.executeQuery("select * from Card where Number=" + cardNumber);
        resultSet.next();
        result = resultSet.getString("PIN");
        resultSet.close();
        isAvailable = true;
        return result;
    }

    public String getBalance(String cardNumber) throws SQLException {
        String result;
        isAvailable = false;
        ResultSet resultSet = statement.executeQuery("SELECT P.Balance FROM Account AS P JOIN Card AS C ON P.ID=C.AccountID WHERE C.Number=" + cardNumber);
        resultSet.next();
        result = resultSet.getString(1);
        resultSet.close();
        isAvailable = true;
        return result;
    }

    public void setBalance(String cardNumber, double balance) throws SQLException {
        isAvailable = false;
        statement.executeUpdate("UPDATE P SET P.Balance=" + balance + " FROM Account AS P JOIN Card AS C ON P.ID=C.AccountID WHERE C.Number=" + cardNumber);
        isAvailable = true;
    }

    public void closeConnection() throws SQLException {
        statement.close();
        connection.close();
        System.out.println("Connection closed.");
    }

    public static boolean isAvailable() {
        return isAvailable;
    }
}

