import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Sola2Be on 04.10.2016.
 */
public class Server {

    private DataOutputStream out;
    private List<UserModel> users;
    private DatabaseHelper dbHelper;
    private Graph graph = Graph.init();

    public Server() {

        users = new ArrayList<UserModel>();
        try {
            ServerSocket sSocket = new ServerSocket(3333);
            Socket cSocket = sSocket.accept();

            InputStream sin = cSocket.getInputStream();
            OutputStream sout = cSocket.getOutputStream();

            DataInputStream in = new DataInputStream(sin);
            out = new DataOutputStream(sout);

            dbHelper = new DatabaseHelper();

            String line;
            while (true) {
                line = in.readUTF();
                System.out.println(line);
                readDataFromClient(new StringTokenizer(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readDataFromClient(StringTokenizer tokenizer) {

        long id = Long.parseLong(tokenizer.nextToken());
        String node = tokenizer.nextToken();
        UserModel user;

        if(dbHelper.checkUser(id)) {
            if (users.size() == 0) {
                user = new UserModel(id);
                users.add(user);
            }
            else {
                user = getUser(id);
            }
        }
        else {
            System.out.println("User does not exist");
            sendToClient("User does not exist");
            return;
        }

        if (graph.getNode(node) == null) {
            System.out.println("Node does not exist");
            sendToClient("Node does not exist");
            return;
        }

        if (user.getMoves().size() == 0) {
            user.addMove(node);
            System.out.println("Started");
            sendToClient("Started");
            return;
        }

        String start = user.getLastMove();
        String finish = node;

        Node nA = graph.getNode(start);
        if (nA != null) {
            int cost = graph.checkMove(start, finish);
            if (cost > 0) {
                long timeIn = dbHelper.updateMoves(start, finish, cost);
                System.out.println("Enter "+start+" in "+timeIn+" and leave "+finish+" in "+System.currentTimeMillis()+
                " move cost "+cost);
                sendToClient("Enter "+start+" in "+timeIn+" and leave "+finish+" in "+System.currentTimeMillis()+
                        " move cost "+cost);
            }
            else {
                System.out.println("Cann't move there");
                sendToClient("Cann't move there");
                return;
            }
        }

        user.addMove(node);
    }

    public UserModel getUser(long id) {
        for (UserModel user : users) {
            if (user.getId() == id)
                return user;
        }
        return new UserModel(id);
    }

    public void sendToClient(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
