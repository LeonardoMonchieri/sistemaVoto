package org.fazioMonchieri.controllers;

import org.fazioMonchieri.models.Gestore;
import org.fazioMonchieri.utilities.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class PartyCreationController extends Controller{
    
    private Gestore gestore;

    @FXML
    private TextField partyName;

    @Override
    public void onNavigateFrom(Controller sender, Object parameter){
        this.gestore = (Gestore) parameter;
    }

    @Override
    public void init() {
    }

    public void create(){
        String pn = partyName.getText().toString().replaceAll("\\s", "");
        Alert warningAllert = new Alert(AlertType.WARNING);
        
        if(pn.isEmpty() || pn==null ){
            warningAllert.setTitle("Errore di inserimento");
            warningAllert.setHeaderText("Inserisci un nome valido");
            warningAllert.showAndWait();
            return;
        }

        if(false){ //Esiste un partito con lo stesso nome
            warningAllert.setTitle("Errore durante la creazione");
            warningAllert.setHeaderText("Il partito é giá presente");
            warningAllert.showAndWait();
            return;
        }
        
        //Create party

        navigate("GestoreView", this.gestore);

    }

    public void back(){
        navigate("GestoreView", this.gestore);
    }


    
}
