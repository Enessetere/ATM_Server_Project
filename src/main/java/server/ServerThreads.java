package server;

import DBTO.DataBaseConnector;

import java.io.*;
import java.net.Socket;

public class ServerThreads implements Runnable {
    private static DataBaseConnector dBC;
    private Socket socket;

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
                    socket.close();
                    ServerApplication.changeStatus();
                    return;
                } else {
                    dataOutputStream.writeBytes(line + "\n\r");
                    dataOutputStream.flush();
                }

            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
