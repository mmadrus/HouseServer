package rest.DBplusUI;

import com.mongodb.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

/**
 * Created by L J on 11/28/2019.
 */
public class Profile implements Initializable {

    @FXML
    Label userlabel, emaillabel, idlabel;
    @FXML
    Button backbtn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UserAccountStorage uas = UserAccountStorage.getInstance();
        idlabel.setText(uas.accountID);

        MongoClient mongo = null;
        try {
            mongo = new MongoClient("localhost", 27017);
        } catch (Exception e) {
            e.printStackTrace();
        }
        DB database = mongo.getDB("HouseDatabase");
        DBCollection collection = database.getCollection("User");

        BasicDBObject search = new BasicDBObject();
        search.put("_id", new ObjectId(uas.accountID));

        DBObject object = collection.findOne(search);
        String name = (String) object.get("username");
        String email = (String) object.get("email");


        userlabel.setText(name);
        emaillabel.setText(email);


    }

    @FXML
    public void back() throws IOException{
        Stage stage = (Stage) backbtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("mainmenu.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
