package server;

import DBTO.DataBaseConnector;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ServerApplication {
    private static final int port = 3367;
    private static boolean shutdown;

    public static void launchServer(DataBaseConnector dBC) {
        ServerSocket serverSocket;
        Socket socket;
        shutdown = false;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            System.out.println("Cannot create ServerSocket");
            return;
        }

        try {
            serverSocket.setSoTimeout(10);
        } catch (SocketException ex) {
            System.out.println("Accept interrupted");
        }

        while (!shutdown) {
            try {
                socket = serverSocket.accept();
                new Thread(new ServerThreads(dBC, socket)).start();
            } catch (IOException ignored) {
            }
        }
    }

    static void changeStatus() {
        shutdown = !shutdown;
    }
}
