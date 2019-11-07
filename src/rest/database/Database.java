package rest.database;

import java.util.Date;

import com.mongodb.*;
import org.bson.types.ObjectId;
import org.json.JSONObject;

/*
The database class. For now we use a local database, you need to download and start MongoDB as a service, call it "HouseDatabase".
Use the correct port for your server.

To get the object id(mongoDB's object ID, looks like: "5dc199596d60d45f6409d791", use the method "getObjectId", which returns the object ID.
The method uses "our" object id notation (e.g. 1234, as protocol states)

*/

public class Database {
    private MongoClient mongoClient = null;
    private DB databaseObj = null;
    private static Database database = Database.getInstance();
    private DBCollection dbCollection;
    private BasicDBObject document;
    private DBCursor cursor;
    private DBObject fetchedObject;


    public static void main(String[] args) {

        Object objectId = Database.getInstance().getDeviceId("1234");
        String status = Database.getInstance().getDeviceStatus(objectId);
        System.out.println(status);

        Database.getInstance().findUser("1234-abcd-12dc");

    }

    private Database() {
        mongoClient = new MongoClient("localhost", 27017);
        databaseObj = mongoClient.getDB("HouseDatabase");

    }


    public static Database getInstance() {
        if (database == null) {

            database = new Database();
        }
        return database;


    }


    public String getDeviceStatus(Object id) {

        dbCollection = databaseObj.getCollection("Device");
        document = new BasicDBObject();

        document.put("_id", id);
        cursor = dbCollection.find(document);
        fetchedObject = cursor.next();

        return fetchedObject.get("State").toString();

    }

    public Object getDeviceId(String id) {
        dbCollection = databaseObj.getCollection("Device");

        document = new BasicDBObject();
        document.put("id", id);
        cursor = dbCollection.find(document);

        fetchedObject = cursor.next();


        return fetchedObject.get("_id");
    }


    //Create user


    public void createUser(JSONObject json) {
        /*
        user Id
        user Email
        UserName
        First name
        Last name
        Password
         */


    }

    //Find user
    public Object findUser(String id) {
        dbCollection = databaseObj.getCollection("User");

        document = new BasicDBObject();
        document.put("userId", id);
        cursor = dbCollection.find(document);

        fetchedObject = cursor.next();



        return fetchedObject;


    }

    public void createUser(String jsonString){


    }


}
