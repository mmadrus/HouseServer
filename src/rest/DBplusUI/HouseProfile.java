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
 * Created by L J on 11/29/2019.
 */
public class HouseProfile implements Initializable {

    @FXML
    Label idlabel, namelabel, addresslabel, postlabel, citylabel;

    @FXML
    Button backbtn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        HouseStorage hs = HouseStorage.getInstance();

        MongoClient mongo = null;
        try {
            mongo = new MongoClient("localhost", 27017);
        } catch (Exception e) {
            e.printStackTrace();
        }
        DB database = mongo.getDB("HouseDatabase");
        DBCollection collection = database.getCollection("House");

        BasicDBObject search = new BasicDBObject();
        search.put("_id", new ObjectId(hs.houseID));

        DBObject object = collection.findOne(search);
        String name = (String) object.get("housename");
        String address = (String) object.get("address");
        String post = (String) object.get("postno");
        String city = (String) object.get("city");

        idlabel.setText(hs.houseID);
        namelabel.setText(name);
        addresslabel.setText(address);
        postlabel.setText(post);
        citylabel.setText(city);
    }

    @FXML
    public void back() throws IOException {
        Stage stage = (Stage) backbtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("HouseMenu.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
