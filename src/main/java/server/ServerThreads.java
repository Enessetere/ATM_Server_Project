package server;

import DBTO.DataBaseConnector;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

public class ServerThreads implements Runnable {
    private static DataBaseConnector dBC;
    private Socket socket;
    private String cardNumber;
    private String pin;
    private double balance;

    ServerThreads(DataBaseConnector dBC, Socket socket) {
        ServerThreads.dBC = dBC;
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream inputStream;
        BufferedReader bufferedReader;
        DataOutputStream dataOutputStream;
        try {
            inputStream = socket.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            return;
        }
        String line;
        while (true) {
            try {
                line = bufferedReader.readLine();
                if ((line == null) || line.equalsIgnoreCase("QUIT")) {
                    socket.close();
                    return;
                } else if (line.equalsIgnoreCase("SHUTDOWN")) {
                    System.out.println("Server shutdown");
                    socket.close();
                    ServerApplication.changeStatus();
                    return;
                } else {
                    if (line.equalsIgnoreCase("BALANCE")) {
                        try {
                            balance = dBC.getBalance(cardNumber);
                            line = Double.toString(balance);
                        } catch (SQLException ex) {
                            System.out.println("SQL DB malfunction");
                        }
                    } else if (line.equalsIgnoreCase("UPDATE")) {
                        try {
                            line = bufferedReader.readLine();
                            balance -= Double.parseDouble(line);
                            dBC.setBalance(cardNumber, balance);
                        } catch (SQLException ex) {
                            System.out.println("SQL DB malfunction");
                        }
                    } else if (line.equalsIgnoreCase("PIN")) {
                        line = bufferedReader.readLine();
                        if(pin.equalsIgnoreCase(line)) {
                            line = "PIN correct";
                        } else {
                            line = "PIN incorrect";
                        }
                    } else if (line.equalsIgnoreCase("CHECK")) {
                        line = bufferedReader.readLine();
                        try {
                            if (dBC.checkCard(line)) {
                                cardNumber = line;
                                pin = dBC.getPin(cardNumber);
                                balance = dBC.getBalance(cardNumber);
                                line = "true";
                            } else {
                                line = "false";
                            }
                        } catch (SQLException ex) {
                            System.out.println("Database error");
                        }
                    } else {
                        line = "Unknown command.";
                    }
                    dataOutputStream.writeBytes(line + "\n");
                    dataOutputStream.flush();
                }

            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
