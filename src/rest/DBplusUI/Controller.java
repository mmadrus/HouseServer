package rest.DBplusUI;

import com.mongodb.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class Controller {

    @FXML
    TextField usernametext;
    @FXML
    TextField passwordtext;


    @FXML
    Button loginbtn;


    @FXML
    private void login() throws Exception {

        MongoClient mongo = new MongoClient("localhost", 27017);
        DB database = mongo.getDB("HouseDatabase");
        DBCollection collection = database.getCollection("User");



        BasicDBObject search = new BasicDBObject();
        List<BasicDBObject> object = new ArrayList<BasicDBObject>();
        object.add(new BasicDBObject("username", usernametext.getText()));
        object.add(new BasicDBObject("password", passwordtext.getText()));
        search.put("$and", object);

        DBCursor cursor = collection.find(search);


        if (cursor.hasNext()){



            UserAccountStorage uas = UserAccountStorage.getInstance();
            uas.accountID = cursor.next().get("_id").toString();


            Stage stage = (Stage) loginbtn.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("mainmenu.fxml"));


            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();



        }
        else{
            System.out.println("not exist");
        }
    }

}
