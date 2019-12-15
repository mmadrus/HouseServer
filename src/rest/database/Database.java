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
import rest.models.House;
import rest.models.Room;
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
    private static Database database = Database.getInstance();
    private DBCollection dbCollection;
    private BasicDBObject document, query;
    private DBCursor cursor;
    private DBObject fetchedObject;


    public static void main(String[] args) {
/*
        Object object = Database.getInstance().getDeviceId("1234");
        String status = Database.getInstance().getDeviceStatus(object);
        System.out.println(status);

        Object mongoObjectId = database.getDeviceId("1234");
        Database.getInstance().findUser("1234-abcd-12dc");
        Database.getInstance().changeStatusOfDevice(object);
        System.out.println("MongoObjectId " + mongoObjectId.toString());
        database.changeStatusOfDevice(mongoObjectId);
*/
        //System.out.println(database.createUser("hej"));

        Database db = Database.getInstance();

        /*
        House house= db.getHouseInfo("5de107ce454ec82185944700");
        System.out.println(house.getHouseID() + house.getHouseName() + house.getAddress() + house.getPostno() +
                house.getCity() + house.getPassword());

         */

        //db.getRoomInfo("5de6359633cc2a2a94de316b");
        //db.getDevices("5de6361933cc2a2a94de316c");

        //System.out.println(db.hasHouse("5ddf9c659802af312021554d"));
        System.out.println(db.addHouse("This House", "Storgatan 10", "135 79", "Kristianstad", "myHouse"));
        //System.out.println(db.deleteHouse("5de8f691ac052a173740fc27"));
        //System.out.println(db.isHouseExist("5de0f5c09802af3120215551"));
        //System.out.println(db.addRoom("Bathroom1", "Bathroom", "5dee200d2675a14c3268e3bf"));
        //System.out.println(db.deleteRoom("5de6a236a885340493488804"));
        //System.out.println(db.getHouseID("myHouse1", "5ddf9c659802af312021554d"));
        //System.out.println(db.getRoomID("Living Room 1", "5de0f5c09802af3120215551"));
        //System.out.println(db.isRoomExist("Living Room 1", "5de0f5c09802af3120215551"));
        //System.out.println(db.addDevice("myDevice1", "Smoke Alarm", "ACTIVE", "5de6359633cc2a2a94de316b"));
        //System.out.println(db.hasDevices("5de6361933cc2a2a94de316c"));
        //System.out.println(db.isUserExist("5de8f9ce33cc2a2a94de316f"));
        //System.out.println(db.deleteDevice("5de92c4333cc2a2a94de3171"));
        //System.out.println(db.deleteUser("5dee1fde4953612e6c4030c2"));



        //db.getHouses("5ddf9c659802af312021554d");

        /*
        ArrayList<House> houses = db.getHouses("5ddf9c659802af312021554d");

        for (House h : houses){
            System.out.println(h.getHouseID() + " " + h.getHouseName() + " " + h.getAddress() + " " +
                    h.getPostno() + h.getCity());
        }
        */










        /*
        ArrayList<Room> rooms = db.getRooms("5de107ce454ec82185944700");
        if (rooms.size() != 0) {
            System.out.println("Rooms inside house ID: " + rooms.get(0).getHouseID());
            for (Room room : rooms) {
                System.out.println(room.getRoomID() + " " + room.getRoomName() + " " +
                        room.getRoomType());
            }
        }
        else{
            System.out.println("No rooms inside this house");
        }
        */




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
        dbCollection = databaseObj.getCollection("User");

        document = new BasicDBObject();
        document.put("userId", id);
        cursor = dbCollection.find(document);

        if (!cursor.hasNext()) {
            return "No Such user";
        }

        fetchedObject = cursor.next();


        return fetchedObject;


    }


    public static Database getInstance() {
        if (database == null) {

            database = new Database();
        }
        return database;


    }

    public boolean commandLog (JSONObject jsonObject) {

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

    //get the house owned by this user
    public ArrayList<House> getHouses(String userid){
        dbCollection = databaseObj.getCollection("User");
        document = new BasicDBObject();
        document.put("_id", new ObjectId(userid));
        cursor = dbCollection.find(document);

        ArrayList<House> houses = new ArrayList<>();


        while(cursor.hasNext()){

            BasicDBList list = (BasicDBList) cursor.next().get("houseid");

            for (int i = 0; i<list.size(); i++){

                String houseID = (String) list.get(i);
                House obj = getHouseInfo(houseID);


                String id = obj.getHouseID();
                String name = obj.getHouseName();
                String address = obj.getAddress();
                String post = obj.getPostno();
                String city = obj.getCity();
                String password = obj.getPassword();


                houses.add(new House(id, name, address, post, city, password));

            }

            return houses;

        }

        return null;
    }

    //get list of rooms inside the house
    public ArrayList<Room> getRooms (String houseid){
        dbCollection = databaseObj.getCollection("Room");
        document = new BasicDBObject();
        document.put("houseid", houseid);
        cursor = dbCollection.find(document);

        ArrayList<Room> rooms = new ArrayList<>();

        while (cursor.hasNext()){
            BasicDBObject obj = new BasicDBObject();
            obj = (BasicDBObject) cursor.next();

            String roomid = obj.get("_id").toString();
            String roomname = obj.get("roomname").toString();
            String roomtype = obj.get("roomtype").toString();
            String houseID = obj.get("houseid").toString();

            rooms.add(new Room(roomid, roomname, roomtype, houseID));
        }

        return rooms;
    }


    //get information about the house
    public House getHouseInfo(String houseid){
        dbCollection = databaseObj.getCollection("House");
        document = new BasicDBObject();
        document.put("_id", new ObjectId(houseid));
        cursor = dbCollection.find(document);

        House house;

        if (cursor.hasNext()){
            BasicDBObject obj = new BasicDBObject();
            obj = (BasicDBObject) cursor.next();

            String id = obj.get("_id").toString();
            String housename = obj.get("housename").toString();
            String address = obj.get("address").toString();
            String postno = obj.get("postno").toString();
            String city = obj.get("city").toString();
            String password = obj.get("password").toString();

            house = new House(id,housename,address,postno,city,password);
            return house;
        }

        return null;
    }

    //get information about the room
    public void getRoomInfo(String roomid){
        dbCollection = databaseObj.getCollection("Room");
        document = new BasicDBObject();
        document.put("_id", new ObjectId(roomid));
        cursor = dbCollection.find(document);

        while(cursor.hasNext()){
            System.out.println(cursor.next());
        }
    }

    //check if user already owns a house
    public boolean hasHouse(String userid){
        dbCollection = databaseObj.getCollection("House");
        document = new BasicDBObject();
        document.put("accountid", userid);
        cursor = dbCollection.find(document);

        if (cursor.hasNext()){
            return true;
        }
        return false;
    }

    //check if user exist
    public boolean isUserExist(String accountid){
        dbCollection = databaseObj.getCollection("User");
        document = new BasicDBObject();
        document.put("_id", new ObjectId(accountid));
        cursor = dbCollection.find(document);

        if (cursor.hasNext()){
            return true;
        }
        return false;
    }

    //add a new house to the user only if the user didn't owned any house yet
    public String addHouse(String name, String address, String post, String city, String password){

        int newIDvalue = getHouseHighestID() + 1;
        String newID = Integer.toString(newIDvalue);


            dbCollection = databaseObj.getCollection("House");
            document = new BasicDBObject();
            document.put("housename", name);
            document.put("address", address);
            document.put("postno", post);
            document.put("city", city);
            document.put("password", password);
            document.put("house-id", newID);
            dbCollection.insert(document);
            return "Successfully added house";


    }

    //remove the house from the user only if it exists.
    //if it has rooms, it will also delete all the room within this house.
    //and eventually delete all the devices linked to these rooms.
    public String deleteHouse(String houseid){
        boolean hasRoom = hasRooms(houseid);
        boolean isHouseExist = isHouseExist(houseid);
        ArrayList<Room> rooms;

        if (isHouseExist == true){

            if (hasRoom == true) {
                rooms = getRooms(houseid);
                for (Room room : rooms) {
                    deleteRoom(room.getRoomID());
                }
            }

            dbCollection = databaseObj.getCollection("House");
            document = new BasicDBObject();
            document.put("_id", new ObjectId(houseid));
            dbCollection.remove(document);
            return "Successfully removed the house";

        }
        else {
            return "This house doesn't exist";
        }
    }

    //remove the room of the house if it exist as
    //well as the devices linked to this room
    public String deleteRoom(String roomid){
        boolean hasDevices = hasDevices(roomid);
        boolean isRoomExist = isRoomExist(roomid);


        if (isRoomExist == true){

            if (hasDevices == true){
                // remove all devices under this room
            }

            dbCollection = databaseObj.getCollection("Room");
            document = new BasicDBObject();
            document.put("_id", new ObjectId(roomid));
            dbCollection.remove(document);
            return "Successfully removed the room";
        }
        else{
            return "Room doesn't exist";
        }
    }

    //check if this house has any rooms
    public boolean hasRooms(String houseid){
        dbCollection = databaseObj.getCollection("Room");
        document = new BasicDBObject();
        document.put("houseid", houseid);
        cursor = dbCollection.find(document);

        if (cursor.hasNext()){
            return true;
        }
        return false;
    }

    //add room only if the house exist.
    public String addRoom(String roomname, String roomtype, String houseid){
        boolean isHouseExist = isHouseExist(houseid);
        boolean isRoomExist = isRoomExist(roomname, houseid);

        if(isHouseExist == true && isRoomExist == false){
            dbCollection = databaseObj.getCollection("Room");
            document = new BasicDBObject();
            document.put("roomname", roomname);
            document.put("roomtype", roomtype);
            document.put("houseid", houseid);
            dbCollection.insert(document);
            return "Successfully added room";
        }
        else{
            if (isHouseExist == false) {
                return "This house must exist first before adding a room";
            }

            else{
                return "Room Name already existed";
            }
        }
    }

    //check if the house exist
    public boolean isHouseExist(String houseid){
        dbCollection = databaseObj.getCollection("House");
        document = new BasicDBObject();
        document.put("_id", new ObjectId(houseid));
        cursor = dbCollection.find(document);

        if(cursor.hasNext()){
            return true;
        }
        return false;
    }

    //check if the room exist
    public boolean isRoomExist(String roomid){
        dbCollection = databaseObj.getCollection("Room");
        document = new BasicDBObject();
        document.put("_id", new ObjectId(roomid));
        cursor = dbCollection.find(document);

        if(cursor.hasNext()){
            return true;
        }
        return false;
    }

    //another variant to check if room exist
    public boolean isRoomExist(String roomname, String houseid){
        dbCollection = databaseObj.getCollection("Room");
        document = new BasicDBObject();
        List<BasicDBObject> obj = new ArrayList<>();
        obj.add(new BasicDBObject("roomname", roomname));
        obj.add(new BasicDBObject("houseid", houseid));
        document.put("$and", obj);
        cursor = dbCollection.find(document);

        if (cursor.hasNext()){
            return true;
        }
        return false;
    }

    //get ID of this House
    public String getHouseID(String housename, String userid){
        dbCollection = databaseObj.getCollection("House");
        document = new BasicDBObject();
        List<BasicDBObject> obj = new ArrayList<>();
        obj.add(new BasicDBObject("housename", housename));
        obj.add(new BasicDBObject("accountid", userid));
        document.put("$and", obj);
        cursor = dbCollection.find(document);

        if (cursor.hasNext()){
            return cursor.next().get("_id").toString();
        }
        return "This house does not exist";
    }

    //get ID of this Room
    public String getRoomID(String roomname, String houseid){
        dbCollection = databaseObj.getCollection("Room");
        document = new BasicDBObject();
        List<BasicDBObject> obj = new ArrayList<>();
        obj.add(new BasicDBObject("roomname", roomname));
        obj.add(new BasicDBObject("houseid", houseid));
        document.put("$and", obj);
        cursor = dbCollection.find(document);

        if (cursor.hasNext()){
            return cursor.next().get("_id").toString();
        }
        return "This room does not exist";
    }

    //add Device to this room
    public String addDevice(String devicename, String type, String status, String roomid){
        boolean isRoomExist = isRoomExist(roomid);

        if (isRoomExist == true){
            dbCollection = databaseObj.getCollection("Device");
            document = new BasicDBObject();
            document.put("devicename", devicename);
            document.put("devicetype", type);
            document.put("status", status);
            document.put("roomid", roomid);
            dbCollection.insert(document);
            return "Successfully added device";
        }
        else{
            return "Room must exist first before adding device";
        }
    }

    //get all devices connected to this room
    public void getDevices(String roomid){
        dbCollection = databaseObj.getCollection("Device");
        document = new BasicDBObject();
        document.put("roomid", roomid);
        cursor = dbCollection.find(document);

        while (cursor.hasNext()){
            System.out.println(cursor.next());
        }
    }

    //checks if the room has any devices linked
    public boolean hasDevices(String roomid){
        dbCollection = databaseObj.getCollection("Device");
        document = new BasicDBObject();
        document.put("roomid", roomid);
        cursor = dbCollection.find(document);

        if (cursor.hasNext()){
            return true;
        }
        return false;
    }

    //remove a device from this room
    public String deleteDevice(String deviceid){
        boolean isDeviceExist = isDeviceExist(deviceid);

        if (isDeviceExist == true){
            dbCollection = databaseObj.getCollection("Device");
            document = new BasicDBObject();
            document.put("_id", new ObjectId(deviceid));
            dbCollection.remove(document);
            return "Successfully removed the device";
        }
        else{
            return "Device does not exist";
        }
    }

    //check if this device exists
    public boolean isDeviceExist(String deviceid){
        dbCollection = databaseObj.getCollection("Device");
        document = new BasicDBObject();
        document.put("_id", new ObjectId(deviceid));
        cursor = dbCollection.find(document);

        if (cursor.hasNext()){
            return true;
        }
        return false;
    }

    //removes the user account as well as the house, rooms
    //and eventually devices associated with this user
    public String deleteUser(String userid){

        boolean isUserExist = isUserExist(userid);

        if (isUserExist == true){


            dbCollection = databaseObj.getCollection("User");
            document = new BasicDBObject();
            document.put("_id", new ObjectId(userid));
            dbCollection.remove(document);

            return "Successfully removed the user";
        }
        else{
            return "User does not exist";
        }

    }

    public int getHouseHighestID(){
        dbCollection = databaseObj.getCollection("House");
        document = new BasicDBObject();
        document.put("house-id", -1);
        cursor = dbCollection.find().sort(document).limit(1);

        if (cursor.hasNext()){
            return Integer.parseInt(cursor.next().get("house-id").toString());
        }
        return 0;

    }

    /*
    public long getRoomCount(){
        dbCollection = databaseObj.getCollection("House");
        document = new BasicDBObject();
    }
    */
    /*
    public String editHouse(String houseid){
        dbCollection = databaseObj.getCollection("House");
        document = new BasicDBObject();
        document.put("_id", new ObjectId(houseid));

        List<BasicDBObject> newDocument = new ArrayList<>();
    }

    public String editRoom(String roomid){
        dbCollection = databaseObj.getCollection("Room");
        document = new BasicDBObject();
        document.put("_id", new ObjectId(roomid));

        List<BasicDBObject> newDocument = new ArrayList<>();

        //newDocument.put("$and", document);
    }
    */




}
