package org.fazioMonchieri.controllers;

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
        String cf = codiceFiscale.getText().toString().replaceAll("\\s", "");
        String un = username.getText().toString().replaceAll("\\s", "");
        String pw = password.getText().toString().replaceAll("\\s", "");

        if(cf.isEmpty() || cf.equals(null)){
            result.setText("Inserisci un codice fiscale valido");
            result.setTextFill(Color.RED);
            result.setFont(Font.font("System", 21));
        }
        
        if(un.isEmpty() || un.equals(null)){
            result.setText("Inserisci un username valido");
            result.setTextFill(Color.RED);
            result.setFont(Font.font("System", 21));
        }

        if(pw.length()==0 || pw.isEmpty() ){
            result.setStyle("-fx-text-fill: red; -fx-font-size: 20px;");
            result.setText("Inserisci una password valida ");   
            return;
        }

        
        if(false){//Check se esite giá utente
            navigate("GestoreView", this.gestore);
        } 

        //Crea Utente
        ImplElettoreDAO elettoreDAO = ImplElettoreDAO.getInstance();
        elettoreDAO.createElettore(un, pw, cf);
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
