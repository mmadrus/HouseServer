package rest.database;

import java.util.*;

import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import rest.models.User;

/*
The database class. For now we use a local database, you need to download and start MongoDB as a service, call it "HouseDatabase".
Use the correct port for your server.

To get the object id(mongoDB's object ID, looks like: "5dc199596d60d45f6409d791"), use the method "getObjectId", which returns the object ID.
The method uses "our" object id notation (e.g. 1234, as protocol states)


*/

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

    public static void main(String[] args) {
        //getInstance().unitToServer(new JSONObject());

        Database database = Database.getInstance();

        System.out.println(database.getAllUsers());

    }

    public static Database getInstance() {
        if (database == null) {

            database = new Database();
        }
        return database;


    }

    private Database(String s) {
        mongoClient = new MongoClient("ec2-13-48-28-82.eu-north-1.compute.amazonaws.com", 27017);
        databaseObj = mongoClient.getDB("smart_house");

    }

    private Database() {
        MongoClientURI uri = new MongoClientURI("mongodb://" + AUTH_USER + ":" + PASSWORD + "@" + URL + ":" + PORT_NUMBER + "/" + DATABASE);
        mongoClient = new MongoClient(uri);
        databaseObj = mongoClient.getDB("smart_house");
    }

    public boolean commandLog(JSONObject jsonObject) {

        try {
/*
 json.put("token", "123455");
            json.put("requestType", "lightOn?");
            json.put("userId", "1");
            json.put("deviceId", "1;2;355");
            json.put("command", "1");
 */
            dbCollection = databaseObj.getCollection("DeviceLog");
            document = new BasicDBObject();
            document.put("dateTime", new Date().getTime());
            document.put("user-id", jsonObject.getString("userId"));
            document.put("device-id", jsonObject.getString("deviceId"));
            document.put("command", jsonObject.getString("command"));
            dbCollection.insert(document);

            return true;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }

    private void unitToServer(JSONObject fromServer) {

        try {
            JSONObject json = new JSONObject();
            JSONObject jsonToStoreInDB = new JSONObject();

            json.put("token", "123455");
            json.put("requestType", "lightOn?");
            json.put("userId", "1");
            json.put("deviceId", "3211");
            json.put("command", "1");
            fromServer = json;
            commandLog(fromServer);
            String deviceId = fromServer.getString("deviceId");
            try {
                String houseId = deviceId.substring(0, 1);
                String roomId = deviceId.substring(1, 2);
                System.out.println(roomId);
                doesEntityExistIfNotAddIt(houseId, "House", fromServer);
                doesEntityExistIfNotAddIt(roomId, "Room", fromServer);

            } catch (Exception ex) {
                ex.printStackTrace();
            }


            System.out.println(deviceId);
            jsonToStoreInDB.put("deviceId", deviceId);
            jsonToStoreInDB.put("command", fromServer.getString("command"));

            dbCollection = databaseObj.getCollection("Device");
            document = new BasicDBObject();
            document.put("id", deviceId);
            cursor = dbCollection.find(document);
            if (!cursor.hasNext()) {
                //Device not found. Lets add it
                fetchedObject = doesEntityExistIfNotAddIt(deviceId, "Device", jsonToStoreInDB);

            } else {
                fetchedObject = cursor.next();
            }

            query = new BasicDBObject();
            query.append("$set", new BasicDBObject().append("state", fromServer.getString("command")));
            BasicDBObject search = new BasicDBObject().append("id", deviceId);
            dbCollection.update(search, query);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private DBObject doesEntityExistIfNotAddIt(String id, String entity, JSONObject jsonString) {
        document = new BasicDBObject();

        switch (entity) {
            case "House":
                dbCollection = databaseObj.getCollection(entity);
                document.put("id", id);
                cursor = dbCollection.find(document);
                if (!cursor.hasNext()) {
                    //Device not found. Lets add it
                    System.out.println("House not found, lets add it");
                    return fetchedObject = addSomethingMethod(jsonString, entity);

                } else {
                    System.out.println("House found!");
                    return fetchedObject = cursor.next();
                }
            case "Room":
                dbCollection = databaseObj.getCollection(entity);
                document.put("roomId", id);
                cursor = dbCollection.find(document);
                if (!cursor.hasNext()) {
                    System.out.println("Room not found");
                    return fetchedObject = addSomethingMethod(jsonString, entity);
                } else {
                    System.out.println("Room found");
                    return fetchedObject = cursor.next();
                }

            case "Device":
                dbCollection = databaseObj.getCollection(entity);
                document.put("deviceId", id);
                cursor = dbCollection.find(document);

                if (!cursor.hasNext()) {
                    System.out.println("Device not found, lets add it");
                    return fetchedObject = addSomethingMethod(jsonString, entity);
                } else {
                    System.out.println("Device found");
                    return fetchedObject = cursor.next();
                }

        }
        return fetchedObject;

    }

    private DBObject addSomethingMethod(JSONObject fromServer, String entity) {
        dbCollection = databaseObj.getCollection(entity);
        JSONObject toAdd = new JSONObject();
        JSONArray jsonArr = new JSONArray();
        try {
            String id = fromServer.getString("deviceId");

            switch (entity) {
                case "House":

                    toAdd.put("id", id.substring(0, 1));
                    int rooms = Integer.valueOf(id.substring(1, 2));
                    if (rooms <= 0) {
                        break;
                    }
                    for (int i = 1; i < rooms; i++) {
                        jsonArr.put(id.substring(0, 2));
                    }
                    toAdd.put("Rooms", jsonArr);
                    document.put("house", toAdd);
                    break;

                case "Room":
                    jsonArr.put(id);
                    toAdd.put("roomId", id.substring(1,2));
                    toAdd.put("deviceList", jsonArr);

                    document.put("room", toAdd);

                    break;

                case "Device":
                    toAdd.put("id", fromServer.getString("deviceId"));
                    toAdd.put("state", fromServer.getString("command"));
                    document.put("device", toAdd);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            dbCollection.insert(BasicDBObject.parse(String.valueOf(toAdd)));
            System.out.println(entity + " added! ");

        }


        return document;

    }

    private String[] parseDeviceId(String deviceId) {
        return deviceId.split(";");

    }


    public String getDeviceStatus(Object id) {

        dbCollection = databaseObj.getCollection("Device");
        document = new BasicDBObject();
        document.put("id", "1234");
        cursor = dbCollection.find(document);
        fetchedObject = cursor.next();

        return fetchedObject.get("state").toString();

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
        /*   JSONObject filipTest = new JSONObject();
           filipTest.put("firstName", "Filip");
           filipTest.put("lastName", "BenkanssonjAO");
           filipTest.put("password", "123456");
           filipTest.put("userName", "Benka33");
           filipTest.put("userId", "5c37692c-360f-4022-a7db-23a45f828c1d");
           filipTest.put("email", "sm@somethingmore.com");
           jsonString = filipTest.toString();  //"den som kommer från server"
*/
        dbCollection = databaseObj.getCollection("users");
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
        query.put("username", user.getUsername());
        query.put("userId", user.getUserId());
        query.put("email", user.getEmail());

        dbCollection.insert(query);

        return "Created succesfully";

    }

    //Find user
    public Object findUser(String id) {
        dbCollection = databaseObj.getCollection("users");

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
        dbCollection = databaseObj.getCollection("users");

        cursor = dbCollection.find();
        while (cursor.hasNext()) {
            fetchedObject = cursor.next();
            allUsers.add(fetchedObject);
        }


        return allUsers;
    }

  /*  public boolean commandLog(JSONObject jsonObject) {

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
    }*/

    //Authenticate user
    public String loginMethod(String jsonString) {
      /* JSONObject testObject = new JSONObject();
       testObject.put("userName", "IsakZ");
       testObject.put("password", "123");
       jsonString = testObject.toString();
*/
        dbCollection = databaseObj.getCollection("users");
        document = new BasicDBObject();
        gson = new Gson();
        User user = gson.fromJson(jsonString, User.class);
        document.put("username", user.getUsername());
        document.put("password", user.getPassword());
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        cursor = dbCollection.find(document);
        while (cursor.hasNext()) {
            fetchedObject = cursor.next();
            if (fetchedObject.toString().contains(user.getUsername())) {
                if (fetchedObject.toString().contains(user.getPassword())) {
                    return "1"; //Login successful
                } else {
                    return "2"; //Incorrect password
                }
            } else {
                return "3"; //Incorrect username
            }
        }
        return "4"; //Big fail eggs de
    }

}
