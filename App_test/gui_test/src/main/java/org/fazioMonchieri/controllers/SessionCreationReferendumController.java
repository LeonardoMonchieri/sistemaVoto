package org.fazioMonchieri.controllers;

import org.fazioMonchieri.models.Sessione;
import org.fazioMonchieri.utilities.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class SessionCreationReferendumController extends Controller{

    private Sessione sessione;

    @FXML
    private TextField question;


    @Override
    public void onNavigateFrom(Controller sender, Object parameter) {
        this.sessione = (Sessione) parameter;
    }

    @FXML
    public void createSession(){
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
        //Create Referendum into db 
        navigate("GestoreView", this.sessione.getGestore());
    }


    @FXML
    public void cancelSession(){
        //Delete Session from db
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText("Creazione sessione avvenuta con successo");
        alert.setContentText("Verrai reinderizzato alla tua pagina gestore");
        alert.showAndWait();
        navigate("GestoreView", this.sessione.getGestore());
    }
    
}
