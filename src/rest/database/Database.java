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
        //    getInstance().unitToServer(new JSONObject());
        System.out.println(getInstance().getRoom(new JSONObject()));

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

    /*
    commandType 1 == login
    commandType 2 == deviceLog
    commandType 3 == trying to get access to house
    commandType 4 == create User
    commandType 5 == create new house log
    commandType 6 ==
    commandType 7 == create new Device
    commandType 9 ==
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

                    dbCollection = databaseObj.getCollection("deviceLog");
                    document = new BasicDBObject();

                    document.put("userID", jsonObject.getString("userID"));
                    document.put("command", jsonObject.getString("command"));
                    System.out.println("Device log added");
                    dbCollection.insert(document);
                    break;
                case 3:

                    document.put("username", jsonObject.getString("username"));
                    document.put("token", jsonObject.getString("token"));
                    document.put("date", new Date());
                    document.put("houseID", jsonObject.getString("houseID"));


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
                    dbCollection = databaseObj.getCollection("deviceLog");
                    document = new BasicDBObject();
                    document.put("requestType", "createDevice");
                    document.put("username", jsonObject.getString("username"));
                    document.put("roomID", jsonObject.getString("roomID"));


                    break;
                case 8:
                    break;
                case 9:
                    dbCollection = databaseObj.getCollection("logs");
                    document = new BasicDBObject();


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

    //PUT/COMMAND.docx
    private JSONObject changeState(JSONObject fromServer) {

        try {
            JSONObject jsonToStoreInDB = new JSONObject();
/*
            JSONObject json = new JSONObject();

            json.put("token", "123455");
            json.put("requestType", "changeLight");
            json.put("userID", "1");
            json.put("deviceId", "1234");
            json.put("command", 0);
            fromServer = json;

*/
            String deviceId = fromServer.getString("deviceId");
            try {
                String houseId = deviceId.substring(0, 1);
                String roomId = deviceId.substring(0, 2);
                System.out.println(roomId);
                boolean houseExist = doesEntityExist(houseId, "house", fromServer);
                boolean roomExist = doesEntityExist(roomId, "room", fromServer);

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

    //PUT/ADD ACCESS TO HOUSE.docx
    private JSONObject addAccessToHouse(JSONObject fromServer) {
        JSONObject successJson = new JSONObject();
        JSONObject failJson = new JSONObject();
        JSONObject testJson = new JSONObject();
        try {

            testJson.put("username", "Isak");
            testJson.put("token", "12345555");
            testJson.put("houseID", 1);
            testJson.put("housePassword", "123");


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

                    BasicDBList list = (BasicDBList) fetchedObject.get("usernames");
                    list.add(fromServer.getString("username"));

                    query = new BasicDBObject();
                    query.append("$set", new BasicDBObject().append("usernames", list));
                    BasicDBObject search = new BasicDBObject().append("houseID", fromServer.getInt("houseID"));
                    dbCollection.update(search, query);
                    System.out.println("User added and username added to list");
                    successJson.put("result", 1);
                    return successJson;

                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return successJson;
    }

    //PUT/Login.docx
    public JSONObject loginMethod(JSONObject jsonObject) {

        String jsonString = jsonObject.toString();
/*
        JSONObject json = new JSONObject();
        try {
            json.put("username", "IsakZ");
            json.put("password", "123");
            json.put("requestType", "LoginMethod");

            jsonString = json.toString();
            jsonObject = json;
        } catch (JSONException e) {
            e.printStackTrace();
        }

*/
        dbCollection = databaseObj.getCollection("User");
        document = new BasicDBObject();
        gson = new Gson();
        User user = gson.fromJson(jsonString, User.class);
        document.put("username", user.getusername());
        document.put("password", user.getPassword());
        System.out.println(user.getusername());
        System.out.println(user.getPassword());
        cursor = dbCollection.find(document);
        //            dbCollection.insert(BasicDBObject.parse(String.valueOf(toAdd)));


        int result = -1;
        while (cursor.hasNext()) {
            fetchedObject = cursor.next();
            if (fetchedObject.toString().contains(user.getusername())) {
                if (fetchedObject.toString().contains(user.getPassword())) {
                    result = 1;
                    return createResponse(jsonObject, result);
                } else {
                    result = 0;

                    return createResponse(jsonObject, result);

                }
            } else {
                result = 0;
                createResponse(jsonObject, result);


                return createResponse(jsonObject, result);
            }
        }
        result = 0;

        return createResponse(jsonObject, result);

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
            case "Room":
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

            case "Device":
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

    //Används inte
    private DBObject addSomethingMethod(JSONObject fromServer, String entity) {
        dbCollection = databaseObj.getCollection(entity);
        JSONObject toAdd = new JSONObject();
        JSONArray jsonArr = new JSONArray();
        try {
            String id = fromServer.getString("deviceId");

            switch (entity) {
                case "house":

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

                case "room":
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

    //POST/create device.docx
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
            testJson.put("roomID", 22);
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
                return failJson.put("result", 0).put("deviceID", null);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;

    }

    // POST/create house.docx
    private JSONObject createNewHouse(JSONObject fromServer) {
        JSONObject successJson = new JSONObject();
        JSONObject failJson = new JSONObject();
        JSONObject testJson = new JSONObject();


        try {
            testJson.put("houseName", "tredjehuset");
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
            BasicDBList list = new BasicDBList();
            list.add(fromServer.getString("username"));
            query = new BasicDBObject();
            query.put("houseName", fromServer.getString("houseName"));
            query.put("housePassword", fromServer.getString("housePassword"));
            query.put("roomList", new BasicDBList());
            query.put("usernames", list);
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

    //POST/CREATEROOM.docx
    private JSONObject createNewRoom(JSONObject fromServer) {
        JSONObject succesJson = new JSONObject();
        JSONObject failJson = new JSONObject();
        JSONObject testJson = new JSONObject();
        try {

            testJson.put("username", "Bole");
            testJson.put("token", "12345");
            testJson.put("houseID", 1);
            testJson.put("roomName", "Kitchen");
            fromServer = testJson;

            dbCollection = databaseObj.getCollection("house");
            document = new BasicDBObject();
            document.put("houseID", fromServer.getInt("houseID"));

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
            myArr.add(String.valueOf(newRoomNumber + houseIdAfterMulti));
            if (addRoomToDb(fromServer, newRoomNumber + houseIdAfterMulti)) {
                dbCollection = databaseObj.getCollection("house");
                query = new BasicDBObject();
                query.append("$set", new BasicDBObject().append("roomList", myArr));
                BasicDBObject search = new BasicDBObject().append("houseID", houseIdAfterMulti / 10);
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

    //POST/Create user.docx
    public JSONObject createUser(JSONObject jsonObject) {


        JSONObject failJson = new JSONObject();
        JSONObject successJson = new JSONObject();
        try {
        /*    JSONObject filipTest = new JSONObject();
            filipTest.put("firstName", "Filip");
            filipTest.put("lastName", "BenkanssonjAO");
            filipTest.put("password", "123456");
            filipTest.put("username", "Benksaa33");
            filipTest.put("requestType", "create-user");
            filipTest.put("userId", "5c37692c-360f-4022-a7db-23a45f828c1d");
            filipTest.put("email", "sm@asd.com");
            filipTest.put("token", "123ad");
            jsonObject = filipTest;  //"den som kommer från server"
*/
            dbCollection = databaseObj.getCollection("User");
            gson = new Gson();
            User user = gson.fromJson(jsonObject.toString(), User.class);
            String userEmail = user.getEmail();
            String username = user.getusername();

            document = new BasicDBObject();
            document.put("email", userEmail);
            cursor = dbCollection.find(document);

            while (cursor.hasNext()) {
                fetchedObject = cursor.next();
                if (fetchedObject.toString().contains(userEmail)) {
                    failJson.put("result", 0);
                    failJson.put("requestType", jsonObject.getString("requestType"));
                    return failJson;
                } else if (fetchedObject.toString().contains(username)) {

                    failJson.put("result", 0);
                    failJson.put("requestType", jsonObject.getString("requestType"));
                    return failJson;
                } else if (fetchedObject.get("password").toString().length() < 3 || fetchedObject.get("password") == null) {
                    failJson.put("result", 0);
                    failJson.put("requestType", jsonObject.getString("requestType"));
                    return failJson;

                }
                commandLog(failJson, 4);

            }

            query = new BasicDBObject();
            query.put("firstName", user.getFirstName());
            query.put("lastName", user.getLastName());
            query.put("password", user.getPassword());
            query.put("username", user.getUsername());
            query.put("userId", user.getUserId());
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

    private boolean addRoomToDb(JSONObject fromServer, int newRoomNumber) {

        try {


            dbCollection = databaseObj.getCollection("room");

            document = new BasicDBObject();
            document.put("roomID", newRoomNumber);
            cursor = dbCollection.find(document);
            if (cursor.hasNext()) {
                System.out.println("roomnumber already in database. someone messed up ");
                return false;
                //HouseIDERROR
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;

    }

    private JSONArray getAllRoomsInHouseCount(String houseID) {
        dbCollection = databaseObj.getCollection("room");
        JSONArray arr = new JSONArray();
        JSONObject json = new JSONObject();
        document = new BasicDBObject();
        int houseIdInt = Integer.valueOf(houseID);
        document.put("houseID", houseIdInt);

        cursor = dbCollection.find(document);
        arr.put(0);
        while (cursor.hasNext()) {
            try {
                json = new JSONObject();
                fetchedObject = cursor.next();

                json.put("roomName", fetchedObject.get("roomName"));
                json.put("roomID", fetchedObject.get("roomID"));

                arr.put(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
     /*   BasicDBList dbList = (BasicDBList) cursor.next().get("roomList");
        for (Object s : dbList) {
            arr.put(s);

        }

      */

        return arr;
    }

    private JSONArray getAllHouses() {
        dbCollection = databaseObj.getCollection("House");
        JSONArray arr = new JSONArray();


        cursor = dbCollection.find();

        while (cursor.hasNext()) {
            arr.put(cursor.next());
        }
        System.out.println(arr.toString());
        return arr;
    }

    //GET/Get houses.docx
    private JSONArray getAllHousesForUser(JSONObject fromServer) {
        dbCollection = databaseObj.getCollection("House");
        JSONObject json = new JSONObject();
        JSONObject temp = new JSONObject();
        JSONArray arr = new JSONArray();

        try {
            temp.put("username", "Bole");
            temp.put("token", "1234");
            fromServer = temp;
            document = new BasicDBObject();
            cursor = dbCollection.find(document);

            while (cursor.hasNext()) {
                json = new JSONObject();
                fetchedObject = cursor.next();
                BasicDBList list = (BasicDBList) fetchedObject.get("usernames");
                for (Object e : list) {
                    String s = e.toString();
                    if (s.equals(fromServer.getString("username"))) {

                        System.out.println("Found user in a house list");
                        json.put("houseName", fetchedObject.get("houseName"));
                        json.put("houseID", fetchedObject.get("houseID"));
                        arr.put(json);

                    }

                }

            }
            return arr;

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return arr;
    }

    //GET/HOUSE.docx
    private JSONObject getHouse(JSONObject fromServer) {
        JSONObject jsonTest = new JSONObject();
        JSONObject jsonSuccess = new JSONObject();
        JSONObject jsonFail = new JSONObject();
        JSONArray array = new JSONArray();
        try {
            jsonTest.put("username", "Bole");
            jsonTest.put("token", "12355");
            jsonTest.put("houseID", 1);
            fromServer = jsonTest;

            array = getAllRoomsInHouseCount(String.valueOf(fromServer.getInt("houseID")));
            System.out.println(array.toString());
            JSONArray arr = new JSONArray();
            for (int i = 1; i < array.length(); i++) {
                JSONObject dbObj = (JSONObject) array.get(i);
                System.out.println(dbObj.toString());
                JSONObject json; //=  (JSONObject) dbObj.get("room");
                json = new JSONObject();
                json.put("roomName", dbObj.getString("roomName"));
                json.put("roomID", dbObj.getString("roomID"));


                arr.put(json);

                jsonSuccess.put("roomList", arr);


            }


            dbCollection = databaseObj.getCollection("House");
            document = new BasicDBObject();

            document.put("houseID", fromServer.getInt("houseID"));
            cursor = dbCollection.find(document);


            if (!cursor.hasNext()) {
                jsonFail.put("result", 0);
                jsonFail.put("houseID", fromServer.getInt("houseID"));
                return jsonFail;

            } else {
                fetchedObject = cursor.next();
                return jsonSuccess.put("houseName", fetchedObject.get("houseName"));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
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


    //gammal
    public String getDeviceStatus(Object id) {

        dbCollection = databaseObj.getCollection("devices");
        document = new BasicDBObject();
        document.put("id", "1234");
        cursor = dbCollection.find(document);
        fetchedObject = cursor.next();

        return fetchedObject.get("state").toString();

    }

    //TODO Gör klart json som ska skickas tillbaka
    public JSONObject getRoom(JSONObject fromServer) {
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
                temp = new JSONObject();
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

    public static Database getInstance() {
        if (database == null) {

            database = new Database("hej");
        }
        return database;


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


}
