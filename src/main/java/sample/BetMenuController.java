package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import user.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

public class BetMenuController extends SceneController {

    private final ObservableList<Button> games = FXCollections.observableArrayList();
    private final ArrayList<ObservableList<Pane>> allMatches = new ArrayList<>();
    private final ArrayList<Odd> currentOdds = new ArrayList<>();
    private final ObservableList<Pane> currentBetPanes = FXCollections.observableArrayList();
    private float currentBetTotalOdd = 1.0f;

    @FXML
    private ListView<Button> listView;
    @FXML
    private ListView<Pane> listViewMatches;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button placeBetButton;
    @FXML
    private Label balance;
    @FXML
    private TextField betAmmount;
    @FXML
    private Label betLabel;

    public void initialize() {
        balance.setText("Balance: " + currentUser.getBalance());
        betAmmount.setText("0.0");
        ArrayList<League> leagues = currentUser.getLeagues();

        constructScene(leagues);

        ///
        placeBetButton = new Button("Place Bet");
        placeBetButton.setLayoutX(632);
        placeBetButton.setLayoutY(820);
        placeBetButton.setPrefWidth(571);
        placeBetButton.setPrefHeight(31);
        placeBetButton.setVisible(false);
        placeBetButton.setOnAction(this::placeBet);
        anchorPane.getChildren().add(placeBetButton);
        anchorPane.getChildren().add(constructBetButton());
        ///

        listView.setItems(games);

    }

    //needed to convert to 2 decimals
    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    /**
     * The function places a bet
     */
    @FXML
    private void placeBet(ActionEvent actionEvent) {
        float betAmmountFloat = Float.parseFloat(betAmmount.getText());
        if (betAmmountFloat > currentUser.getBalance()) {
            betLabel.setText("You do not have enough money!");
        } else if (betAmmountFloat > 0 && !currentOdds.isEmpty()) {
            Bet currBet = new Bet(currentOdds, betAmmountFloat, round(currentBetTotalOdd, 2), new java.sql.Date(System.currentTimeMillis()), false);

            currentUser.getBets().add(currBet);
            sqlConnection.insertBet(currBet, currentUser);
            currentUser.setBalance(currentUser.getBalance() - betAmmountFloat);
            sqlConnection.updateBalance(currentUser);

            try {
                changeScene(actionEvent, "/MainMenu.fxml");
            } catch (IOException ioException) {
                System.out.println("The file could not be loaded. @placeBet");
            }
        }
    }

    /**
     * @return the Button that shows the current Bet
     */
    @FXML
    private Button constructBetButton() {
        Button Bet = new Button("Bet");
        Bet.setLayoutX(408);
        Bet.setLayoutY(0);
        Bet.setPrefWidth(220);
        Bet.setPrefHeight(31);
        Bet.setOnAction(actionEvent -> {
            listViewMatches.setItems(currentBetPanes);
            placeBetButton.setVisible(true);
        });

        return Bet;
    }

    /**
     * The function sets (adds) a new Bet to the current Bet & listView of Bets
     */
    @FXML
    public void setBet(Match match, int betIndex, Label betLabel) {
        Odd odd = new Odd(match.getOdd(betIndex).getOdd(), match.getTeam1(), match.getTeam2(), betLabel.getText(), match.getDateTime());
        Odd oddToRemove = null;

        for (Odd o : currentOdds) {
            if ((o.getTeam2().equals(odd.getTeam2()) && o.getTeam1().equals(odd.getTeam1()) &&
                    o.getOdd() == odd.getOdd())) {

                //remove the pane
                currentBetPanes.remove(currentOdds.indexOf(o));

                //remove the oddValue
                currentBetTotalOdd /= o.getOdd();

                //remove the odd !!
                currentOdds.remove(o);

                //returning
                return;

            } else if (o.getTeam1().equals(odd.getTeam1()) && o.getTeam2().equals(odd.getTeam2())) {
                //remove the pane
                currentBetPanes.remove(currentOdds.indexOf(o));

                //remove the oddValue
                currentBetTotalOdd /= o.getOdd();

                //remove the odd !!
                oddToRemove = o;

                //not returning
            }
        }

        if (oddToRemove != null)
            currentOdds.remove(oddToRemove);

        Pane pane = new Pane();
        Text teams = new Text(odd.getTeam1() + " - " + odd.getTeam2());
        teams.setLayoutY(25);
        teams.setStyle(
                "-fx-font: 24 arial;"
        );

        Label betL = new Label(betLabel.getText());
        Label oddL = new Label(String.valueOf(odd.getOdd()));

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

        pane.setStyle(
                "-fx-background-color: #948f8a;" +
                        "-fx-background-radius: 0;"
        );
        pane.getChildren().add(teams);
        pane.getChildren().add(betL);
        pane.getChildren().add(oddL);

        currentBetPanes.add(pane);
        currentOdds.add(odd);
        currentBetTotalOdd *= odd.getOdd();
    }

    /**
     * The function construct the whole Scene
     */
    @FXML
    private void constructScene(ArrayList<League> leagues) {
        for (League l : leagues) {
            games.add(constructButton(leagues, l));

            ListView<Pane> currentMatches = new ListView<>();

            ObservableList<Pane> matches = FXCollections.observableArrayList();
            for (Match m : l.getGames()) {
                matches.add(constructPane(m));
            }

            currentMatches.setItems(matches);
            allMatches.add(matches);
        }
    }

