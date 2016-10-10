import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.function.Consumer;

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

        if (tokenizer.countTokens() != 2) {
            sendToClient("Wrong arguments count");
            return;
        }
        long id = Long.parseLong(tokenizer.nextToken());
        String nFinish = tokenizer.nextToken();
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

        if (graph.getNode(nFinish) == null) {
            System.out.println("Node does not exist");
            sendToClient("Node does not exist");
            return;
        }

        if (user.getMoves().size() == 0) {
            user.addMove(nFinish);
            System.out.println("Started");
            sendToClient("Started");
            return;
        }

        String start = user.getLastMove();

        Node nA = graph.getNode(start);
        if (nA != null) {
            int cost = graph.checkMove(start, nFinish);
            if (cost > 0) {
                long timeIn = dbHelper.updateMoves(start, nFinish, cost);
                System.out.println("Enter "+start+" in "+timeIn+" and leave "+nFinish+" in "+System.currentTimeMillis()+
                " move cost "+cost);
                sendToClient("Enter "+start+" in "+timeIn+" and leave "+nFinish+" in "+System.currentTimeMillis()+
                        " move cost "+cost);
            }
            else {
                System.out.println("Cann't move there");
                sendToClient("Cann't move there");
                return;
            }
        }

        user.addMove(nFinish);
    }

    public UserModel getUser(long id) {

        return users.stream().filter(userModel -> userModel.getId() == id)
                .reduce((userModel, userModel2) -> userModel = userModel2).orElse(new UserModel(id));
    }

    public void sendToClient(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
