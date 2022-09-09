package org.fazioMonchieri.controllers;

import java.util.Optional;

import org.fazioMonchieri.data.ImplReferendumDAO;
import org.fazioMonchieri.data.ImplSeggioDAO;
import org.fazioMonchieri.data.ImplSessioneDAO;
import org.fazioMonchieri.models.Gestore;
import org.fazioMonchieri.models.Sessione;
import org.fazioMonchieri.utilities.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class SessionCreationReferendumController extends Controller{

    private Sessione sessione;

    private ImplSessioneDAO sessioneDAO;

    @FXML
    private TextField question;


    @Override
    public void onNavigateFrom(Controller sender, Object parameter) {
        this.sessione = (Sessione) parameter;
        sessioneDAO = ImplSessioneDAO.getInstance();
    }

    @FXML
    public void createSession(){

        ImplReferendumDAO referendumDAO = ImplReferendumDAO.getInstance();;

        String q = question.getText().toString();

        Alert warning_alert = new Alert(AlertType.WARNING);
        if(q.isEmpty()){
            warning_alert.setContentText("Inserisci un quesito valido");
            warning_alert.showAndWait();
            return;
        }
        if(!q.substring(q.length() - 1).equals("?")){
            warning_alert.setContentText("Porre il quesito in forma di domanda");
            warning_alert.showAndWait();
            return;

        }
        referendumDAO.createReferendum(this.sessione.getId(), q);
        navigate("GestoreView", sessioneDAO.getGestore(this.sessione.getId()));
    }


    @FXML
    public void cancelSession(){

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Annullamento creazione della sessione");
        alert.setContentText("Vuoi annullare la creazione della sessione?");

        
        ButtonType btnYes = new ButtonType("Sí");
        ButtonType btnNo = new ButtonType("no");
        alert.getButtonTypes().setAll(btnYes,btnNo);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnYes){
            Gestore g=sessioneDAO.getGestore(this.sessione.getId());
            sessioneDAO.delete(this.sessione.getId());
            navigate("GestoreView", g);
            return;
        } else {
            return;
        }

    
        
    }
    
}
