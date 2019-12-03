package rest.DBplusUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by L J on 11/29/2019.
 */
public class HouseMenu implements Initializable{

    @FXML
    Label housenamelabel;

    @FXML
    Button backbtn, viewhousebtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        HouseStorage hs = HouseStorage.getInstance();
        housenamelabel.setText(hs.houseID);
    }

    @FXML
    public void back() throws IOException {
        HouseStorage hs = HouseStorage.getInstance();
        hs.houseID = null;

        Stage stage = (Stage) backbtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("houselist.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void info() throws IOException {
        Stage stage = (Stage) backbtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("houseprofile.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
