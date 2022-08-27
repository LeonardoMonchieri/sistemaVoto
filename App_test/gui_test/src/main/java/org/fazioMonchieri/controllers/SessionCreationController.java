package org.fazioMonchieri.controllers;

import org.fazioMonchieri.utilities.Controller;
import org.fazioMonchieri.models.Gestore;
import org.fazioMonchieri.models.Sessione;
import org.fazioMonchieri.models.TipoScrutinio;
import org.fazioMonchieri.models.TipoSessione;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.beans.value.ObservableValue;

public class SessionCreationController extends Controller {

    private Gestore gestore;

    @FXML
    private TextField sessionTitle;

    @FXML
    private ChoiceBox<TipoSessione> sessionType;

    @FXML
    private ChoiceBox<TipoScrutinio> sessionBallot;

    @Override
    public void onNavigateFrom(Controller sender, Object parameter) {
        this.gestore = (Gestore) parameter;
    }

    @Override
    public void init() {
        sessionType.getItems().add(null);
        sessionType.getItems().add(TipoSessione.referendum);
        sessionType.getItems().add(TipoSessione.votoCategorico);
        sessionType.getItems().add(TipoSessione.votoCategoricoPreferenza);
        sessionType.getItems().add(TipoSessione.votoOrdinale);

        sessionBallot.getItems().add(null);
        sessionBallot.getItems().add(TipoScrutinio.maggioranza);
        sessionBallot.getItems().add(TipoScrutinio.maggioranzaAssoluta);
        sessionBallot.getItems().add(TipoScrutinio.referendumConQuorum);
        sessionBallot.getItems().add(TipoScrutinio.referendumSenzaQuorum);
        

        sessionType.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends TipoSessione> ov, TipoSessione old_val, TipoSessione new_val) -> {
                    updateBallot(new_val);
                });

        sessionBallot.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends TipoScrutinio> ov, TipoScrutinio old_val, TipoScrutinio new_val) -> {
                    updateSession(new_val);
                });
    }

    @FXML 
    public void handleButtonContinue(){
        TipoScrutinio selectedBalot = sessionBallot.getValue();
        TipoSessione  selectedType = sessionType.getValue();
        String nome = sessionTitle.getText().toString().replaceAll("\\s", "");
        Alert alert = new Alert(AlertType.WARNING);

        String msg ="";
        //createSession
        if(nome.isEmpty() || nome.equals(null)){
            msg+="Inserisci un titolo valido\n";
        }
        if(selectedType==null){
            msg+="Inserisci un tipo di sessione valido\n";
            
        }
        if(selectedBalot==null){
            msg+="Inserisci un metodo di balotaggio valido\n";
        }
        if(selectedBalot==null ||  selectedType==null || nome.isEmpty() || nome.equals(null)){
            alert.setHeaderText("Errore durante la creazione della sessione");
            alert.setContentText(msg);
            alert.showAndWait();
            return;
        }
        Sessione sessione = new Sessione("dada", nome, "dasda ", selectedType, selectedBalot, this.gestore);
        
        if(selectedType==TipoSessione.referendum) navigate("SessionCreationReferendumView", sessione);
        else navigate("SessionCreationOptionsView", sessione);
    }

    @FXML
    public void back() {
        navigate("GestoreView", this.gestore);
    }

    private void updateBallot(TipoSessione selectedType) {

        if(selectedType==null){
            sessionBallot.getItems().clear();
            sessionBallot.getItems().add(null);
            sessionBallot.getItems().add(TipoScrutinio.maggioranza);
            sessionBallot.getItems().add(TipoScrutinio.maggioranzaAssoluta);
            sessionBallot.getItems().add(TipoScrutinio.referendumConQuorum);
            sessionBallot.getItems().add(TipoScrutinio.referendumSenzaQuorum);
            return;

        }

        if (selectedType == TipoSessione.referendum) {
            sessionBallot.getItems().remove(TipoScrutinio.maggioranza);
            sessionBallot.getItems().remove(TipoScrutinio.maggioranzaAssoluta);
            if( sessionBallot.getItems().contains(TipoScrutinio.referendumConQuorum) &&  sessionBallot.getItems().contains(TipoScrutinio.referendumSenzaQuorum)) return;
            sessionBallot.getItems().add(TipoScrutinio.referendumConQuorum);
            sessionBallot.getItems().add(TipoScrutinio.referendumSenzaQuorum);
            return;
        }
        sessionBallot.getItems().remove(TipoScrutinio.referendumConQuorum);
        sessionBallot.getItems().remove(TipoScrutinio.referendumSenzaQuorum);
        if( sessionBallot.getItems().contains(TipoScrutinio.maggioranza) &&  sessionBallot.getItems().contains(TipoScrutinio.maggioranzaAssoluta)) return;
        sessionBallot.getItems().add(TipoScrutinio.maggioranza);
        sessionBallot.getItems().add(TipoScrutinio.maggioranzaAssoluta);
        
        
        return;

    }

    private void updateSession(TipoScrutinio selectedBallot) {
        
        if(selectedBallot==null){
            sessionType.getItems().clear();
            sessionType.getItems().add(null);
            sessionType.getItems().add(TipoSessione.referendum);
            sessionType.getItems().add(TipoSessione.votoCategorico);
            sessionType.getItems().add(TipoSessione.votoCategoricoPreferenza);
            sessionType.getItems().add(TipoSessione.votoOrdinale);
            return;

        }

        if (selectedBallot == TipoScrutinio.maggioranzaAssoluta || selectedBallot == TipoScrutinio.maggioranza) {
            sessionType.getItems().remove(TipoSessione.referendum);
            
            if( sessionType.getItems().contains(TipoSessione.votoCategoricoPreferenza) &&  
            sessionType.getItems().contains(TipoSessione.votoOrdinale) &&
            sessionType.getItems().contains(TipoSessione.votoCategorico)
            ) return;
            sessionType.getItems().add(TipoSessione.votoCategorico);
            sessionType.getItems().add(TipoSessione.votoCategoricoPreferenza);
            sessionType.getItems().add(TipoSessione.votoOrdinale);

            return;
        }
        sessionType.getItems().remove(TipoSessione.votoCategorico);
        sessionType.getItems().remove(TipoSessione.votoCategoricoPreferenza);
        sessionType.getItems().remove(TipoSessione.votoOrdinale);
        if( sessionType.getItems().contains(TipoSessione.referendum)) return;
        sessionType.getItems().add(TipoSessione.referendum);
        return;

    }

    

}
