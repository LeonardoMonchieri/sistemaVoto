package org.fazioMonchieri.controllers;

import org.fazioMonchieri.App;
import org.fazioMonchieri.utilities.Controller;
import org.fazioMonchieri.models.Persona;
import org.fazioMonchieri.models.Sessione;
import org.fazioMonchieri.data.DbManager;


import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;



public class SessionLoginController extends Controller{

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField password;

    @FXML
    private Label result;

    @FXML
    private TextField identifier;

    

    @FXML
    void handleButtonLog() throws Exception {
        
        String id = identifier.getText().toString().replaceAll("\\s", "");
        String pw = password.getText().toString().replaceAll("\\s", "");
        
        if(id.length()==0 || pw.length()==0){
            result.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
            result.setText("Inserisci le credenziali");   
            return;
        }
        DbManager db = DbManager.getInstance();    
        
        Sessione sessione = db.loginSession(id, pw);
        if(sessione==null){
            result.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
            result.setText("credenziali NON valide");
        }else{
            navigate("LocalSessionView", sessione);   
        }
        return;
    }
}