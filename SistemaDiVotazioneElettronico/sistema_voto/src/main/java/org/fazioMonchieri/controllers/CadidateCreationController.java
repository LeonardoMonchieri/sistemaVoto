package org.fazioMonchieri.controllers;

import java.nio.charset.spi.CharsetProvider;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.fazioMonchieri.data.ImplCandidatoDAO;
import org.fazioMonchieri.data.ImplPartitoDAO;
import org.fazioMonchieri.data.ImplSessioneDAO;
import org.fazioMonchieri.models.Gestore;
import org.fazioMonchieri.models.Partito;
import org.fazioMonchieri.utilities.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class CadidateCreationController extends Controller{

    private Gestore gestore;

    @FXML
    private ChoiceBox<String> party;

    @FXML
    private ChoiceBox<String> role;

    @FXML
    private TextField codiceFiscale;
    
    @Override
    public void onNavigateFrom(Controller sender, Object parameter) {
        this.gestore = (Gestore) parameter;
    }

    @Override
    public void init() {
        ImplPartitoDAO partitoDAO = ImplPartitoDAO.getInstance();
        List<Partito> partiti = partitoDAO.getPartiti();
        
        for(Partito p : partiti){
            party.getItems().add(p.getNome());
        }

        role.getItems().add("Segretario");
        role.getItems().add("Membro del comitato centrale");
        role.getItems().add("Membro del congresso");
        role.getItems().add("Membro dell'ufficio politico");

    }

    public void create(){

        Alert warningAlert = new Alert(AlertType.WARNING);
        String error=""; 
        String cf = codiceFiscale.getText().toString().replaceAll("\\s", "").toUpperCase();
        String p = party.getValue();
        String r = role.getValue();

        

        if(cf.length()!=16 || cf==null) error+="Inserisci un codice fiscale valido\n";
        if(p.isEmpty() || p==null) error+= "Seleziona un partito\n";
        if(r.isEmpty() || r==null) error+= "Seleziona un ruolo\n";
        if(!error.isEmpty()){
            warningAlert.setTitle("Errore di inserimento");
            warningAlert.setHeaderText("Campi mancanti:");
            warningAlert.setContentText(error);
            warningAlert.showAndWait();
            return;
        }

        ImplCandidatoDAO candidatoDAO = ImplCandidatoDAO.getInstance();
        try {
            candidatoDAO.createCandidato(r,cf,p);
        } catch (IllegalArgumentException e) {
            warningAlert.setTitle("Errore di inserimento");
            warningAlert.setHeaderText("Il candidato appartiene giá ad un altro partito");
            warningAlert.showAndWait();
            return;
        }
        
      
        //Crea candidato 
        Alert infoAlert = new Alert(AlertType.INFORMATION);
        infoAlert.setHeaderText("Candidato creato");
        infoAlert.setContentText("Il candidato é stato creato verrai reinderizzato alla tua pagina di gestione");
        infoAlert.showAndWait();

        navigate("GestoreView", this.gestore);
    }

    public void back(){
        navigate("GestoreView", this.gestore);
    }

}
