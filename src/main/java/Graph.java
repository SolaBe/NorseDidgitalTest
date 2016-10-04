
import java.util.HashMap;


/**
 * Created by Sola2Be on 03.10.2016.
 */
public class Graph {

    private int nCount = 10;
    private Node[] nodes;
    private String[] nNames = {"A","B","C","D","E","F","G","H","J","K"};
    public static Graph graph;

    private Graph() {

        //init arrays
        nodes = new Node[nCount];
        for (int i = 0; i < nCount; i++) {
            nodes[i] = new Node(nNames[i],new HashMap<Node, Integer>());
        }

        //fill links and weights
        nodes[0].getLinks().put(nodes[1],5);
        nodes[0].getLinks().put(nodes[4],6);
        nodes[1].getLinks().put(nodes[0],5);
        nodes[1].getLinks().put(nodes[5],3);
        nodes[1].getLinks().put(nodes[2],3);
        nodes[2].getLinks().put(nodes[1],3);
        nodes[2].getLinks().put(nodes[3],4);
        nodes[2].getLinks().put(nodes[6],3);
        nodes[2].getLinks().put(nodes[7],3);
        nodes[3].getLinks().put(nodes[2],4);
        nodes[3].getLinks().put(nodes[4],3);
        nodes[4].getLinks().put(nodes[0],6);
        nodes[4].getLinks().put(nodes[8],3);
        nodes[4].getLinks().put(nodes[9],2);
        nodes[5].getLinks().put(nodes[1],3);
        nodes[6].getLinks().put(nodes[2],3);
        nodes[7].getLinks().put(nodes[2],3);
        nodes[8].getLinks().put(nodes[4],3);
        nodes[9].getLinks().put(nodes[4],2);
    }

    public static Graph init() {
        if (graph == null)
            graph = new Graph();
        return graph;
    }

    public Node getNode(String name) {
        if (getIndexByName(name) >= 0)
            return nodes[getIndexByName(name)];
        else
            return null;
    }

    public int checkMove(String start, String finish) {
        Node nStart = getNode(start);
        Node nFinish = getNode(finish);
        if (nStart != null && nFinish != null) {
            if (nStart.getLinks().containsKey(nFinish))
                return nStart.getLinks().get(nFinish);
            else
                return -1;
        }
        else
            return -1;
    }


    private int getIndexByName(String name) {
        for (int i = 0; i < nCount; i++) {
            if (nNames[i].equals(name))
                return i;
        }
        return -1;
    }
}
