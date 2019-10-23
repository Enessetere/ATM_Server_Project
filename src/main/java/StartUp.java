import DBTO.DataBaseConnector;
import server.ServerApplication;

public class StartUp {
    private static  DataBaseConnector dBC = new DataBaseConnector();

    public static void main(String[] args) {
        ServerApplication.launchServer(dBC);
    }
}
