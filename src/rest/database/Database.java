package rest.database;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.DB;
import com.mongodb.*;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
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

public class Database {

    private static final String URL = "ec2-13-48-28-82.eu-north-1.compute.amazonaws.com";
    private static final String AUTH_USER = "server_db";
    private static final char[] PASSWORD_AS_ARR = new char[]{'s', 'e', 'r', 'v', 'e', 'r', 'i', 's', 'k', 'i', 'n', 'g'};
    private static final String PASSWORD = "serverisking";
    private static final String PORT_NUMBER = "27017";
    private static final String DATABASE = "smart_house";

    public static boolean isAuthenticated = false;
    public static int result;
    private static Database database = Database.getInstance();


    private int rommIdInt;


    private Gson gson;
    private MongoClient mongoClient = null;
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

    }

    /*
    commandType 1 == login
    commandType 2 == deviceLog
    commandType 3 == trying to get access to house
    commandType 4 == create User
    commandType 5 == create new house log
    commandType 6 ==
    commandType 7 == create new Device
     */
    public boolean commandLog(JSONObject jsonObject, int commandType) {
        System.out.println("Log created with type " + commandType);
        dbCollection = databaseObj.getCollection("Logs");
        document = new BasicDBObject();
        try {
            switch (commandType) {

                case 1:


                    document.put("requestType", jsonObject.getString("requestType"));
                    document.put("logTime", new Date());
                    document.put("username", jsonObject.getString("username"));
                    document.put("password", jsonObject.getString("password"));
                    document.put("requestResult", jsonObject.getString("result"));
                    System.out.println("request log added");
                    break;


                case 2:

                    dbCollection = databaseObj.getCollection("device_log");
                    document = new BasicDBObject();

                    document.put("userID", jsonObject.getString("userID"));
                    document.put("command", jsonObject.getString("command"));
                    System.out.println("Device log added");
                    dbCollection.insert(document);
                    break;
                case 3:

                    document.put("userID", jsonObject.getString("userID"));
                    document.put("token", jsonObject.getString("token"));
                    document.put("requesType", jsonObject.getString("requestType"));
                    document.put("houseID", jsonObject.getString("houseID"));
                    dbCollection.insert(document);

                    break;
                case 4:
                    dbCollection = databaseObj.getCollection("logs");
                    document = new BasicDBObject();
                    //fixa log för create user

                    break;
                case 5: //create new house

                    document.put("token", jsonObject.getString("token"));
                    document.put("houseName", jsonObject.getString("houseName"));
                    document.put("userID", jsonObject.getString("userID"));
                    document.put("result", jsonObject.getString("result"));

                    break;

                case 6:

                    break;

                case 7:
                    dbCollection = databaseObj.getCollection("device_log");
                    document = new BasicDBObject();
                    document.put("requestType", "createDevice");
                    document.put("username", jsonObject.getString("username"));
                    document.put("roomID", jsonObject.getString("roomID"));


                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + commandType);
            }
            document.put("Date", new Date());
            dbCollection.insert(document);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;
    }


    private JSONObject changeState(JSONObject fromServer) {

        try {
            JSONObject json = new JSONObject();
            JSONObject jsonToStoreInDB = new JSONObject();

            json.put("token", "123455");
            json.put("requestType", "changeLight");
            json.put("userID", "1");
            json.put("deviceId", "1234");
            json.put("command", 0);
            fromServer = json;
            commandLog(fromServer, 2);
            String deviceId = fromServer.getString("deviceId");
            try {
                String houseId = deviceId.substring(0, 1);
                String roomId = deviceId.substring(0, 2);
                System.out.println(roomId);
                boolean houseExist = doesEntityExist(houseId, "houses", fromServer);
                boolean roomExist = doesEntityExist(roomId, "rooms", fromServer);

                if (!houseExist || !roomExist) {
                    //Returna error pga hus eller rum inte finns
                    JSONObject errorJson = new JSONObject();

                    errorJson.put("requestType", fromServer.get("requestType"));
                    errorJson.put("deviceId", fromServer.getString("deviceId"));
                    errorJson.put("result", 0);
                    System.out.println("room or house does not exist");
                    return errorJson;


                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }


            System.out.println(deviceId);
            jsonToStoreInDB.put("deviceId", deviceId);
            jsonToStoreInDB.put("command", fromServer.getString("command"));

            dbCollection = databaseObj.getCollection("devices");
            document = new BasicDBObject();
            document.put("id", deviceId);
            cursor = dbCollection.find(document);
            if (!cursor.hasNext()) {
                //Device not found. Lets add it
                if (!doesEntityExist(deviceId, "devices", jsonToStoreInDB)) {
                    JSONObject errorJson = new JSONObject();

                    errorJson.put("requestType", fromServer.get("requestType"));
                    errorJson.put("deviceId", fromServer.getString("deviceId"));
                    errorJson.put("result", 0);
                    return errorJson;
                }


            } else {
                fetchedObject = cursor.next();

            }

            query = new BasicDBObject();
            query.append("$set", new BasicDBObject().append("state", fromServer.getInt("command")));
            BasicDBObject search = new BasicDBObject().append("deviceId", deviceId);
            dbCollection.update(search, query);
            JSONObject successJson = new JSONObject();
            successJson.put("requestType", fromServer.getInt("command"));
            successJson.put("deviceId", fromServer.getString("deviceId"));
            successJson.put("result", 1);

            return successJson;


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    } //Unit o server (command protocol)


    private JSONObject addAccessToServer(JSONObject fromServer) {
        JSONObject successJson = new JSONObject();
        JSONObject failJson = new JSONObject();
        JSONObject testJson = new JSONObject();
        try {

            testJson.put("username", "Bole");
            testJson.put("token", "12345555");
            testJson.put("houseId", 1);
            testJson.put("housePassword", "abc");


            fromServer = testJson;
            commandLog(fromServer, 3);


            dbCollection = databaseObj.getCollection("house");

            document.put("houseID", fromServer.getString("houseID"));

            cursor = dbCollection.find(document);

            if (!cursor.hasNext()) {
                failJson.put("result", 0);
                return failJson;
            } else {

                fetchedObject = cursor.next();

                if (fetchedObject.get("housePassword").equals(fromServer.getString("housePassword"))) {

                    BasicDBList list = (BasicDBList) fetchedObject.get("houseAdmins");


                    successJson.put("result", 1);
                    return successJson;

                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return successJson;
    }

    private boolean doesEntityExist(String id, String entity, JSONObject jsonString) {
        document = new BasicDBObject();

        switch (entity) {
            case "houses":
                dbCollection = databaseObj.getCollection(entity);
                document.put("id", id);
                cursor = dbCollection.find(document);
                if (!cursor.hasNext()) {
                    //Device not found. Lets add it
                    System.out.println("House not found, lets add it");
                    return false;

                } else {
                    System.out.println("House found!");
                    return true;
                }
            case "Room":
                dbCollection = databaseObj.getCollection(entity);
                document.put("roomId", id);
                cursor = dbCollection.find(document);
                if (!cursor.hasNext()) {
                    System.out.println("Room not found");

                    return false;
                } else {
                    System.out.println("Room found");
                    return true;
                }

            case "Device":
                dbCollection = databaseObj.getCollection(entity);
                document.put("deviceId", id);
                cursor = dbCollection.find(document);

                if (!cursor.hasNext()) {
                    System.out.println("Device not found");
                    return false;
                } else {
                    System.out.println("Device found");
                    return true;

                }

        }
        return false;

    }

    private DBObject addSomethingMethod(JSONObject fromServer, String entity) {
        dbCollection = databaseObj.getCollection(entity);
        JSONObject toAdd = new JSONObject();
        JSONArray jsonArr = new JSONArray();
        try {
            String id = fromServer.getString("deviceId");

            switch (entity) {
                case "houses":

                    toAdd.put("id", id.substring(0, 1));
                    int rooms = Integer.valueOf(id.substring(1, 2));
                    if (rooms <= 0) {
                        break;
                    }
                    for (int i = 1; i < rooms; i++) {
                        jsonArr.put(id.substring(0, 2));
                    }
                    toAdd.put("roomList", jsonArr);
                    document.put("house", toAdd);
                    break;

                case "rooms":
                    jsonArr.put(id);
                    toAdd.put("roomId", id.substring(1, 2));
                    toAdd.put("deviceList", jsonArr);

                    document.put("room", toAdd);

                    break;

                case "devices":
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

    private JSONObject createNewDevice(JSONObject fromServer) {
        JSONObject successJson = new JSONObject();
        JSONObject failJson = new JSONObject();
        JSONObject testJson = new JSONObject();
        JSONArray arr = new JSONArray();
        try {
            int actualDeviceId;
            int nextDeviceId;
            testJson.put("username", "Bole");
            testJson.put("token", "1234555");
            testJson.put("roomID", 12);
            testJson.put("deviceName", "Outdoor lamp");
            testJson.put("type", "Lamp");
            testJson.put("status", 0);
            fromServer = testJson;
            dbCollection = databaseObj.getCollection("room");
            document = new BasicDBObject();
            document.put("roomID", fromServer.get("roomID"));
            cursor = dbCollection.find(document);

            if (cursor.hasNext()) {
                System.out.println("Room found! ");
                fetchedObject = cursor.next();
                BasicDBList dbList = (BasicDBList) fetchedObject.get("deviceList");
                nextDeviceId = dbList.size() + 1;
                actualDeviceId = nextDeviceId + testJson.getInt("roomID") * 100;
                System.out.println(actualDeviceId);
                dbList.add(actualDeviceId);
                //updating roomList before changing collection
                query = new BasicDBObject();
                BasicDBObject search = new BasicDBObject().append("roomID", fromServer.getInt("roomID"));
                query.append("$set", new BasicDBObject().append("deviceList", dbList));
                dbCollection.update(search, query);
                //removing some parts of the json
                fromServer.remove("token");
                fromServer.put("deviceID", actualDeviceId);
                dbCollection = databaseObj.getCollection("devices");
                System.out.println("Device added");

                dbCollection.insert(BasicDBObject.parse(fromServer.toString()));
                commandLog(fromServer, 7);

                return successJson.put("result", 1).put("deviceID", actualDeviceId);
            } else {
                System.out.println("Room not found");
                return failJson.put("result", 0).put("deviceID", (Collection<?>) null);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;

    }


    private JSONObject createNewHouse(JSONObject fromServer) {
        JSONObject successJson = new JSONObject();
        JSONObject failJson = new JSONObject();
        JSONObject testJson = new JSONObject();


        try {
            testJson.put("houseName", "bajs");
            testJson.put("housePassword", "123");
            testJson.put("username", "Bole");
            fromServer = testJson;

            dbCollection = databaseObj.getCollection("house");
            document = new BasicDBObject();
            document.put("houseName", fromServer.getString("houseName"));
            cursor = dbCollection.find(document);
            if (cursor.hasNext()) {
                failJson.put("result", 0);
                failJson.put("houseID", fromServer.getString("username"));
                System.out.println("HouseName already in use");
                int nextHouseId = getAllHouses().length();
                System.out.println(nextHouseId);
                commandLog(fromServer, 4);
                return failJson;
            }
            int nextHouseId = getAllHouses().length() + 1;
            System.out.println(nextHouseId);
            query = new BasicDBObject();
            query.put("houseName", fromServer.getString("houseName"));
            query.put("housePassword", fromServer.getString("housePassword"));
            query.put("roomList", new BasicDBList());
            query.put("username", new BasicDBList().add(fromServer.getString("username")));
            query.put("houseID", nextHouseId);
            dbCollection.insert(query);
            commandLog(fromServer, 4);
            successJson.put("result", 1);
            successJson.put("houseID", nextHouseId);
            return successJson;


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    private JSONObject createNewRoom(JSONObject fromServer) {
        JSONObject succesJson = new JSONObject();
        JSONObject failJson = new JSONObject();
        JSONObject testJson = new JSONObject();
        try {

            testJson.put("username", "Bole");
            testJson.put("token", "12345");
            testJson.put("houseID", "1");
            testJson.put("roomName", "Kitchen");
            fromServer = testJson;

            dbCollection = databaseObj.getCollection("house");
            document = new BasicDBObject();
            document.put("houseID", fromServer.getString("houseID"));

            cursor = dbCollection.find(document);

            if (!cursor.hasNext()) {
                failJson.put("result", 0);
                failJson.put("roomID", (Collection<?>) null);
                System.out.println("House not found... ");

                return failJson;
            }
            fetchedObject = cursor.next();
            BasicDBList myArr = (BasicDBList) fetchedObject.get("roomList");
            int houseIdAfterMulti = Integer.valueOf(fetchedObject.get("houseID").toString()) * 10; //to simply add the roomnumber to get the roomID
            int newRoomNumber = getAllRoomsInHouseCount(fetchedObject.get("houseID").toString()).length() + 1;
            System.out.println(myArr + "  hämtade listan");
            myArr.add(String.valueOf(newRoomNumber + houseIdAfterMulti));
            System.out.println(myArr + " Listan efter add");
            if (addRoomToDb(fromServer, newRoomNumber + houseIdAfterMulti)) {
                dbCollection = databaseObj.getCollection("house");
                query = new BasicDBObject();
                query.append("$set", new BasicDBObject().append("roomList", myArr));
                BasicDBObject search = new BasicDBObject().append("houseID", String.valueOf(houseIdAfterMulti / 10));
                dbCollection.update(search, query);

                System.out.println("Updated in db ");
                succesJson.put("result", 1);
                succesJson.put("houseID", houseIdAfterMulti / 10);
                return succesJson;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return succesJson;
    }

    private boolean addRoomToDb(JSONObject fromServer, int newRoomNumber) {

        try {


            dbCollection = databaseObj.getCollection("room");

            document = new BasicDBObject();

            cursor = dbCollection.find(document);
            if (cursor.hasNext()) {
                System.out.println("roomnumber already in database. Some logical error xD");
                return false;
                //HouseIDERROR
            }

            JSONArray jsonArr = new JSONArray();
            BasicDBList list = new BasicDBList();
            document.put("roomID", newRoomNumber);
            document.put("deviceList", list);
            document.put("roomName", fromServer.getString("roomName"));
            System.out.println("added in Room Document");
            dbCollection.insert(document);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;

    }

    private JSONArray getAllRoomsInHouseCount(String houseID) {
        dbCollection = databaseObj.getCollection("house");
        JSONArray arr = new JSONArray();
        document = new BasicDBObject();
        document.put("houseID", houseID);
        cursor = dbCollection.find(document);
        while (cursor.hasNext()) {

            BasicDBList dbList = (BasicDBList) cursor.next().get("roomList");
            for (Object s : dbList) {
                arr.put(s);

            }
        }
        System.out.println(arr.toString() + "toString xD ");
        return arr;
    }

    private JSONArray getAllHouses() {
        dbCollection = databaseObj.getCollection("house");
        JSONArray arr = new JSONArray();


        cursor = dbCollection.find();

        while (cursor.hasNext()) {
            arr.put(cursor.next());
        }
        System.out.println(arr.toString());
        return arr;
    }

    private void newRoom(String roomId) {
        //Update house array of rooms. If room already exist, nothing will happen.
        dbCollection = databaseObj.getCollection("house");
        document = new BasicDBObject();
        String houseId = roomId.substring(0, 1);
        document.put("id", houseId);
        cursor = dbCollection.find(document);
        fetchedObject = cursor.next();
        System.out.println(fetchedObject.toString());
        BasicDBList arr = (BasicDBList) fetchedObject.get("roomList");
        BasicDBList newArr = new BasicDBList();
        System.out.println(arr.size());
        for (int i = 0; i < arr.size(); i++) {
            System.out.println(arr.get(i));
            if (arr.get(i).equals(roomId)) {
                System.out.println("Room already in house list");

            } else {
                newArr.add(arr.get(i));

            }
        }

        newArr.add(roomId);
        System.out.println(newArr.toString().trim() + " efter adden");
        query = new BasicDBObject();
        BasicDBObject search = new BasicDBObject().append("id", houseId);
        query.append("$set", new BasicDBObject().append("roomList", newArr));
        System.out.println("Query.append " + query.toJson());

        dbCollection.update(search, query);

        document.clear();
        //Check if room exist in Room documents, if not add it
        dbCollection = databaseObj.getCollection("room");
        document.put("id", roomId);
        cursor = dbCollection.find(document);
        query = new BasicDBObject();
        if (!cursor.hasNext()) {
            query.put("id", roomId);
            query.put("deviceList", new BasicDBList());
            dbCollection.insert(query);

        } else {
            System.out.println("Room foundd, no need to create it");

        }


    }


    public String getDeviceStatus(Object id) {

        dbCollection = databaseObj.getCollection("devices");
        document = new BasicDBObject();
        document.put("id", "1234");
        cursor = dbCollection.find(document);
        fetchedObject = cursor.next();

        return fetchedObject.get("state").toString();

    }

    //TODO Gör klart json som ska skickas tillbaka
    public JSONObject getAllDevicesInRoom(JSONObject fromServer) {
        JSONObject jsonSuccess = new JSONObject();
        JSONObject jsonFail = new JSONObject();
        JSONObject jsonTest = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        BasicDBList dbList = new BasicDBList();
        JSONObject temp = new JSONObject();
        try {
            jsonTest.put("username", "Bole");
            jsonTest.put("token", "12345");
            jsonTest.put("roomID", 12);

            fromServer = jsonTest;

            dbCollection = databaseObj.getCollection("device");
            document = new BasicDBObject().append("roomID", fromServer.getInt("roomID"));
            cursor = dbCollection.find(document);

            while (cursor.hasNext()) {
                fetchedObject = cursor.next();
                temp.put("deviceName", fetchedObject.get("deviceName"));
                temp.put("deviceID", fetchedObject.get("deviceID"));
                temp.put("type", fetchedObject.get("type"));
                jsonArray.put(temp);
                //skapa lista att returna här
            }

            dbCollection = databaseObj.getCollection("room");
            document = new BasicDBObject();
            document.put("roomID", fromServer.getInt("roomID"));
            cursor = dbCollection.find(document);
            if (cursor.hasNext()) {
                jsonSuccess.put("deviceList", jsonArray);
                jsonSuccess.put("roomID", fromServer.getString("roomID"));
                jsonSuccess.put("roomName", cursor.next().get("roomName"));
                jsonSuccess.put("result", 1);
                return jsonSuccess;
            } else {
                jsonFail.put("result", 0);
                return jsonFail;

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    //Create user

    public JSONObject createUser(JSONObject jsonObject) {
        /*   JSONObject filipTest = new JSONObject();
           filipTest.put("firstName", "Filip");
           filipTest.put("lastName", "BenkanssonjAO");
           filipTest.put("password", "123456");
           filipTest.put("username", "Benka33");
           filipTest.put("userID", "5c37692c-360f-4022-a7db-23a45f828c1d");
           filipTest.put("email", "sm@somethingmore.com");
           jsonObject = filipTest.toString();  //"den som kommer från server"
*/
        JSONObject failJson = new JSONObject();
        JSONObject successJson = new JSONObject();
        try {


            dbCollection = databaseObj.getCollection("user");
            gson = new Gson();
            User user = gson.fromJson(jsonObject.toString(), User.class);
            String userEmail = user.getEmail();
            String username = user.getUsername();

            document = new BasicDBObject();
            document.put("email", userEmail);
            cursor = dbCollection.find(document);

            while (cursor.hasNext()) {
                fetchedObject = cursor.next();
                if (fetchedObject.toString().contains(userEmail)) {
                    failJson.put("result", 0);
                    failJson.put("requestType", jsonObject.getString("requestType"));
                    failJson.put("token", jsonObject.getString("token"));
                    return failJson;
                } else if (fetchedObject.toString().contains(username)) {

                    failJson.put("result", 0);
                    failJson.put("requestType", jsonObject.getString("requestType"));
                    failJson.put("token", jsonObject.getString("token"));
                    return failJson;
                } else if (fetchedObject.get("password").toString().length() < 3 || fetchedObject.get("password") == null) {
                    failJson.put("result", 0);
                    failJson.put("requestType", jsonObject.getString("requestType"));
                    failJson.put("token", jsonObject.getString("token"));
                    return failJson;

                }
                commandLog(failJson, 4);

            }

            query = new BasicDBObject();
            query.put("firstName", user.getFirstName());
            query.put("lastName", user.getLastName());
            query.put("password", user.getPassword());
            query.put("username", user.getUsername());
            query.put("userID", user.getUserId());
            query.put("email", user.getEmail());

            dbCollection.insert(query);

            successJson.put("token", jsonObject.getString("token"));
            successJson.put("requestType", jsonObject.getString("requestType"));
            successJson.put("result", 1);
            return successJson;
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;

    }

    //Find user
    public Object findUser(String id) {
        dbCollection = databaseObj.getCollection("user");

        document = new BasicDBObject();
        document.put("userID", id);
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
    public JSONObject loginMethod(JSONObject jsonObject) {

        String jsonString = jsonObject.toString();

        dbCollection = databaseObj.getCollection("user");
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
                    isAuthenticated = true;
                    result = 1;
                    return createResponse(jsonObject, result);
                } else {
                    isAuthenticated = false;
                    result = 0;
                    return createResponse(jsonObject, result);
                }
            } else {
                isAuthenticated = false;
                result = 0;
                createResponse(jsonObject, result);

                return createResponse(jsonObject, result);
            }
        }
        result = 0;

        return createResponse(jsonObject, result);

    }

    private JSONObject createResponse(JSONObject jsonString, int result) {

        JSONObject json = null;
        try {
            json = jsonString;
            json.put("username", json.get("username"));
            json.put("requestType", json.get("requestType"));
            json.put("result", result);
            //Server will add token??
            commandLog(json, 1);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;

    }


    public JSONArray getDevices () {

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        try {


            dbCollection = databaseObj.getCollection("devices");

            document = new BasicDBObject();
            cursor = dbCollection.find(document);

            while (cursor.hasNext()) {
                fetchedObject = cursor.next();
                jsonObject = JSONProtocol.getInstance().toJson(fetchedObject.toString());
                jsonArray.put(jsonObject);

            }

            return jsonArray;


        } catch (Exception e) {

            e.printStackTrace();

            return jsonArray;
        }
    }

}
