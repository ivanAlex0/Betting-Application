package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import sqlserver.SQLConnection;
import user.Scraping;
import user.User;

import java.io.IOException;

public class SceneController {

    public static Scraping scraping = null;
    public static SQLConnection sqlConnection = null;
    public static User currentUser = null;
    public static User controlledUser = null;
    public static ActionEvent actionEvent;
    public static Stage stage = null;
    public static Scene scene = null;

    public SceneController() {
        if (scraping == null) {
            scraping = new Scraping();
            scraping.getLeaguesFromJson();
            sqlConnection = new SQLConnection();
        }
    }

    /**
     * The Function changes the currentScene to the Scene from fxmlFilePath
     */
    public void changeScene(ActionEvent actionEvent, String fxmlFilePath) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(fxmlFilePath));
        if (scene == null) {
            scene = new Scene(parent);
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        } else
            stage.getScene().setRoot(parent);
    }

    public void exitApp() {
        System.exit(0);
    }
}
