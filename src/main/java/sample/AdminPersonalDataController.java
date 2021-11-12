package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class AdminPersonalDataController extends SceneController {

    @FXML
    private Label _username;

    @FXML
    private Label _name;

    @FXML
    private Label _CNP;

    @FXML
    private Label _balance;

    @FXML
    private Label _ID;

    @FXML
    private void initialize() {
        _username.setText(controlledUser.getUsername());
        _name.setText(controlledUser.getName());
        _CNP.setText(controlledUser.getCNP());
        _balance.setText(String.valueOf(controlledUser.getBalance()));
        _ID.setText(String.valueOf(controlledUser.getId()));
    }

    @FXML
    private void goBack(ActionEvent actionEvent) {
        try {
            changeScene(actionEvent, "/AdminMenu.fxml");
        } catch (IOException ioException) {
            System.out.println("The page could not be loaded! @goBack");
        }
    }
}
