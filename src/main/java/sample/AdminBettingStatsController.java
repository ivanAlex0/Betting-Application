package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import sqlserver.SQLConnection;

import java.io.IOException;
import java.sql.*;

public class AdminBettingStatsController extends SceneController {

    @FXML
    private ListView<Pane> listView;

    @FXML
    private void initialize() {
        Connection sqlConnection;
        try {
            sqlConnection = DriverManager.getConnection(SQLConnection.getDatabaseUrl(), SQLConnection.getDatabaseUsername(), SQLConnection.getDatabasePassword());

            Statement stmtmDistinctOdds = sqlConnection.createStatement();
            ResultSet distinctOdds = stmtmDistinctOdds.executeQuery("SELECT DISTINCT team1, team2, dateTime, count(*) as nb FROM Odd GROUP BY team1, team2, dateTime");

            ObservableList<Pane> allMatchesNbOfBets = FXCollections.observableArrayList();
            while (distinctOdds.next()) {
                Text teams = new Text(distinctOdds.getString("team1") + " - " +
                        distinctOdds.getString("team2"));
                teams.setLayoutY(25);
                teams.setLayoutX(100);
                teams.setStyle(
                        "-fx-font: 24 arial;"
                );

                Text dateTime = new Text(distinctOdds.getString("dateTime"));
                dateTime.setLayoutY(40);
                dateTime.setLayoutX(100);
                dateTime.setStyle(
                        "-fx-font: 12 arial;" +
                                "-fx-background-color: #9e6303;"
                );

                Text numberOfBets = new Text(distinctOdds.getString("nb"));
                numberOfBets.setLayoutY(25);
                numberOfBets.setLayoutX(900);
                numberOfBets.setStyle(
                        "-fx-font: 24 arial;" +
                                "-fx-background-color: #03fc90;"
                );

                Pane currentPane = new Pane();
                currentPane.setStyle(
                        "-fx-background-color: #9c9994;"
                );
                currentPane.getChildren().add(dateTime);
                currentPane.getChildren().add(teams);
                currentPane.getChildren().add(numberOfBets);
                allMatchesNbOfBets.add(currentPane);
            }

            listView.setItems(allMatchesNbOfBets);

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

    }

    @FXML
    private void goBack(ActionEvent actionEvent) {
        try {
            changeScene(actionEvent, "/AdminMenu.fxml");
        } catch (IOException ioException) {
            System.out.println("The file could not be changed! @goBackToAdminMenuFromStats");
        }
    }
}
