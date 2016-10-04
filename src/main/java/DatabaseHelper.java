import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Sola2Be on 03.10.2016.
 */
public class DatabaseHelper {

    final static String DB_NAME = "payment_roads";
    final static String TABLE_USER = "users";
    private MongoClient mClient;
    private MongoDatabase mDatabase;
    private MongoCollection uCollection;
    private long userId;

    public DatabaseHelper() {
        mClient = new MongoClient();
        mDatabase = mClient.getDatabase(DB_NAME);
        System.out.println("Connected to database");
        uCollection = mDatabase.getCollection(TABLE_USER);
        uCollection.drop();
        checkDocumentExist();
    }

    public boolean checkUser(long id) {

        BasicDBObject query = new BasicDBObject("user_id", id);

        FindIterable<Document> iterable = uCollection.find(query).limit(1);

        MongoCursor<Document> cursor = iterable.iterator();

        if (!cursor.hasNext()) {
            return false;
        }
        else {
            System.out.println(cursor.next());
            userId = id;
            return true;
        }
    }

    public long updateMoves(String start, String finish, int moveCost) {

        BasicDBObject query = new BasicDBObject("user_id", userId);
        query.append("moves", new BasicDBObject("$exists", true));

        FindIterable<Document> it = uCollection.find(query).limit(1);

        long timeIn = 0;

        MongoCursor<Document> cursor = it.iterator();

        if (cursor.hasNext()) {
            ArrayList<Document> listMoves = (ArrayList<Document>) cursor.next().get("moves");
            if (listMoves.size() > 0)
                timeIn = Long.parseLong((String) listMoves.get(listMoves.size()-1).get("time"));
            else
                timeIn = System.currentTimeMillis();
        }

        BasicDBList moves = new BasicDBList();
        moves.add(new BasicDBObject().append("node_start",start).append("node_finish", finish)
                .append("cost",moveCost).append("time",System.currentTimeMillis()));

        BasicDBObject newMoves = new BasicDBObject();
        newMoves.append("$set", new BasicDBObject().append("moves",moves));
        uCollection.updateOne(query,newMoves);

        return timeIn;
    }

    private void checkDocumentExist() {

        BasicDBObject query = new BasicDBObject("user_id", new BasicDBObject("$exists",true));
        query.append("user_email", new BasicDBObject("$exists",true));

        FindIterable<Document> it = uCollection.find(query);

        MongoCursor<Document> cursor = it.iterator();
        if (!cursor.hasNext())
            insertUsers();
    }

    private void insertUsers() {

        Document documentA = new Document("user_id",91l).append("user_email", "userA@mail.com");
        documentA.append("moves", new BasicDBList());

        Document documentB = new Document("user_id",45l).append("user_email", "userB@mail.com");
        documentB.append("moves", new BasicDBList());

        Document documentC = new Document("user_id",73l).append("user_email", "userC@mail.com");
        documentC.append("moves", new BasicDBList());

        List<Document> listUsers = new ArrayList<Document>();
        listUsers.add(documentA);
        listUsers.add(documentB);
        listUsers.add(documentC);

        uCollection.insertMany(listUsers);
        System.out.print(documentB);

    }

    public void close() {
        mClient.close();
    }

}
