package rest.DBplusUI;

import com.mongodb.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

/**
 * Created by L J on 11/29/2019.
 */
public class AddHouse implements Initializable {

    @FXML
    Button addbtn, cancelbtn;

    @FXML
    TextField nametext, addresstext, posttext, citytext;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void addHouse() throws IOException{
        UserAccountStorage uas = UserAccountStorage.getInstance();

        if (!nametext.getText().equalsIgnoreCase("") || !addresstext.getText().equalsIgnoreCase("") ||
                !posttext.getText().equalsIgnoreCase("") || !citytext.getText().equalsIgnoreCase("")) {

            MongoClient mongo = null;
            try {
                mongo = new MongoClient("localhost", 27017);
            } catch (Exception e) {
                e.printStackTrace();
            }
            DB database = mongo.getDB("HouseDatabase");
            DBCollection collection = database.getCollection("House");
            BasicDBObject object = new BasicDBObject();
            object.put("housename", nametext.getText());
            object.put("address", addresstext.getText());
            object.put("postno", posttext.getText());
            object.put("city", citytext.getText());
            object.put("accountid", uas.accountID);
            collection.insert(object);

            Stage stage = (Stage) cancelbtn.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("houselist.fxml"));

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("All fields cannot be empty");
            alert.showAndWait();
        }
    }

    @FXML
    public void cancel() throws IOException {
        Stage stage = (Stage) cancelbtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("houselist.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
