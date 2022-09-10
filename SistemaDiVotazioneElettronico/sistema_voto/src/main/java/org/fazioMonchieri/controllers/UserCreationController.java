package org.fazioMonchieri.controllers;

import java.sql.SQLException;
import java.util.List;

import org.fazioMonchieri.data.ImplElettoreDAO;
import org.fazioMonchieri.models.Gestore;
import org.fazioMonchieri.utilities.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class UserCreationController extends Controller{
    
    private Gestore gestore;

   
    
    @FXML
    private Label result;

    @FXML
    private TextField codiceFiscale;

    @FXML
    private TextField name;

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @Override
    public void onNavigateFrom(Controller sender, Object parameter) {
        this.gestore = (Gestore) parameter;
    }

    @Override
    public void init() {

    }

    @FXML
    public void create(){
        String cf = codiceFiscale.getText().toString().toUpperCase();;
        String un = username.getText().toString();
        String pw = password.getText().toString();

        Alert warningAllert = new Alert(AlertType.WARNING);


     
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

        if(cf.length()!=16 || cf.equals(null)){
            warningAllert.setTitle("Errore di inserimento");
            warningAllert.setHeaderText("Inserisci un codice fiscale valido");
            warningAllert.showAndWait();
            return;
        }
        
        if(un.isEmpty() || un.equals(null)){
            warningAllert.setTitle("Errore di inserimento");
            warningAllert.setHeaderText("Inserisci un username valido");
            warningAllert.showAndWait();
            return;
        }

        if(pw.length()<5 || pw.isEmpty() || !numFlag || !charFlag){
            warningAllert.setTitle("Errore di inserimento");
            warningAllert.setHeaderText("Inserisci una password valida ");   
            warningAllert.showAndWait();
            return;
        }

        ImplElettoreDAO elettoreDAO = ImplElettoreDAO.getInstance();
        
        try{//Check se esite giá utente
            elettoreDAO.createElettore(un, pw, cf);
        }catch(SQLException e){
            warningAllert.setTitle("Errore di inserimento");
            warningAllert.setHeaderText("Utente giá registrato ");   
            warningAllert.showAndWait();
            navigate("GestoreView", this.gestore);
        } 

        //Crea Utente
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText("Creazione utente avvenuta con successo");
        alert.setContentText("Verrai reinderizzato alla tua pagina gestore");
        alert.showAndWait();

        navigate("GestoreView", this.gestore);
    }

    @FXML
    public void back(){
        navigate("GestoreView", this.gestore);
    }
}
