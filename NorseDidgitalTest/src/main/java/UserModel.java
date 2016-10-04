/**
 * Created by Sola2Be on 03.10.2016.
 */
public class UserModel {

    private final String email;
    private final long id;

    public UserModel(String email, long id) {
        this.email = email;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public long getId() {
        return id;
    }
}
