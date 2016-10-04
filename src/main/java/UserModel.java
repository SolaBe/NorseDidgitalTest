import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sola2Be on 03.10.2016.
 */
public class UserModel {

    private long id;
    private List<String> moves;

    public UserModel(long id) {
        this.id = id;
        moves = new ArrayList<String>();
    }

    public long getId() {
        return id;
    }

    public String getLastMove() {
        return (moves.size() == 0) ? moves.get(moves.size()) : moves.get(moves.size() - 1);
    }

    public List<String> getMoves() {
        return moves;
    }

    public void addMove(String node) {
        moves.add(node);
    }
}
