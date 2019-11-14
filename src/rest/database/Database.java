package rest.database;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.*;
import org.json.JSONException;
import org.json.JSONObject;
import rest.models.User;
import rest.service.AuthServiceImpl;

import javax.naming.AuthenticationException;

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
    private static Database database = Database.getInstance();
    private DBCollection dbCollection;
    private BasicDBObject document, query;
    private DBCursor cursor;
    private DBObject fetchedObject;
    private MongoCredential mongoCredential = null;

    private final String URL = "localhost";
    private final String AUTH_USER = "server_db";
    private final char[] PASSWORD_AS_ARR = new char[]{'s', 'e', 'r', 'v', 'e', 'r', 'i', 's', 'k', 'i', 'n', 'g'};
    private final String PASSWORD = "serverisking";
    private final String PORT_NUMBER = "27017";
    private final String DATABASE = "smart_house";


    public void changeStatusOfDevice(Object objectId) {
    }

    private Database() {
        try {
            mongoCredential = MongoCredential.createCredential(AUTH_USER, DATABASE, PASSWORD_AS_ARR);
            MongoClientOptions options = MongoClientOptions.builder()
                    .writeConcern(WriteConcern.JOURNALED).build();
            // mongoClient = new MongoClient(new ServerAddress("localhost", 27017),
            //        Arrays.asList(mongoCredential), options);
            mongoClient = new MongoClient(new MongoClientURI("mongodb://" + AUTH_USER + ":" + PASSWORD + "@" + "localhost" + "/" + DATABASE));

            //mongoClient = new MongoClient("localhost", 27017);
            databaseObj = mongoClient.getDB("smart_house");
        }catch (Exception ix) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ix);
        }
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

    public boolean commandLog(JSONObject jsonObject) {

        try {

            dbCollection = databaseObj.getCollection("DeviceLog");
            document = new BasicDBObject();
            document.put("dateTime", new Date().getTime());
            document.put("user-id", jsonObject.getString("user-id"));
            document.put("device-id", jsonObject.getString("device-id"));
            document.put("command", jsonObject.getInt("command"));
            dbCollection.insert(document);

            return true;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }


}
