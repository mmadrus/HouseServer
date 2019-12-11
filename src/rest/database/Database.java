package rest.database;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;

import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.*;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;
import rest.models.User;
import rest.protocols.JSONProtocol;

/*
The database class. For now we use a local database, you need to download and start MongoDB as a service, call it "HouseDatabase".
Use the correct port for your server.

To get the object id(mongoDB's object ID, looks like: "5dc199596d60d45f6409d791"), use the method "getObjectId", which returns the object ID.
The method uses "our" object id notation (e.g. 1234, as protocol states)


*/

@SuppressWarnings("deprecation")
public class Database {

    private static final String URL = "ec2-13-48-28-82.eu-north-1.compute.amazonaws.com";
    private static final String AUTH_USER = "server_db";
    private static final char[] PASSWORD_AS_ARR = new char[]{'s', 'e', 'r', 'v', 'e', 'r', 'i', 's', 'k', 'i', 'n', 'g'};
    private static final String PASSWORD = "serverisking";
    private static final String PORT_NUMBER = "27017";
    private static final String DATABASE = "smart_house";


    private Gson gson;
    private MongoClient mongoClient = null;
    private static Database database = Database.getInstance();
    private DBCollection dbCollection;
    private BasicDBObject document, query;
    private DBCursor cursor;
    private DBObject fetchedObject;
    private MongoCredential mongoCredential = null;
    private DB databaseObj;


    private Database() {
        MongoClientURI uri = new MongoClientURI("mongodb://" + AUTH_USER + ":" + PASSWORD + "@" + URL + ":" + PORT_NUMBER + "/" + DATABASE);
        mongoClient = new MongoClient(uri);
        databaseObj = mongoClient.getDB("smart_house");
    }

    public void addDevices () {

        dbCollection = databaseObj.getCollection("devices");
        dbCollection.drop();
        dbCollection = databaseObj.getCollection("devices");

        document = new BasicDBObject();
        document.put("deviceId", "01");
        document.put("status", 0);
        document.put("deviceName", "Indoor Lamp");
        document.put("flag", false);
        dbCollection.insert(document);

        document = new BasicDBObject();
        document.put("deviceId", "02");
        document.put("status", 0);
        document.put("deviceName", "Outdoor Lamp");
        document.put("flag", false);
        dbCollection.insert(document);

        document = new BasicDBObject();
        document.put("deviceId", "03");
        document.put("status", 0);
        document.put("deviceName", "Indoor Temp");
        document.put("flag", false);
        dbCollection.insert(document);

        document = new BasicDBObject();
        document.put("deviceId", "04");
        document.put("status", 0);
        document.put("deviceName", "Outdoor Temp");
        document.put("flag", false);
        dbCollection.insert(document);

        document = new BasicDBObject();
        document.put("deviceId", "05");
        document.put("status", 0);
        document.put("deviceName", "Water Leakage");
        document.put("flag", false);
        dbCollection.insert(document);

        document = new BasicDBObject();
        document.put("deviceId", "06");
        document.put("status", 0);
        document.put("deviceName", "Stove");
        document.put("flag", false);
        dbCollection.insert(document);

        document = new BasicDBObject();
        document.put("deviceId", "07");
        document.put("status", 0);
        document.put("deviceName", "Fire Alarm");
        document.put("flag", false);
        dbCollection.insert(document);

        document = new BasicDBObject();
        document.put("deviceId", "08");
        document.put("status", 0);
        document.put("deviceName", "Window");
        document.put("flag", false);
        dbCollection.insert(document);

        document = new BasicDBObject();
        document.put("deviceId", "09");
        document.put("status", 0);
        document.put("deviceName", "Radiator");
        document.put("flag", false);
        dbCollection.insert(document);

        document = new BasicDBObject();
        document.put("deviceId", "10");
        document.put("status", 0);
        document.put("deviceName", "Electric Consumption");
        document.put("flag", false);
        dbCollection.insert(document);

    }

    public String getDev () {

        dbCollection = databaseObj.getCollection("Device");
        document = new BasicDBObject();
        document = new BasicDBObject();
        cursor = dbCollection.find(document);
        String s = "";

            fetchedObject = cursor.next();
            s = fetchedObject.toString();


        return s;
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
        List<Object> allUsers = new ArrayList<>();
        dbCollection = databaseObj.getCollection("user");

        cursor = dbCollection.find();
        while (cursor.hasNext()) {
            fetchedObject = cursor.next();
            allUsers.add(fetchedObject);
        }


        return allUsers;
    }

    public static Database getInstance() {
        if (database == null) {

            database = new Database();
        }
        return database;


    }

    public boolean commandLog(JSONObject jsonObject) {

        try {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = formatter.format(new Date());
            dbCollection = databaseObj.getCollection("DeviceLog");
            document = new BasicDBObject();
            document.put("dateTime", date);
            document.put("userId", jsonObject.getString("userId"));
            document.put("deviceId", jsonObject.getInt("deviceId"));
            document.put("command", jsonObject.getInt("command"));
            dbCollection.insert(document);

            return true;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }

    public void readLog() {

        try {


            dbCollection = databaseObj.getCollection("DeviceLog");

            document = new BasicDBObject();
            cursor = dbCollection.find(document);

            while (cursor.hasNext()) {
                fetchedObject = cursor.next();
                System.out.println(fetchedObject.toString());

            }


        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public Document [] getUpdates () {

        return null;
    }


}
