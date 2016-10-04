import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.BSON;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sola2Be on 03.10.2016.
 */
public class DatabaseHelper {

    final static String DB_NAME = "payment_roads";
    final static String TABLE_USER = "user";
    final static String TABLE_MOVES = "moves";
    private MongoClient mClient;
    private MongoDatabase mDatabase;
    private UserModel cUser;

    public DatabaseHelper(UserModel user) {
        mClient = new MongoClient();
        mDatabase = mClient.getDatabase(DB_NAME);
        checkUser(user);
    }

    public void checkUser(UserModel user) {
        MongoCollection tUser = mDatabase.getCollection(TABLE_USER);
        BasicDBObject query = new BasicDBObject("user_id", user.getId());
        Map <String, Object> fields = new HashMap<String, Object>();
        fields.put("user_id", user.getId());
        fields.put("user_email", user.getEmail());
        FindIterable<Document> iterable = tUser.find(new Document(fields));
        iterable.forEach(new Block<Document>() {
            public void apply(Document document) {
                System.out.print("Document "+document);
            }
        });
    }

}
