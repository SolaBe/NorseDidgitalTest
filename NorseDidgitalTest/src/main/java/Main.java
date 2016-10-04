import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Sola2Be on 27.09.2016.
 */
public class Main {


    public static void main(String[] args) {

        Graph graph = Graph.init();

        try {
            ServerSocket sSocket = new ServerSocket(3232);
            Socket cSocket = sSocket.accept();

        } catch (IOException e) {
            e.printStackTrace();
        }

        UserModel user = new UserModel("aaa@aa.ii",91l);
        DatabaseHelper dbHelper = new DatabaseHelper(user);
    }



}
