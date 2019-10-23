package server;

import DBTO.DataBaseConnector;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApplication {
    private static final int port = 3367;
    private static boolean shutdown;

    public static void launchServer(DataBaseConnector dBC) {
        ServerSocket serverSocket;
        Socket socket = null;
        shutdown = false;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            System.out.println("Cannot create ServerSocket");
            return;
        }

        while (!shutdown) {
            try {
                socket = serverSocket.accept();
            } catch (IOException ex) {
                System.out.println("Problem with acceptance of client");
            }
            new Thread(new ServerThreads(dBC, socket)).start();
        }
    }

    static void changeStatus() {
        shutdown = !shutdown;
    }
}