    /**
     * @return a Pane that corresponds to the Match m
     */
    @FXML
    private Pane constructPane(Match m) {
        String unpickedColor = "#fc9803";
        String pickedColor = "#1e9e71";

        Pane currentPane = new Pane();
        String teamsString = m.getTeam1() + " - " + m.getTeam2();
        Text teams = new Text(teamsString);
        teams.setLayoutY(25);
        teams.setStyle(
                "-fx-font: 24 arial;"
        );

        Text dateAndTime = new Text(m.getDateTime());
        dateAndTime.setLayoutY(40);
        dateAndTime.setStyle(
                "-fx-font: 12 arial;"
        );

        Button bet3 = new Button(String.valueOf(m.getOdd(1).getOdd()));
        Button bet1 = new Button(String.valueOf(m.getOdd(0).getOdd()));
        Button bet2 = new Button(String.valueOf(m.getOdd(2).getOdd()));


        Label odd1 = new Label("1");
        odd1.setLayoutY(10);
        odd1.setLayoutX(680);
        Label oddX = new Label("X");
        oddX.setLayoutY(10);
        oddX.setLayoutX(810);
        Label odd2 = new Label("2");
        odd2.setLayoutY(10);
        odd2.setLayoutX(940);

        odd1.setStyle(
                "-fx-font: 24 arial;"
        );
        odd2.setStyle(
                "-fx-font: 24 arial;"
        );
        oddX.setStyle(
                "-fx-font: 24 arial;"
        );


        bet1.setLayoutX(700);
        bet1.setLayoutY(5);
        bet1.setPrefHeight(30);
        bet1.setPrefWidth(100);

        bet2.setLayoutX(830);
        bet2.setLayoutY(5);
        bet2.setPrefHeight(30);
        bet2.setPrefWidth(100);

        bet3.setLayoutX(960);
        bet3.setLayoutY(5);
        bet3.setPrefHeight(30);
        bet3.setPrefWidth(100);

        bet1.setOnAction(e -> bet1.setStyle(
                "-fx-background-color: #131441;"
        ));

        bet1.setStyle(
                "-fx-background-color: " + unpickedColor + ";" +
                        "-fx-background-radius: 0;"
        );
        bet2.setStyle(
                "-fx-background-color: " + unpickedColor + ";" +
                        "-fx-background-radius: 0;"
        );
        bet3.setStyle(
                "-fx-background-color: " + unpickedColor + ";" +
                        "-fx-background-radius: 0;"
        );

        bet1.setOnAction(e -> {
            setBet(m, 0, odd1);
            if (bet1.getStyle().contains(unpickedColor))
                bet1.setStyle(
                        "-fx-background-color: " + pickedColor + ";"
                );
            else
                bet1.setStyle(
                        "-fx-background-color: " + unpickedColor + ";"
                );
            bet2.setStyle(
                    "-fx-background-color: " + unpickedColor + ";"
            );
            bet3.setStyle(
                    "-fx-background-color: " + unpickedColor + ";"
            );
        });
        bet2.setOnAction(e -> {
            setBet(m, 1, oddX);
            if (bet2.getStyle().contains(unpickedColor))
                bet2.setStyle(
                        "-fx-background-color: " + pickedColor + ";"
                );
            else
                bet2.setStyle(
                        "-fx-background-color: " + unpickedColor + ";"
                );
            bet1.setStyle(
                    "-fx-background-color: " + unpickedColor + ";"
            );
            bet3.setStyle(
                    "-fx-background-color: " + unpickedColor + ";"
            );
        });
        bet3.setOnAction(e -> {
            setBet(m, 2, odd2);
            if (bet3.getStyle().contains(unpickedColor))
                bet3.setStyle(
                        "-fx-background-color: " + pickedColor + ";"
                );
            else
                bet3.setStyle(
                        "-fx-background-color: " + unpickedColor + ";"
                );
            bet2.setStyle(
                    "-fx-background-color: " + unpickedColor + ";"
            );
            bet1.setStyle(
                    "-fx-background-color: " + unpickedColor + ";"
            );
        });


        currentPane.setStyle(
                "-fx-background-color: #948f8a;" +
                        "-fx-background-radius: 0;"
        );
        currentPane.setPrefHeight(listView.getFixedCellSize());
        currentPane.getChildren().add(dateAndTime);
        currentPane.getChildren().add(teams);
        currentPane.getChildren().add(bet1);
        currentPane.getChildren().add(bet2);
        currentPane.getChildren().add(bet3);
        currentPane.getChildren().add(odd1);
        currentPane.getChildren().add(oddX);
        currentPane.getChildren().add(odd2);

        return currentPane;
    }

    /**
     * @return a Button that has all the matches&odds of the current League l
     */
    @FXML
    private Button constructButton(ArrayList<League> leagues, League l) {
        Button button = new Button(l.getName());
        button.setPrefWidth(340);
        button.setStyle(
                "-fx-background-color: #16b55d;"
        );
        button.setOnAction(actionEvent -> {
            listViewMatches.setItems(allMatches.get(leagues.indexOf(l)));
            placeBetButton.setVisible(false);
        });

        return button;
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
