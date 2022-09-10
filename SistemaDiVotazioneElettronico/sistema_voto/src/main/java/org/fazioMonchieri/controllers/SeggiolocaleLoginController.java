package org.fazioMonchieri.controllers;

import org.fazioMonchieri.data.ImplSeggioDAO;
import org.fazioMonchieri.models.Seggio;
import org.fazioMonchieri.utilities.Controller;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;



public class SeggiolocaleLoginController extends Controller{
    
    private ImplSeggioDAO seggioDAO; 

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField password;

    @FXML
    private Label result;

    @FXML
    private TextField seggioId;

    public void init() {
        
    }
    

    @FXML
    void login() throws Exception {

        Alert alert = new Alert(AlertType.WARNING);
        Integer id=-1;
        try{
            id = Integer.parseInt(seggioId.getText());
        }catch(NumberFormatException nf){
            alert.setContentText("Inserisci un id valido");
            alert.showAndWait();
            return;
        }
       
        String pw = password.getText().toString();

        Boolean numFlag = false;
        Boolean charFlag = false;

        
        for(int i=0;i<=pw.length();i++){
            if(Character.isDigit(pw.charAt(i)) && !numFlag){
                numFlag=true;
            }
            if(Character.isAlphabetic(pw.charAt(i)) && !charFlag){
                charFlag=true;
            }
        }

        if(pw.length()<5){
            alert.setContentText("Inserisci password valida");
            alert.showAndWait();
            return;
        }
        if(id==-1){
            alert.setContentText("Inserisci un id valido");
            alert.showAndWait();
            return;
        }
        
        seggioDAO = ImplSeggioDAO.getInstance();
        Seggio seggio= seggioDAO.loginSeggio(id, pw);

        if(seggio==null){
            alert.setContentText("Credenziali NON valide");
            alert.showAndWait();
            return;
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