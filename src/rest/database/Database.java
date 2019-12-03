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

        //db.getRooms("5de107ce454ec82185944700");
        //db.getHouseInfo("5de107ce454ec82185944700");
        //db.getRoomInfo("5de6359633cc2a2a94de316b");

        //System.out.println(db.hasHouse("5ddf9c659802af312021554d"));
        //System.out.println(db.addHouse("This House", "Storgatan 10", "135 79", "Kristianstad", "5ddfea939802af312021554e"));
        //System.out.println(db.deleteHouse("5de68c2bc707335f49e7a92f"));
        //System.out.println(db.isHouseExist("5de0f5c09802af3120215551"));
        //System.out.println(db.addRoom("Bathroom1", "Bathroom", "5de0f5c09802af3120215551"));

        /*
        House house = db.getHouses("5ddfea939802af312021554f");
        System.out.println("House under the ownership of user iD:" + house.getAccountID());

            System.out.println("HouseID: " + house.getHouseID() + " Name: " + house.getHouseName() +
                    " Address: " + house.getAddress() + " Post no. " + house.getPostno() + " City: " +
                    house.getCity());
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

    //get list of houses owned by this user
    public House getHouses(String userid){
        dbCollection = databaseObj.getCollection("House");
        document = new BasicDBObject();
        document.put("accountid", userid);
        cursor = dbCollection.find(document);

        House house = null;

        while(cursor.hasNext()){

            BasicDBObject obj = (BasicDBObject) cursor.next();

            String id = obj.get("_id").toString();
            String name = obj.get("housename").toString();
            String address = obj.get("address").toString();
            String postno = obj.get("postno").toString();
            String city = obj.get("city").toString();
            String accountID = obj.get("accountid").toString();

            house = new House(id, name, address, postno, city, accountID);
        }

        return house;
    }

    //get list of rooms inside the house
    public void getRooms(String houseid){
        dbCollection = databaseObj.getCollection("Room");
        document = new BasicDBObject();
        document.put("houseid", houseid);
        cursor = dbCollection.find(document);

        while (cursor.hasNext()){
            System.out.println(cursor.next());
        }
    }



    //get information about the house
    public void getHouseInfo(String houseid){
        dbCollection = databaseObj.getCollection("House");
        document = new BasicDBObject();
        document.put("_id", new ObjectId(houseid));
        cursor = dbCollection.find(document);

        while(cursor.hasNext()){
            System.out.println(cursor.next());
        }
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

    //add a new house to the user only if the user didn't owned any house yet
    public String addHouse(String name, String address, String post, String city, String userid){
        boolean hasHouse = hasHouse(userid);

        if (hasHouse == false){
            dbCollection = databaseObj.getCollection("House");
            document = new BasicDBObject();
            document.put("housename", name);
            document.put("address", address);
            document.put("postno", post);
            document.put("city", city);
            document.put("accountid", userid);
            dbCollection.insert(document);
            return "Successfully added house";
        }
        else {
            return "You already owned a house";
        }
    }

    //remove the house from the user only if it has 0 rooms in it
    public String deleteHouse(String houseid){
        boolean hasRoom = hasRooms(houseid);
        boolean isHouseExist = isHouseExist(houseid);

        if (hasRoom == false && isHouseExist == true){

            dbCollection = databaseObj.getCollection("House");
            document = new BasicDBObject();
            document.put("_id", new ObjectId(houseid));
            dbCollection.remove(document);
            return "Successfully removed the house";

        }
        else {
            if (isHouseExist == true) {
                return "Houses must have no rooms first before deleting this house";
            }
            else{
                return "This house doesn't exist";
            }
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

    public String addRoom(String roomname, String roomtype, String houseid){
        boolean isHouseExist = isHouseExist(houseid);

        if(isHouseExist == true){
            dbCollection = databaseObj.getCollection("Room");
            document = new BasicDBObject();
            document.put("roomname", roomname);
            document.put("roomtype", roomtype);
            document.put("houseid", houseid);
            dbCollection.insert(document);
            return "Successfully added room";
        }
        else{
            return "This house doesn't exist";
        }
    }

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

}
