package com.clyderunners;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class LoginController {

    //we set variable to be volatile to have accessing and modifying the same variable in UI Thread and Background Thread
    @FXML
    private VBox  mainVboxLogin;

    @FXML
    private PasswordField  passwordField;
    @FXML
    private Label msg;

    @FXML
    private Button loginButton;
    private  int  fail =2;

    private void accessToApp(){
        try {
            // load root with our main.fxml file
            Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("main.fxml"))));
            // assign stage with current Stage , so we need get windows (Stage)
            // so our parent Node (Root) is mainVboxLogin first we get scene then we get window then we Cast it into Stage
            Stage stage = (Stage) ((mainVboxLogin).getScene().getWindow());
            stage.setTitle("Clyde Runners");
            //then set Scene with new root that we loaded
            Scene scene = new Scene(root);
            //scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            //then set scene
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loginFromUI() {

        if(fail==0) {
            System.out.println("blocked less then 3 times" + fail);
            loginButton.setDisable(true);
            passwordField.clear();
            passwordField.setDisable(true);
            msg.setTextFill(Color.RED);
            msg.setText("Number of attempts exceeded. You are now locked out");

        }else if (passwordField.getText().equals("password") ){
            accessToApp();
        }else{
            passwordField.clear();
            msg.setText("Your Password is incorrect");
            System.out.println("Your Password is incorrect");
            fail--;
        }
    }



    // event when user click on keyboard 'ENTER' while typing password (focs was in passwordField)
    public void handelOnKeyPressed(KeyEvent keyEvent){
        if(keyEvent.getCode().equals( KeyCode.ENTER)){
            loginFromUI();
        }

    }



}
