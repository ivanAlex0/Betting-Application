package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class DepositController extends SceneController {

    @FXML
    private TextField _cardNumber;
    @FXML
    private Label _cardNumberError;

    @FXML
    private TextField _expireDate1;
    @FXML
    private TextField _expireDate2;
    @FXML
    private Label _expireDateError;

    @FXML
    private TextField _cardOwner;
    @FXML
    private Label _cardOwnerError;

    @FXML
    private TextField _CCV;
    @FXML
    private Label _CCVError;

    @FXML
    private TextField _ammount;
    @FXML
    private Label _ammountError;


    @FXML
    private void clearLabels() {
        _cardNumberError.setText("");
        _ammountError.setText("");
        _expireDateError.setText("");
        _cardOwnerError.setText("");
        _CCVError.setText("");
    }

    /**
     * The function makes a deposit for the currentUser
     */
    @FXML
    private void deposit() {

        if (!_cardNumber.getText().matches("[0-9]+")) {
            _cardNumberError.setText("Card number must contain only digits!");
        } else if (_cardNumber.getText().length() != 16) {
            _cardNumberError.setText("Card number must have 16 digits");
        } else if (_expireDate1.getText().length() != 2 || !_expireDate1.getText().matches("[0-9]+") || Integer.parseInt(_expireDate1.getText()) > 12) {
            _expireDateError.setText("Expire date is incorrect!");
        } else if (_expireDate2.getText().length() != 2 || !_expireDate2.getText().matches("[0-9]+") || Integer.parseInt(_expireDate2.getText()) < 21) {
            _expireDateError.setText("Expire date is incorrect!");
        } else if (!_CCV.getText().matches("[0-9]+") || _CCV.getText().length() != 3) {
            _CCVError.setText("The CCV is incorrect!");
        } else if (!_ammount.getText().matches("[-+]?[0-9]*\\.?[0-9]+") || Integer.parseInt(_ammount.getText()) > 100) {
            _ammountError.setText("Ammount is incorrect! Max Ammount is 100");
        } else {
            currentUser.setBalance(currentUser.getBalance() + Float.parseFloat(_ammount.getText()));
            sqlConnection.updateBalance(currentUser);
            try {
                changeScene(actionEvent, "/MainMenu.fxml");
            } catch (IOException ioException) {
                System.out.println("The page could not be loaded! @goBackDeposit");
            }
        }
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
