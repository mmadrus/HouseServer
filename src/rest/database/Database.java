package rest.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.mongodb.*;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import rest.models.User;

/*
The database class. For now we use a local database, you need to download and start MongoDB as a service, call it "HouseDatabase".
Use the correct port for your server.

To get the object id(mongoDB's object ID, looks like: "5dc199596d60d45f6409d791"), use the method "getObjectId", which returns the object ID.
The method uses "our" object id notation (e.g. 1234, as protocol states)

*/

public class Database {
    private Gson gson;
    private MongoClient mongoClient = null;
    private DB databaseObj = null;
    private static Database database; //= Database.getInstance();
    private DBCollection dbCollection;
    private BasicDBObject document, query;
    private DBCursor cursor;
    private DBObject fetchedObject;


    public static void main(String[] args) {

        Object object = Database.getInstance().getDeviceId("1234");
        String status = Database.getInstance().getDeviceStatus(object);
        System.out.println(status);

        Object mongoObjectId = database.getDeviceId("1234");
        Database.getInstance().findUser("1234-abcd-12dc");
        Database.getInstance().changeStatusOfDevice(object);
        System.out.println("MongoObjectId " + mongoObjectId.toString());
        database.changeStatusOfDevice(mongoObjectId);

        System.out.println(database.createUser("hej"));
    }

    public void changeStatusOfDevice(Object objectId) {


    }

    private Database() {
        mongoClient = new MongoClient("localhost", 27017);
        databaseObj = mongoClient.getDB("HouseDatabase");

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


    public String createUser(String jsonString) {
        try {
         /*   JSONObject filipTest = new JSONObject();
            filipTest.put("firstName", "Filip");
            filipTest.put("lastName", "BenkanssonjAO");
            filipTest.put("password", "123456");
            filipTest.put("userName", "Benka33");
            filipTest.put("userId", "5c37692c-360f-4022-a7db-23a45f828c1d");
            filipTest.put("email", "sm@somethingmore.com");
            jsonString = filipTest.toString();  //"den som kommer från server"
*/
            dbCollection = databaseObj.getCollection("User");
            gson = new Gson();
            User user = gson.fromJson(jsonString, User.class);
            String userEmail = user.getEmail();

            document = new BasicDBObject();
            document.put("email", userEmail);
            cursor = dbCollection.find(document);

            while (cursor.hasNext()) {
                fetchedObject = cursor.next();
                if (fetchedObject.toString().contains(userEmail)) {

                    return "Email address already in use";
                }

            }

            query = new BasicDBObject();
            query.put("firstName", user.getFirstName());
            query.put("lastName", user.getLastName());
            query.put("password", user.getPassword());
            query.put("userName", user.getUserName());
            query.put("userId", user.getUserId());
            query.put("email", user.getEmail());

            dbCollection.insert(query);

        } catch (JSONException ex) {
            ex.printStackTrace();
            return "Unkown error";
        }

        return "Created succesfully";

    }

    //Find user
    public Object findUser(String id) {
        dbCollection = databaseObj.getCollection("user");

        document = new BasicDBObject();
        document.put("userId", id);
        cursor = dbCollection.find(document);

        if (!cursor.hasNext()) {
            return "No Such user";
        }

        fetchedObject = cursor.next();


        return fetchedObject;
    }

    public List<Object> getAllUsers() {
        dbCollection = databaseObj.getCollection("user");
        List<Object> allUsers = new ArrayList<>();

        cursor = dbCollection.find();
        while (cursor.hasNext()) {
            document = new BasicDBObject();

            allUsers.add(document);
        }

        return allUsers;
    }


    public static Database getInstance() {
        if (database == null) {

            database = new Database();
        }
        return database;


    }




}
