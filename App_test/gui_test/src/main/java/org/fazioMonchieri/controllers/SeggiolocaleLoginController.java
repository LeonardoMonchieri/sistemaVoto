package org.fazioMonchieri.controllers;

import org.fazioMonchieri.App;
import org.fazioMonchieri.models.Seggio;
import org.fazioMonchieri.utilities.Controller;
//import org.fazioMonchieri.models.Sessione;

import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;



public class SeggiolocaleLoginController extends Controller{

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField password;

    @FXML
    private Label result;

    @FXML
    private TextField seggioId;

    

    @FXML
    void login() throws Exception {
        
        String id = seggioId.getText().toString().replaceAll("\\s", "");
        String pw = password.getText().toString().replaceAll("\\s", "");
        
        if(id.length()==0 || pw.length()==0){
            result.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
            result.setText("Inserisci le credenziali");   
            return;
        }

        /**
         * 
         * 
         * 
         */
        Seggio seggio=null;

        if(id.equals("seggioId") && pw.equals("1234")){
            seggio=new Seggio("seggio test");
        }
        if(seggio==null){
            result.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
            result.setText("credenziali NON valide");
        }else{
            navigate("SeggiolocaleView", seggio);   
        }
        return;
    }

    @FXML
    public void home(){
        navigate("HomeView",null);
    }
}