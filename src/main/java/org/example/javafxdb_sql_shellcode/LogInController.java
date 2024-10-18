package org.example.javafxdb_sql_shellcode;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.example.javafxdb_sql_shellcode.db.ConnDbOps;

import java.net.URL;
import java.util.ResourceBundle;

public class LogInController {
    public TextField logInPassword;
    public TextField logInEmail;
    public Text logInError;
    public Button logInBtn;

    private static final ConnDbOps cdbop = new ConnDbOps();;
    public Button themeToggle;

    public int currentUserId;

    /**
     * Test login info with the database.
     * @return True or False if log in was successful.
     */
    public boolean logInQuery() {
        String email = logInEmail.getText();
        String pass = logInPassword.getText();
        if (email.isEmpty()) {logInError.setText("Email field is empty.");logInError.setVisible(true);return false;}
        else if (pass.isEmpty()) {logInError.setText("Password field is empty.");logInError.setVisible(true);return false;}

        //ToDo remove this override -
        if(logInEmail.getText().equals("admin") && logInPassword.getText().equals("1234")) {
            return true;
        }
        currentUserId = cdbop.userLogIn(email, pass);
        if(currentUserId!=-1){
            logInError.setVisible(false);
            return true;
        }
        else{
            logInError.setVisible(true);
            logInError.setText("Email or password were incorrect.");
            return false;
        }
    }
}
