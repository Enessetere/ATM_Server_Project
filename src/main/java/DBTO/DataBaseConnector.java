package DBTO;

import java.sql.*;

public class DataBaseConnector {
    private static Connection connection;
    private static Statement statement;

    public static void startConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        connection = DriverManager
                .getConnection("jdbc:sqlserver://localhost:3366;instance=SQLEXPRESS;databaseName=ATM_DATA_DB", "sa", "21DIablo!@");
        statement = connection.createStatement();
    }

    public static boolean checkCard(String cardNumber) throws SQLException {
        boolean result;
        ResultSet resultSet = statement.executeQuery("select PIN from Card where Number=" + cardNumber);
        result = resultSet.last();
        resultSet.close();
        return  result;
    }

    public static String getPin(String cardNumber) throws SQLException {
        String result;
        ResultSet resultSet = statement.executeQuery("select PIN from Card where Number=" + cardNumber);
        result = resultSet.getString(1);
        resultSet.close();
        return  result;
    }

    public static String getBalance(String cardNumber) throws SQLException {
        String result;
        ResultSet resultSet = statement.executeQuery("SELECT P.Balance FROM Account AS P JOIN Card AS C ON P.ID=C.AccountID WHERE C.Number=" + cardNumber);
        result = resultSet.getString(1);
        resultSet.close();
        return  result;
    }

    public static void setBalance(String cardNumber, double balance) throws SQLException {
        statement.executeQuery("UPDATE P SET P.Balance=" + balance + " FROM Account AS P JOIN Card AS C ON P.ID=C.AccountID WHERE C.Number=" + cardNumber);
    }

    public static void closeConnection() throws SQLException {
        statement.close();
        connection.close();
    }
}

