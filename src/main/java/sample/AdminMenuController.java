package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

import javafx.scene.text.Text;
import user.User;

import java.io.IOException;


public class AdminMenuController extends SceneController {

    @FXML
    private ListView<Pane> listView;

    @FXML
    private void goToStats() {
        try {
            changeScene(actionEvent, "/AdminBettingStats.fxml");
        } catch (IOException ioException) {
            System.out.println("The page could not be changed. @goToStats");
        }
    }

    @FXML
    private void initialize() {
        ObservableList<Pane> observableList = FXCollections.observableArrayList();

        for (User user : sqlConnection.getUsers()) {
            Button userBetHistoryButton = new Button("See betting history");
            userBetHistoryButton.setLayoutX(500);
            userBetHistoryButton.setLayoutY(0);
            userBetHistoryButton.setStyle(
                    "-fx-background-radius: 20;" +
                            "-fx-background-color: #6e5210;" +
                            "-fx-font: 24 arial;"
            );
            userBetHistoryButton.setOnAction(e ->
            {
                controlledUser = user;
                try {
                    changeScene(e, "/AdminBetHistory.fxml");
                } catch (IOException ioException) {
                    System.out.println("The file could not be changed! @adminMenu to History");
                }
            });

            Button userPersonalDataButton = new Button("See personal data");
            userPersonalDataButton.setLayoutY(0);
            userPersonalDataButton.setLayoutX(780);
            userPersonalDataButton.setStyle(
                    "-fx-background-radius: 20;" +
                            "-fx-background-color: #10556e;" +
                            "-fx-font: 24 arial;"
            );
            userPersonalDataButton.setOnAction(e -> {
                controlledUser = user;
                try {
                    changeScene(e, "/AdminPersonalData.fxml");
                } catch (IOException ioException) {
                    System.out.println("The file could not be changed! @adminMenu to PersonalData");
                }
            });

            Pane pane = new Pane();
            Text id = new Text(user.getId() + " - " + user.getUsername());
            id.setLayoutY(25);
            id.setLayoutX(10);
            id.setStyle(
                    "-fx-font: 24 arial;"
            );

            pane.getChildren().add(userBetHistoryButton);
            pane.getChildren().add(userPersonalDataButton);
            pane.getChildren().add(id);

            observableList.add(pane);
        }

        listView.setItems(observableList);
    }

    @FXML
    private void goBack(ActionEvent actionEvent) {
        try {
            changeScene(actionEvent, "/SignIn.fxml");
        } catch (IOException ioException) {
            System.out.println("The page could not be changed! @goBackFromAdminMenu");
            ioException.printStackTrace();
        }
    }
}
