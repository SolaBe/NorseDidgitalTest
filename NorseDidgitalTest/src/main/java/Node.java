import java.util.List;
import java.util.Map;

/**
 * Created by Sola2Be on 28.09.2016.
 */
public class Node {

    private String name;

    private Map<Node,Integer> links;

    public Node(String name, Map<Node, Integer> links) {
        this.name = name;
        this.links = links;
    }

    public String getName() {
        return name;
    }

    public Map<Node, Integer> getLinks() {
        return links;
    }
}
