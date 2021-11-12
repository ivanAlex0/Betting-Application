package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import user.Bet;
import user.Odd;

import java.io.IOException;
import java.util.ArrayList;

public class BetHistoryController extends SceneController {

    private final ObservableList<Pane> allBets = FXCollections.observableArrayList();
    private final ArrayList<ObservableList<Pane>> allBetsAsLists = new ArrayList<>();

    @FXML
    private ListView<Pane> listView;

    @FXML
    private ListView<Pane> listViewOdds;

    @FXML
    private Label totalOdd;

    @FXML
    private Label date;

    @FXML
    private Label _ammount;

    @FXML
    private void initialize() {
        totalOdd.setText("");
        date.setText("");
        _ammount.setText("");
        for (Bet bet : currentUser.getBets()) {
            Button betButton = new Button(bet.getDate().toString() + "          " + bet.getTotalOdd());
            betButton.setLayoutX(0);
            betButton.setLayoutY(0);
            betButton.setStyle(
                    "-fx-background-color:  #16b55d;" +
                            "-fx-font: 15 arial;"
            );
            betButton.setOnAction(e -> {
                listViewOdds.setItems(allBetsAsLists.get(currentUser.getBets().indexOf(bet)));
                totalOdd.setText(String.valueOf(bet.getTotalOdd()));
                date.setText(bet.getDate().toString());
                _ammount.setText(String.valueOf(bet.getAmountBet()));
            });

            Pane pane = new Pane();
            pane.getChildren().add(betButton);
            allBets.add(pane);

            allBetsAsLists.add(constructOsbList(bet));
        }

        if (!allBets.isEmpty())
            listView.setItems(allBets);

    }

    /**
     * @return - the ObservableList of Panes corresponding to Bet bet
     */
    @FXML
    private ObservableList<Pane> constructOsbList(Bet bet) {
        ObservableList<Pane> currObsList = FXCollections.observableArrayList();
        for (Odd odd : bet.getAllOdds()) {
            Pane auxPane = new Pane();
            Text teams = new Text(odd.getTeam1() + " - " + odd.getTeam2());
            teams.setLayoutY(25);
            teams.setStyle(
                    "-fx-font: 24 arial;"
            );

            Label betL = new Label(odd.getOddType());
            Label oddL = new Label(String.valueOf(odd.getOdd()));

            Text dateTime = new Text(odd.getDateTime());
            dateTime.setLayoutY(40);
            dateTime.setStyle(
                    "-fx-font: 12 arial;"
            );

            betL.setLayoutY(5);
            betL.setLayoutX(750);
            betL.setStyle(
                    "-fx-font: 24 arial;"
            );

            oddL.setLayoutY(5);
            oddL.setLayoutX(900);
            oddL.setStyle(
                    "-fx-font: 25 arial;" +
                            "-fx-background-color: #1e9e71;"
            );

            auxPane.setStyle(
                    "-fx-background-color: #948f8a;" +
                            "-fx-background-radius: 0;"
            );
            auxPane.getChildren().add(dateTime);
            auxPane.getChildren().add(teams);
            auxPane.getChildren().add(betL);
            auxPane.getChildren().add(oddL);
            currObsList.add(auxPane);
        }

        return currObsList;
    }

    @FXML
    private void goBack(ActionEvent actionEvent) {
        try {
            changeScene(actionEvent, "/MainMenu.fxml");
        } catch (IOException ioException) {
            System.out.println("The page could not be loaded! @goBack");
        }
    }
}
