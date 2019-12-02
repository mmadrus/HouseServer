package rest.DBplusUI;

import com.mongodb.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;



/**
 * Created by L J on 11/29/2019.
 */
public class HouseList implements Initializable{

    @FXML
    Button backbtn, addbtn;

    @FXML
    ListView<Button> houselist;

    private ObservableList<Button> buttons = FXCollections.observableArrayList();

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
        DBCollection collection = database.getCollection("House");

        BasicDBObject search = new BasicDBObject();
        search.put("accountid", uas.accountID);



        DBCursor cursor = collection.find(search);

        while (cursor.hasNext()){

            DBObject object = cursor.next();

            String btnname = (String) object.get("housename");
            String btnid = object.get("_id").toString();


            Button b = new Button(btnname);
            b.setId(btnid);
            buttons.add(b);
            b.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                HouseStorage hs = HouseStorage.getInstance();
                hs.houseID = b.getId();

                try {
                    Stage stage = (Stage) b.getScene().getWindow();
                    Parent root = FXMLLoader.load(getClass().getResource("HouseMenu.fxml"));

                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }catch (IOException e){
                    e.printStackTrace();
                }
            });
        }

        houselist.setItems(buttons);

    }

    @FXML
    public void addHouse() throws IOException{
        Stage stage = (Stage) addbtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("addhouse.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
