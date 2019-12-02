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
public class MainMenu implements Initializable {


    @FXML
    Button logoutbtn, profilebtn;

    @FXML
    Label label1;

    @FXML
    private void logout() throws IOException {

        UserAccountStorage uas = UserAccountStorage.getInstance();
        uas.accountID = null;

        Stage stage = (Stage) logoutbtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void profile() throws IOException{
        Stage stage = (Stage) profilebtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("profile.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UserAccountStorage uas = UserAccountStorage.getInstance();

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
        label1.setText(name);
    }

    @FXML
    public void viewhouses() throws IOException{
        Stage stage = (Stage) profilebtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("houselist.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
