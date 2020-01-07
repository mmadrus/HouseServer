package rest.database;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import org.json.JSONObject;
import rest.models.Token;
import rest.models.User;
import rest.protocols.JSONProtocol;
import rest.protocols.TokenProtocol;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/*
The database class. For now we use a local database, you need to download and start MongoDB as a service, call it "HouseDatabase".
Use the correct port for your server.

To get the object id(mongoDB's object ID, looks like: "5dc199596d60d45f6409d791"), use the method "getObjectId", which returns the object ID.
The method uses "our" object id notation (e.g. 1234, as protocol states)


*/

@SuppressWarnings("DuplicatedCode")
public class Database  {

    private static final String URL = "ec2-13-53-175-23.eu-north-1.compute.amazonaws.com";
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

    private Cryption cryption = new Cryption();




    public static void main(String[] args) {
        Database database = Database.getInstance();

        database.getAllUsers();
    }

    private Database() {
        MongoClientURI uri = new MongoClientURI("mongodb://" + AUTH_USER + ":" + PASSWORD + "@" + URL + ":" + PORT_NUMBER + "/" + DATABASE);
        mongoClient = new MongoClient(uri);
        databaseObj = mongoClient.getDB("smart_house");
    }

    public static Database getInstance() {
        if (database == null) {

            database = new Database();
        }
        return database;


    }

    private void clearTables () {
        dbCollection = databaseObj.getCollection("device");
        dbCollection.drop();
        dbCollection = databaseObj.getCollection("rooms");
        dbCollection.drop();
        dbCollection = databaseObj.getCollection("houses");
        dbCollection.drop();
        dbCollection = databaseObj.getCollection("users");
        dbCollection.drop();
    }

    private void addDevices () {

        dbCollection = databaseObj.getCollection("devices");
        dbCollection.drop();
        dbCollection = databaseObj.getCollection("devices");

        document = new BasicDBObject();
        document.put("deviceID", 1101);
        document.put("status", 0);
        document.put("deviceName", "Indoor Lamp");
        document.put("type", "Lamp");
        document.put("flag", false);
        dbCollection.insert(document);

        document = new BasicDBObject();
        document.put("deviceID", 1102);
        document.put("status", 0);
        document.put("deviceName", "Outdoor Lamp");
        document.put("type", "Lamp");
        document.put("flag", false);
        dbCollection.insert(document);

        document = new BasicDBObject();
        document.put("deviceID", 11053);
        document.put("fahrenheit", 0);
        document.put("celsius", 0);
        document.put("status", 0);
        document.put("deviceName", "Indoor Temp");
        document.put("type", "Sensor");
        document.put("flag", false);
        dbCollection.insert(document);

        document = new BasicDBObject();
        document.put("deviceID", 11063);
        document.put("fahrenheit", 0);
        document.put("celsius", 0);
        document.put("status", 0);
        document.put("deviceName", "Outdoor Temp");
        document.put("type", "Sensor");
        document.put("flag", false);
        dbCollection.insert(document);

        document = new BasicDBObject();
        document.put("deviceID", 1109);
        document.put("status", 0);
        document.put("deviceName", "Water Leakage");
        document.put("type", "Alarm");
        document.put("flag", false);
        dbCollection.insert(document);

        document = new BasicDBObject();
        document.put("deviceID", 1112);
        document.put("status", 0);
        document.put("deviceName", "Stove");
        document.put("type", "Alarm");
        document.put("flag", false);
        dbCollection.insert(document);

        document = new BasicDBObject();
        document.put("deviceID", 1108);
        document.put("status", 0);
        document.put("deviceName", "Fire Alarm");
        document.put("type", "Alarm");
        document.put("flag", false);
        dbCollection.insert(document);

        document = new BasicDBObject();
        document.put("deviceID", 1111);
        document.put("status", 0);
        document.put("deviceName", "Window");
        document.put("type", "Alarm");
        document.put("flag", false);
        dbCollection.insert(document);

        document = new BasicDBObject();
        document.put("deviceID", 1103);
        document.put("status", 0);
        document.put("deviceName", "Radiator");
        document.put("type", "Device");
        document.put("flag", false);
        dbCollection.insert(document);

        document = new BasicDBObject();
        document.put("deviceID", 1104);
        document.put("status", 0);
        document.put("deviceName", "Fan");
        document.put("type", "Device");
        document.put("flag", false);
        dbCollection.insert(document);

        document = new BasicDBObject();
        document.put("deviceID", 11073);
        document.put("status", 0);
        document.put("deviceName", "Electric Consumption");
        document.put("type", "Sensor");
        document.put("flag", false);
        dbCollection.insert(document);

        document = new BasicDBObject();
        document.put("deviceID", 1113);
        document.put("status", 0);
        document.put("deviceName", "Power Cut");
        document.put("type", "Alarm");
        document.put("flag", false);
        dbCollection.insert(document);

        document = new BasicDBObject();
        document.put("deviceID", 1110);
        document.put("status", 0);
        document.put("deviceName", "Break-in Alarm");
        document.put("type", "Alarm");
        document.put("flag", false);
        dbCollection.insert(document);

        dbCollection = databaseObj.getCollection("rooms");
        document = new BasicDBObject();
        document.put("roomID", 11);
        cursor = dbCollection.find(document);

        if (cursor.hasNext()) {

            fetchedObject = cursor.next();

            BasicDBList dbList = (BasicDBList) fetchedObject.get("deviceList");

            if (dbList.size() <= 0) {
                dbList.add(1101);
                dbList.add(1102);
                dbList.add(11053);
                dbList.add(11063);
                dbList.add(1109);
                dbList.add(1112);
                dbList.add(1108);
                dbList.add(1111);
                dbList.add(1103);
                dbList.add(1104);
                dbList.add(11073);
                dbList.add(1113);
                dbList.add(1110);
            }

            //updating roomList before changing collection
            query = new BasicDBObject();

            BasicDBObject search = new BasicDBObject().append("roomID", 11);

            query.append("$set", new BasicDBObject().append("deviceList", dbList));
            dbCollection.update(search, query);
        }
    }

