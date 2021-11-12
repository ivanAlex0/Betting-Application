package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import user.User;

import javafx.scene.control.TextField;

import javafx.event.ActionEvent;

import javax.management.ValueExp;
import java.io.IOException;
import java.util.ArrayList;


public class SignUpController extends SceneController {

    @FXML
    private TextField _username;
    @FXML
    private TextField _password;
    @FXML
    private TextField _CNP;
    @FXML
    private TextField _name;
    @FXML
    private Label _usernameError;
    @FXML
    private Label _passwordError;
    @FXML
    private Label _CNPError;
    @FXML
    private Label _nameError;


    /**
     * The function realizes the SignUp action
     */
    @FXML
    private void SignUp() {
        this.initLabels();
        String username, password, CNP, name;

        ArrayList<User> users = sqlConnection.getUsers();

        username = _username.getText();
        password = _password.getText();
        CNP = _CNP.getText();
        name = _name.getText();

        boolean usernameCheck1 = true, usernameCheck2 = true, passwordCheck = true,
                CNPCheck1 = true, CNPCheck2 = true;

        if (username.length() < 6) {
            usernameCheck1 = false;
        } else {
            for (User u : users) {
                if (username.equals(u.getUsername())) {
                    usernameCheck2 = false;
                    break;
                }
            }
        }

        if (password.length() < 6) {
            passwordCheck = false;
        }
        if (!CNP.matches("[0-9]+")) {
            CNPCheck1 = false;
        }
        if (CNP.length() != 13) {
            CNPCheck2 = false;
        }

        if (!usernameCheck2) {
            _usernameError.setText("Username is already taken");
        } else if (!usernameCheck1) {
            _usernameError.setText("Username must be  at least 6 characters long");
        } else if (!passwordCheck) {
            _passwordError.setText("Password must be at least 6 characters long!");
        } else if (!CNPCheck1) {
            _CNPError.setText("Your CNP must contain only digits!");
        } else if (!CNPCheck2) {
            _CNPError.setText("Your CNP must be 13 characters long");
        } else {
            User u = new User(username, password, CNP, name, 0.0f, false, scraping.getLeagues(), null, users.size());
            sqlConnection.insertUser(u);
            sqlConnection.setUsers();
            //corrected because no history was fetched #gotoSignIn currentUser = u;
            try {
                changeScene(actionEvent, "/SignIn.fxml");
            } catch (IOException ioException) {
                System.out.println("IO exception while trying to changeScene");
            }
        }

    }

    /**
     * The function initializes all the Labels with "" = empty
     */
    @FXML
    private void initLabels() {
        this._usernameError.setText("");
        this._passwordError.setText("");
        this._CNPError.setText("");
        this._nameError.setText("");
    }

    @FXML
    private void goToSignIn(ActionEvent actionEvent) throws IOException {
        changeScene(actionEvent, "/SignIn.fxml");
    }
}
