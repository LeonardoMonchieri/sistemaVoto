package org.fazioMonchieri.controllers;

import org.fazioMonchieri.App;
import org.fazioMonchieri.utilities.Controller;
import org.fazioMonchieri.data.DbManager;
import org.fazioMonchieri.models.Persona;


import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;


public class UserLoginController extends Controller{

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField password;

    @FXML
    private Label result;

    @FXML
    private TextField username;

    @FXML
    private ChoiceBox<String> Usertype;

    @Override
    public void init() {
        Usertype.getItems().add("Gestore");
        Usertype.getItems().add("Elettore");
    }

    

    @FXML
    void handleButtonLog() throws Exception {
        
        String user = username.getText().toString().replaceAll("\\s", "");
        String pw = password.getText().toString().replaceAll("\\s", "");
        String userType = (String) Usertype.getValue();
        
        if(user.length()==0 || pw.length()==0){
            result.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
            result.setText("Inserisci le credenziali");   
            return;
        }
        DbManager db = DbManager.getInstance();
        
        boolean isGestore=false;

        if(userType.equals("Gestore")) isGestore = true;
        else isGestore=false;
        
        
        Persona persona = db.login(user, pw, isGestore);
        if(persona==null){
            result.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
            result.setText("credenziali NON valide");
        } 
        else if(isGestore) {
            navigate("GestoreView", persona);
        }else{
            navigate("ElettoreView", persona);   
        }
        return;
    }
}