    public void commandLog(JSONObject jsonObject) {

        dbCollection = databaseObj.getCollection("logs");
        document = new BasicDBObject();

        try {

            document.put("Date", new Date());
            document.put("User-command", jsonObject.toString());
            dbCollection.insert(document);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public JSONObject changeDeviceState (JSONObject object) {

        try {

            dbCollection = databaseObj.getCollection("devices");
            document = new BasicDBObject();
            document.append("$set", new BasicDBObject().append("status", object.getInt("command")));

            BasicDBObject search = new BasicDBObject().append("deviceID", object.getInt("deviceID"));

            dbCollection.update(search, document);
            return new JSONObject().put("result", 1);

        } catch (Exception e) {

            e.printStackTrace();
            return new JSONObject().put("result", 0);
        }
    }*/

    //PUT/COMMAND.docx
    private JSONObject changeState(JSONObject fromServer) {

        try {
            JSONObject jsonToStoreInDB = new JSONObject();

            String deviceId = fromServer.getString("deviceID");
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
            jsonToStoreInDB.put("deviceID", deviceId);
            jsonToStoreInDB.put("command", fromServer.getString("command"));

            dbCollection = databaseObj.getCollection("devices");
            document = new BasicDBObject();
            document.put("deviceID", deviceId);
            cursor = dbCollection.find(document);
            while (cursor.hasNext()) {

                if (!doesEntityExist(deviceId, "devices", jsonToStoreInDB)) {
                    JSONObject errorJson = new JSONObject();

                    errorJson.put("deviceId", fromServer.getString("deviceId"));
                    errorJson.put("result", 0);
                    return errorJson;
                }

            }

            query = new BasicDBObject();
            query.append("$set", new BasicDBObject().append("status", fromServer.getInt("command")));
            BasicDBObject search = new BasicDBObject().append("deviceID", deviceId);
            dbCollection.update(search, query);

            JSONObject successJson = new JSONObject();
            successJson.put("deviceId", fromServer.getString("deviceId"));
            successJson.put("result", 1);

            return successJson;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    } //Unit o server (command protocol)

    //PUT/ADD ACCESS TO HOUSE.docx
    public JSONObject addAccessToHouse(JSONObject fromServer) {

        JSONObject successJson = new JSONObject();
        JSONObject failJson = new JSONObject();

        try {

            dbCollection = databaseObj.getCollection("houses");
            document = new BasicDBObject();
            document.put("houseID", fromServer.getInt("houseID"));

            cursor = dbCollection.find(document);

            if (!cursor.hasNext()) {

                failJson.put("result", 0);
                return failJson;

            } else {

                fetchedObject = cursor.next();
                JSONObject house = JSONProtocol.getInstance().toJson(fetchedObject.toString());

                if (fetchedObject.get("housePassword").equals(fromServer.getString("housePassword"))) {

                    BasicDBList list = (BasicDBList) fetchedObject.get("usernames");
                    list.add(fromServer.getString("username"));

                    query = new BasicDBObject();
                    query.append("$set", new BasicDBObject().append("usernames", list));
                    BasicDBObject search = new BasicDBObject().append("houseID", fromServer.getInt("houseID"));
                    dbCollection.update(search, query);

                    addHouseToUser(house.put("username", fromServer.getString("username")), fromServer.getInt("houseID"));

                    successJson.put("result", 1);
                    return successJson;

                } else {

                    failJson.put("result", 0);
                    return failJson;
                }
            }


        } catch (Exception e) {

            e.printStackTrace();
            failJson.put("result", 0);
            return failJson;
        }

    }

    //PUT/Login.docx
    public JSONObject loginMethod(JSONObject jsonObject) {

        String jsonString = jsonObject.toString();

        dbCollection = databaseObj.getCollection("users");
        document = new BasicDBObject();
        gson = new Gson();
        User user = gson.fromJson(jsonString, User.class);
        document.put("username", jsonObject.getString("username"));
        //document.put("password", jsonObject.getString("password"));

        cursor = dbCollection.find(document);

        String temp = "";

        while (cursor.hasNext()) {

            fetchedObject = cursor.next();
            if (fetchedObject.get("username").equals(user.getUsername())) {
                temp = cryption.cryption((String) fetchedObject.get("password"),2);
                if (temp.equals(user.getPassword())) {
                    Token token = new Token();
                    TokenProtocol.getInstance().addToken(token);
                    return new JSONObject().put("result", 1).put("token", token.getToken());
                }
            }
        }

        return new JSONObject().put("result", 0);
    }

    private boolean doesEntityExist(String id, String entity, JSONObject jsonString) {
        document = new BasicDBObject();
        int idInt = Integer.valueOf(id);
        switch (entity) {
            case "houses":
                dbCollection = databaseObj.getCollection(entity);
                document.put("houseID", idInt);
                cursor = dbCollection.find(document);
                if (!cursor.hasNext()) {
                    System.out.println("House not found");
                    return false;

                } else {
                    System.out.println("House found!");
                    return true;
                }
            case "rooms":
                dbCollection = databaseObj.getCollection(entity);
                document.put("roomID", idInt);
                cursor = dbCollection.find(document);
                if (!cursor.hasNext()) {
                    System.out.println("Room not found");

                    return false;
                } else {
                    System.out.println("Room found");
                    return true;
                }

            case "devices":
                dbCollection = databaseObj.getCollection(entity);
                document.put("deviceID", idInt);
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

    //POST/create device.docx
    public JSONObject createNewDevice(JSONObject fromServer) {

        JSONObject successJson = new JSONObject();
        JSONObject failJson = new JSONObject();
        JSONArray arr = new JSONArray();

        try {

            int actualDeviceId;
            int nextDeviceId;

            dbCollection = databaseObj.getCollection("rooms");
            document = new BasicDBObject();
            document.put("roomID", fromServer.getInt("roomID"));
            cursor = dbCollection.find(document);

            if (cursor.hasNext()) {

                fetchedObject = cursor.next();

                BasicDBList dbList = (BasicDBList) fetchedObject.get("deviceList");
                nextDeviceId = dbList.size() + 1;

                actualDeviceId = nextDeviceId + fromServer.getInt("roomID") * 100;
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
                //commandLog(fromServer, 7);

                return successJson.put("result", 1).put("deviceID", actualDeviceId);

            } else {

                System.out.println("Room not found");
                return failJson.put("result", 0).put("deviceID", 0);

            }


        } catch (Exception e) {
            e.printStackTrace();
            return failJson.put("result", 0).put("deviceID", 0);
        }

    }

    // POST/create house.docx
    public JSONObject createNewHouse(JSONObject fromServer) {

        JSONObject successJson = new JSONObject();
        JSONObject failJson = new JSONObject();

        try {

            dbCollection = databaseObj.getCollection("houses");
            document = new BasicDBObject();

            document.put("houseName", fromServer.getString("houseName"));

            cursor = dbCollection.find(document);

            int nextHouseId = getAllHouses().length() + 1;
            System.out.println(nextHouseId);

            BasicDBList list = new BasicDBList();
            list.add(fromServer.getString("username"));

            query = new BasicDBObject();
            query.put("houseName", fromServer.getString("houseName"));
            query.put("housePassword", fromServer.getString("housePassword"));
            query.put("roomList", new BasicDBList());
            query.put("usernames", list);
            query.put("houseID", nextHouseId);
            dbCollection.insert(query);

            addHouseToUser(fromServer, nextHouseId);

            successJson.put("result", 1);
            successJson.put("houseID", nextHouseId);

            return successJson;


        } catch (Exception e) {

            e.printStackTrace();
            failJson.put("database", "fail");
            return failJson.put("result", 0);
        }

    }

    //POST/CREATEROOM.docx
    public JSONObject createNewRoom (JSONObject fromServer) {

        JSONObject succesJson = new JSONObject();
        JSONObject failJson = new JSONObject();

        try {

            dbCollection = databaseObj.getCollection("houses");

            document = new BasicDBObject();
            document.put("houseID", fromServer.getInt("houseID"));

            cursor = dbCollection.find(document);

            if (!cursor.hasNext()) {
                failJson.put("result", 0);
                failJson.put("roomID", 0);
                System.out.println("House not found... ");

                return failJson;
            }

            fetchedObject = cursor.next();

            BasicDBList myArr = (BasicDBList) fetchedObject.get("roomList");

            int houseIdAfterMulti = Integer.parseInt(fetchedObject.get("houseID").toString()) * 10; //to simply add the roomnumber to get the roomID
            int newRoomNumber = getAllRoomsInHouseCount(fetchedObject.get("houseID").toString()).length() + 1;
            myArr.add((newRoomNumber + houseIdAfterMulti));

            if (addRoomToDb(fromServer, newRoomNumber + houseIdAfterMulti)) {

                dbCollection = databaseObj.getCollection("houses");

                query = new BasicDBObject();
                query.append("$set", new BasicDBObject().append("roomList", myArr));

                BasicDBObject search = new BasicDBObject().append("houseID", houseIdAfterMulti / 10);
                dbCollection.update(search, query);

                succesJson.put("result", 1);
                succesJson.put("houseID", fetchedObject.get("houseID"));

            } else {

                failJson.put("result", 0);
                return failJson;
            }

            return succesJson;

        } catch (Exception e) {
            e.printStackTrace();
            failJson.put("result", 0);
            return failJson;
        }

    }

    //POST/Create user.docx
    public JSONObject createUser(JSONObject jsonObject) {


        JSONObject failJson = new JSONObject();
        JSONObject successJson = new JSONObject();
        JSONObject commandJson = new JSONObject();

        try {

            dbCollection = databaseObj.getCollection("users");
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
                    commandJson.put("fail", userEmail);
                    return failJson;

                } else if (fetchedObject.toString().contains(username)) {

                    failJson.put("result", 0);
                    commandJson.put("fail", username);
                    return failJson;

                } else if (fetchedObject.get("password").toString().length() < 3 || fetchedObject.get("password") == null) {

                    failJson.put("result", 0);
                    commandJson.put("fail", "password");
                    return failJson;

                }
            }

            //String temp = cryption(user.getPassword(), 1);

            BasicDBList list = new BasicDBList();

            query = new BasicDBObject();
            query.put("firstName", user.getFirstName());
            query.put("lastName", user.getLastName());
            query.put("password", cryption.cryption(user.getPassword(), 1));
            query.put("username", user.getUsername());
            query.put("userID", user.getUserId());
            query.put("email", user.getEmail());
            query.put("houseList", list);

            dbCollection.insert(query);

            successJson.put("result", 1);

            return successJson;

        } catch (Exception ex) {

            ex.printStackTrace();
            return new JSONObject().put("result", 0);
        }
    }

    private boolean addRoomToDb(JSONObject fromServer, int newRoomNumber) {

        try {


            dbCollection = databaseObj.getCollection("rooms");

            document = new BasicDBObject();
            document.put("roomID", newRoomNumber);
            cursor = dbCollection.find(document);

            if (cursor.hasNext()) {

                return false;
            }

            JSONArray jsonArr = new JSONArray();
            BasicDBList list = new BasicDBList();
            document.put("roomID", newRoomNumber);
            document.put("deviceList", list);
            document.put("roomName", fromServer.getString("roomName"));
            document.put("houseID", fromServer.getInt("houseID"));
            System.out.println("added in Room Document");
            dbCollection.insert(document);
            return true;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }

    }

    private JSONArray getAllRoomsInHouseCount(String houseID) {

        dbCollection = databaseObj.getCollection("rooms");
        JSONArray arr = new JSONArray();
        JSONObject json = new JSONObject();

        document = new BasicDBObject();
        int houseIdInt = Integer.parseInt(houseID);
        document.put("houseID", houseIdInt);

        cursor = dbCollection.find(document);

        while (cursor.hasNext()) {

            try {
                json = new JSONObject();
                fetchedObject = cursor.next();

                json.put("roomName", fetchedObject.get("roomName"));
                json.put("roomID", fetchedObject.get("roomID"));

                arr.put(json);

            } catch (Exception e) {

                e.printStackTrace();
                return null;
            }

        }

        return arr;
    }

    private JSONArray getAllHouses() {

        dbCollection = databaseObj.getCollection("houses");
        JSONArray arr = new JSONArray();
        JSONObject jsonObject = new JSONObject();


        cursor = dbCollection.find();

        while (cursor.hasNext()) {
            fetchedObject = cursor.next();
            jsonObject = JSONProtocol.getInstance().toJson(fetchedObject.toString());
            jsonObject.remove("usernames");
            jsonObject.remove("_id");
            arr.put(jsonObject);
        }
        System.out.println(arr.toString());
        return arr;
    }

    //Find user
    public Object findUser(String id) {

        dbCollection = databaseObj.getCollection("users");

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
        dbCollection = databaseObj.getCollection("users");

        cursor = dbCollection.find();
        while (cursor.hasNext()) {
            fetchedObject = cursor.next();
            allUsers.add(fetchedObject);
        }


        return allUsers;
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
                jsonObject.remove("_id");
                jsonArray.put(jsonObject);

            }

            return jsonArray;


        } catch (Exception e) {

            e.printStackTrace();

            return jsonArray;
        }
    }

    private void addHouseToUser (JSONObject jsonObject, int houseId) {

        try {

            dbCollection = databaseObj.getCollection("users");

            document = new BasicDBObject();
            document.put("username", jsonObject.getString("username"));
            cursor = dbCollection.find(document);
            JSONObject temp = new JSONObject();
            String hID = String.valueOf(houseId);

            while (cursor.hasNext()) {

                fetchedObject = cursor.next();
                temp = JSONProtocol.getInstance().toJson(fetchedObject.toString());
                if (temp.getString("username").equals(jsonObject.getString("username"))) {

                    BasicDBList list = (BasicDBList) fetchedObject.get("houseList");
                    list.add(hID + ":" + jsonObject.getString("houseName"));

                    query = new BasicDBObject();
                    query.append("$set", new BasicDBObject().append("houseList", list));
                    BasicDBObject search = new BasicDBObject().append("username", jsonObject.getString("username"));
                    dbCollection.update(search, query);

                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public JSONObject getSpecificHouse (JSONObject jsonObject) {

        try {

            dbCollection = databaseObj.getCollection("users");

            JSONObject arrayObj = null;

            document = new BasicDBObject();
            document.put("username", jsonObject.getString("username"));
            cursor = dbCollection.find(document);

            if (!cursor.hasNext()) {
                return null;
            }

            fetchedObject = cursor.next();

            BasicDBList list = (BasicDBList) fetchedObject.get("houseList");
            DBCollection tempCollection = databaseObj.getCollection("houses");
            Cursor tempC = null;
            DBObject tempFetched = null;
            int tempInt;
            BasicDBObject tempObj = null;
            JSONObject tempArrayObj = null;
            JSONObject temp = null;
            String tempeStr = "";
            String [] strArray;


            for (int i = 0; i < list.size(); i++) {


                tempObj = new BasicDBObject();
                tempeStr = String.valueOf(list.get(i));
                strArray = tempeStr.split(":");

                System.out.println(strArray);
                if (jsonObject.getString("houseName").equals(strArray[1])) {

                    tempInt = Integer.parseInt(strArray[0]);
                    System.out.println(tempInt);
                    tempObj.put("houseID", tempInt);

                    tempC = tempCollection.find(tempObj);

                    tempFetched = tempC.next();

                    tempArrayObj = JSONProtocol.getInstance().toJson(tempFetched.toString());

                    System.out.println(tempArrayObj.toString());

                    arrayObj = new JSONObject();
                    arrayObj.put("houseName", tempArrayObj.getString("houseName"));
                    arrayObj.put("houseID", tempArrayObj.get("houseID"));
                    temp = getSpecificHousesRoom(arrayObj);
                    arrayObj.put("listOfRooms", temp.getJSONArray("listOfRooms"));

                    return arrayObj;
                }

            }

            return arrayObj.put("result", 0).put("fail", "db1");

        } catch (Exception e) {

            e.printStackTrace();
            return new JSONObject().put("result", 0).put("fail", "db2");
        }
    }

    public JSONObject getSpecificHousesRoom (JSONObject object) {
        try {

            dbCollection = databaseObj.getCollection("houses");

            JSONArray jsonArray = new JSONArray();
            JSONObject arrayObj = null;
            JSONObject finalObject = new JSONObject();

            document = new BasicDBObject();
            document.put("houseID", object.getInt("houseID"));
            cursor = dbCollection.find(document);

            if (!cursor.hasNext()) {
                return null;
            }

            fetchedObject = cursor.next();

            finalObject.put("houseName", fetchedObject.get("houseName"));
            finalObject.put("houseID", object.getInt("houseID"));

            BasicDBList list = (BasicDBList) fetchedObject.get("roomList");
            DBCollection tempCollection = databaseObj.getCollection("rooms");
            Cursor tempC = null;
            DBObject tempFetched = null;
            int tempInt;
            BasicDBObject tempObj = null;
            JSONObject tempArrayObj = null;
            JSONObject temp = null;

            System.out.println(list.toString());

            for (int i = 0; i < list.size(); i++) {
                tempObj = new BasicDBObject();
                tempInt = (int) list.get(i);

                tempObj.put("roomID", tempInt);

                tempC = tempCollection.find(tempObj);

                tempFetched = tempC.next();

                tempArrayObj = JSONProtocol.getInstance().toJson(tempFetched.toString());

                arrayObj = new JSONObject();
                arrayObj.put("roomName", tempArrayObj.getString("roomName"));
                arrayObj.put("roomID", tempArrayObj.getInt("roomID"));

                jsonArray.put(arrayObj);
            }

            finalObject.put("listOfRooms", jsonArray);

            return finalObject;

        } catch (Exception e) {

            e.printStackTrace();
            return new JSONObject().put("result", 0);
        }
    }

    public JSONArray getUserHouses (JSONObject object) {

        try {

            dbCollection = databaseObj.getCollection("users");

            JSONArray jsonArray = new JSONArray();
            JSONObject arrayObj = null;

            document = new BasicDBObject();
            document.put("username", object.getString("username"));
            cursor = dbCollection.find(document);

            if (!cursor.hasNext()) {
                return null;
            }

            fetchedObject = cursor.next();

            BasicDBList list = (BasicDBList) fetchedObject.get("houseList");
            DBCollection tempCollection = databaseObj.getCollection("houses");
            Cursor tempC = null;
            DBObject tempFetched = null;
            int tempInt;
            BasicDBObject tempObj = null;
            JSONObject tempArrayObj = null;
            JSONObject temp = null;
            String tempeStr = "";
            String [] strArray;

            for (int i = 0; i < list.size(); i++) {
                tempObj = new BasicDBObject();
                tempeStr = String.valueOf(list.get(i));
                strArray = tempeStr.split(":");
                tempInt = Integer.parseInt(strArray[0]);
                System.out.println(tempInt);
                tempObj.put("houseID", tempInt);

                tempC = tempCollection.find(tempObj);

                tempFetched = tempC.next();

                tempArrayObj = JSONProtocol.getInstance().toJson(tempFetched.toString());

                arrayObj = new JSONObject();
                arrayObj.put("houseName", tempArrayObj.getString("houseName"));
                arrayObj.put("houseID", tempArrayObj.get("houseID"));
                arrayObj.put("housePassword", tempArrayObj.getString("housePassword"));
                temp = getHouseRooms(arrayObj);
                arrayObj.put("listOfRooms", temp.getJSONArray("listOfRooms"));

                jsonArray.put(arrayObj);
            }

            return jsonArray;

        } catch (Exception e) {

            e.printStackTrace();
            return new JSONArray().put(new JSONObject().put("result", 0));
        }
    }

    public JSONObject getHouseRooms (JSONObject object) {

        try {

            dbCollection = databaseObj.getCollection("houses");

            JSONArray jsonArray = new JSONArray();
            JSONObject arrayObj = null;
            JSONObject finalObject = new JSONObject();

            document = new BasicDBObject();
            document.put("houseID", object.getInt("houseID"));
            cursor = dbCollection.find(document);

            if (!cursor.hasNext()) {
                return null;
            }

            fetchedObject = cursor.next();

            finalObject.put("houseName", fetchedObject.get("houseName"));
            finalObject.put("houseID", object.getInt("houseID"));

            BasicDBList list = (BasicDBList) fetchedObject.get("roomList");
            DBCollection tempCollection = databaseObj.getCollection("rooms");
            Cursor tempC = null;
            DBObject tempFetched = null;
            int tempInt;
            BasicDBObject tempObj = null;
            JSONObject tempArrayObj = null;
            JSONObject temp = null;

            System.out.println(list.toString());

            for (int i = 0; i < list.size(); i++) {
                tempObj = new BasicDBObject();
                tempInt = (int) list.get(i);

                tempObj.put("roomID", tempInt);

                tempC = tempCollection.find(tempObj);

                tempFetched = tempC.next();

                tempArrayObj = JSONProtocol.getInstance().toJson(tempFetched.toString());

                arrayObj = new JSONObject();
                arrayObj.put("roomName", tempArrayObj.getString("roomName"));
                arrayObj.put("roomID", tempArrayObj.getInt("roomID"));
                temp = getRoomDevices(arrayObj);
                arrayObj.put("listOfDevices", temp.getJSONArray("listOfDevices"));

                jsonArray.put(arrayObj);
            }

            finalObject.put("listOfRooms", jsonArray);

            return finalObject;

        } catch (Exception e) {

            e.printStackTrace();
            return new JSONObject().put("result", 0);
        }

    }

    public JSONObject getRoomDevices (JSONObject object) {

        try {

            dbCollection = databaseObj.getCollection("rooms");

            JSONArray jsonArray = new JSONArray();
            JSONObject arrayObj = null;
            JSONObject finalObject = new JSONObject();

            document = new BasicDBObject();
            document.put("roomID", object.getInt("roomID"));
            cursor = dbCollection.find(document);

            if (!cursor.hasNext()) {
                return null;
            }

            fetchedObject = cursor.next();

            finalObject.put("roomName", fetchedObject.get("roomName"));
            finalObject.put("roomID", object.getInt("roomID"));

            BasicDBList list = (BasicDBList) fetchedObject.get("deviceList");
            DBCollection tempCollection = databaseObj.getCollection("devices");
            Cursor tempC = null;
            DBObject tempFetched = null;
            int tempInt;
            BasicDBObject tempObj = null;
            JSONObject tempArrayObj = null;

            System.out.println(list.toString());

            for (int i = 0; i < list.size(); i++) {
                tempObj = new BasicDBObject();
                tempInt = (int) list.get(i);

                tempObj.put("deviceID", tempInt);

                tempC = tempCollection.find(tempObj);

                try {

                    tempFetched = tempC.next();

                    tempArrayObj = JSONProtocol.getInstance().toJson(tempFetched.toString());

                    arrayObj = new JSONObject();
                    arrayObj.put("deviceName", tempArrayObj.getString("deviceName"));
                    arrayObj.put("deviceID", tempArrayObj.getInt("deviceID"));
                    arrayObj.put("status", tempArrayObj.getInt("status"));
                    arrayObj.put("type", tempArrayObj.getString("type"));


                    jsonArray.put(arrayObj);

                } catch (NoSuchElementException exe) {
                    exe.getSuppressed();
                }
            }

            finalObject.put("listOfDevices", jsonArray);

            return finalObject;

        } catch (Exception e) {

            e.printStackTrace();
            return new JSONObject().put("result", 0);
        }

    }

    public JSONArray getDeviceUpdate () {

        try {

            dbCollection = databaseObj.getCollection("devices");

            JSONArray jsonArray = new JSONArray();
            JSONObject arrayObj = null;

            cursor = dbCollection.find();

            while (cursor.hasNext()) {

                fetchedObject = cursor.next();

                if (!fetchedObject.get("type").equals("Sensor") && !fetchedObject.get("type").equals("Alarm")) {
                    arrayObj = new JSONObject();
                    arrayObj.put("deviceID", fetchedObject.get("deviceID"));
                    arrayObj.put("deviceName", fetchedObject.get("deviceName"));
                    arrayObj.put("status", fetchedObject.get("status"));
                    jsonArray.put(arrayObj);

                }
            }

            return jsonArray;

        } catch (Exception e) {

            e.printStackTrace();
            return new JSONArray().put(new JSONObject().put("result", 0));
        }
    }

    public JSONArray getAlarmUpdate () {

        try {

            dbCollection = databaseObj.getCollection("devices");

            JSONArray jsonArray = new JSONArray();
            JSONObject arrayObj = null;

            cursor = dbCollection.find();

            while (cursor.hasNext()) {

                fetchedObject = cursor.next();

                if (fetchedObject.get("type").equals("Alarm")) {
                    arrayObj = new JSONObject();
                    arrayObj.put("deviceID", fetchedObject.get("deviceID"));
                    arrayObj.put("deviceName", fetchedObject.get("deviceName"));
                    arrayObj.put("status", fetchedObject.get("status"));
                    jsonArray.put(arrayObj);

                }
            }

            return jsonArray;

        } catch (Exception e) {

            e.printStackTrace();
            return new JSONArray().put(new JSONObject().put("result", 0));
        }
    }

    public JSONObject getSensorUpdate () {

        try {

            dbCollection = databaseObj.getCollection("devices");

            JSONObject finalObject = new JSONObject();
            JSONObject arrayObj = null;

            cursor = dbCollection.find();

            while (cursor.hasNext()) {

                fetchedObject = cursor.next();

                if (fetchedObject.get("type").equals("Sensor")) {
                    /*arrayObj = new JSONObject();
                    arrayObj.put("deviceID", fetchedObject.get("deviceID"));
                    arrayObj.put("deviceName", fetchedObject.get("deviceName"));
                    arrayObj.put("fahrenheit", fetchedObject.get("fahrenheit"));
                    arrayObj.put("celsius", fetchedObject.get("celsius"));
                    arrayObj.put("status", fetchedObject.get("status"));*/
                    switch ((int) fetchedObject.get("deviceID")) {

                        case 11053:
                            finalObject.put("internalTemp", fetchedObject.get("status"));
                            break;

                        case 11063:
                            finalObject.put("externalTemp", fetchedObject.get("status"));
                            break;

                        case  11073:
                            finalObject.put("electricalConsumption", fetchedObject.get("status"));
                            break;
                    }

                }
            }

            return finalObject;

        } catch (Exception e) {

            e.printStackTrace();
            return new JSONObject().put("result", 0);
        }
    }

    public JSONObject deleteDevice (JSONObject jsonObject) {

        try {

            dbCollection = databaseObj.getCollection("devices");

            cursor = dbCollection.find();
            int id;
            int temp;

            while (cursor.hasNext()) {

                fetchedObject = cursor.next();
                temp = (int) fetchedObject.get("deviceID");
                if (temp == jsonObject.getInt("deviceID")) {
                    dbCollection.remove(fetchedObject);
                    break;

                }
            }
            return new JSONObject().put("result", 1);

        } catch (Exception e) {

            e.printStackTrace();
            return new JSONObject().put("result", 0);
        }
    }

    public JSONObject updateDeviceStatus (JSONObject jsonObject) {

        try {

            dbCollection = databaseObj.getCollection("devices");

            document = new BasicDBObject();
            //document.put("deviceID", jsonObject.getInt("deviceID"));
            cursor = dbCollection.find(document);
            JSONObject temp = new JSONObject();

            while (cursor.hasNext()) {

                fetchedObject = cursor.next();
                System.out.println(fetchedObject.get("deviceID"));
                System.out.println(jsonObject.getInt("deviceID"));
                temp = JSONProtocol.getInstance().toJson(fetchedObject.toString());

                if (temp.getInt("deviceID") == jsonObject.getInt("deviceID")) {

                    query = new BasicDBObject();
                    query.append("$set", new BasicDBObject().append("status", jsonObject.getInt("command")));
                    BasicDBObject search = new BasicDBObject().append("deviceID", jsonObject.getInt("deviceID"));
                    dbCollection.update(search, query);

                    return new JSONObject().put("result", 1);
                }
            }

            return new JSONObject().put("result", 0);

        } catch (Exception e) {

            e.printStackTrace();
            return new JSONObject().put("result", 0);
        }
    }

    public JSONObject updateTempDevice (JSONObject jsonObject) {

        try {

            dbCollection = databaseObj.getCollection("devices");

            document = new BasicDBObject();
            document.put("deviceID", jsonObject.getInt("deviceID"));
            cursor = dbCollection.find(document);

            while (cursor.hasNext()) {

                fetchedObject = cursor.next();

                if (fetchedObject.get("deviceID").equals(jsonObject.getInt("deviceID"))) {

                    query = new BasicDBObject();
                    query.append("$set", new BasicDBObject().append("celsius", jsonObject.getInt("celsius")));
                    query.append("$set", new BasicDBObject().append("fahrenheit", jsonObject.getInt("fahrenheit")));
                    BasicDBObject search = new BasicDBObject().append("deviceID", jsonObject.getInt("deviceID"));
                    dbCollection.update(search, query);

                    return new JSONObject().put("result", 1);
                }
            }

            return new JSONObject().put("result", 0);

        } catch (Exception e) {

            e.printStackTrace();
            return new JSONObject().put("result", 0);
        }
    }

    private class Cryption {

    /*
    The encryptor/decryptor class.
    You call the method 'cryption' with parameters String and mode.
    Encrypt the String = Mode 1  (Cipher.ENCRYPT_MODE, kan också använda)
    Decrypt the String = Mode 2 (Cipher.DECRYPT.MODE, kan också använda)


     */

        private String salt = "ssshhhhhhhhhhh!!!!";



        public String cryption(String strToCrypt, int mode) {
            String string = "";

            try {

                IvParameterSpec iv = new IvParameterSpec(getIv());
                SecretKeySpec sKeySpec = new SecretKeySpec(generateKeyFromString(), "AES");
                Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
                cipher.init(mode, sKeySpec, iv);
//            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));

                switch (mode) {

                    case 1:
                        string = Base64.getEncoder().encodeToString(cipher.doFinal(strToCrypt.getBytes("UTF-8")));
                        break;

                    case 2:
                        string = new String(cipher.doFinal(Base64.getDecoder().decode(strToCrypt)));

                        break;

                }
                System.out.println("Mode: " + mode);
                System.out.println(string);
                return string;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }


        private byte[] generateKeyFromString() {

            return generateHash(salt).substring(0, 16).getBytes();
        }

        private byte[] getIv() {

            return new byte[]{0x04, 0x02, 0x0f, 0x0a,
                    0x08, 0x08, 0x05, 0x0c,
                    0x03, 0x01, 0x0e, 0x0f,
                    0x08, 0x08, 0x05, 0x0c};

        }

        public String generateHash(String PasswordToHash) {
            String generatedPasswordHash = null;
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(PasswordToHash.getBytes());
                byte[] bytes = md.digest();
                StringBuilder sb = new StringBuilder();

                for (byte aByte : bytes) {
                    sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
                }
                generatedPasswordHash = sb.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return generatedPasswordHash;
        }

    }
}
