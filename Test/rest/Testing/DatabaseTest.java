package rest.Testing;

import com.google.gson.Gson;
import com.mongodb.*;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import rest.database.Database;

import static org.junit.Assert.*;

/*
This is Database test class. Since its connected to the database, some test will fail unless you change values.
E.g. Createuser method, this will not succeed twice if you enter the same email. If the email already exists in the database, it will fail
So to make sure it succeed, change the email after one try. (Remember to clean up your database :) )


 */
public class DatabaseTest {
    private Gson gson;
    private MongoClient mongoClient = null;
    private DB databaseObj = null;
    private static Database database = Database.getInstance();
    private DBCollection dbCollection;
    private BasicDBObject document, query;
    private DBCursor cursor;
    private DBObject fetchedObject;

    @Before
    public void setUp() throws Exception {


    }

    @Test
    public void loginMethodSuccess() throws Exception {

        JSONObject testObject = new JSONObject();
        testObject.put("userName", "IsakZ");
        testObject.put("password", "123");
        String jsonString = testObject.toString();


        String actual = Database.getInstance().loginMethod(jsonString);

        Assert.assertEquals("1", actual);


    }
    @Test
    public void loginMethodFail() throws Exception {

        JSONObject testObject = new JSONObject();
        testObject.put("userName", "");
        testObject.put("password", "");
        String jsonString = testObject.toString();


        String actual = Database.getInstance().loginMethod(jsonString);

        Assert.assertEquals("0", actual);


    }


    @Test
    public void getDeviceStatus() throws Exception {
    }

    @Test
    public void getDeviceId() throws Exception {
    }

    @Test
    public void createUserFail() throws Exception {
        JSONObject testJson = new JSONObject();
        testJson.put("firstName", "Filip");
        testJson.put("lastName", "BenkanssonjAO");
        testJson.put("password", "123456");
        testJson.put("userName", "Benka33");
        testJson.put("userId", "5c37692c-360f-4022-a7db-23a45f828c1d");
        testJson.put("email", "sm@somethimore.com");
        String jsonString = testJson.toString();  //"den som kommer från server"


        String actual = Database.getInstance().createUser(jsonString);

        Assert.assertEquals("0", actual);

    }

    @Test
    public void createUserSuccess() throws Exception {
        JSONObject testJson = new JSONObject();
        testJson.put("firstName", "Filip");
        testJson.put("lastName", "BenkanssonjAO");
        testJson.put("password", "123456");
        testJson.put("userName", "Benka33"); //Change email and username for it to succeed
        testJson.put("userId", "5c37692c-360f-4022-a7db-23a45f828c1d");
        testJson.put("email", "sm@somete.com"); //Change email and username for it to succeed
        String jsonString = testJson.toString();  //"den som kommer från server"


        String actual = Database.getInstance().createUser(jsonString);

        Assert.assertEquals("1", actual);

    }

}