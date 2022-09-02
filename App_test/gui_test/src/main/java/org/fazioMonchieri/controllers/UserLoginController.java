package org.fazioMonchieri.controllers;


import org.fazioMonchieri.utilities.Controller;
import org.fazioMonchieri.models.Gestore;
import org.fazioMonchieri.models.Elettore;


import org.fazioMonchieri.data.ImplElettoreDAO;
import org.fazioMonchieri.data.ImplGestoreDAO;



import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

import javafx.scene.control.ChoiceBox;


public class UserLoginController extends Controller{

    ImplElettoreDAO elettoreDAO;

    ImplGestoreDAO gestoreDAO;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @FXML
    private ChoiceBox<String> Usertype;

    @Override
    public void init() {
        Usertype.getItems().clear();
        Usertype.getItems().add("Gestore");
        Usertype.getItems().add("Elettore");

        elettoreDAO = ImplElettoreDAO.getInstance();
        gestoreDAO = ImplGestoreDAO.getInstance();
    }

    

    @FXML
    void login() throws Exception {
        
        String user = username.getText().toString().replaceAll("\\s", "");
        String pw = password.getText().toString().replaceAll("\\s", "");
        String userType = Usertype.getValue();

        Alert alert = new Alert(AlertType.WARNING);
        
        if(user.length()==0 && pw.length()==0){
            alert.setContentText("Inserisci delle credenziali");
            alert.showAndWait();
            return;
        }else if(user.length()==0){
            alert.setContentText("Inserisci un username");
            alert.showAndWait();
        }
        else if(pw.length()==0){
            alert.setContentText("Inserisci la password");
            alert.showAndWait();
        }
        
        boolean isGestore=false;

        if(userType.equals("Gestore")) isGestore = true;
        else isGestore=false;
        
        /*
         * Login dbManager
         * Persona persona = db.login(user, pw, isGestore)
         */
       
        if(isGestore) {
            //Login
            Gestore gestore= gestoreDAO.loginGestore(user, pw);
            if(gestore==null){
                alert.setHeaderText("Errore durante la fase di accesso");
                alert.setContentText("Credenziali GESTORE non valide");
                alert.showAndWait();
                return;
            }
            
            navigate("GestoreView", gestore);
        }else{

            Elettore elettore=elettoreDAO.loginElettore(user, pw);

            if(elettore==null){
                alert.setHeaderText("Errore durante la fase di accesso");
                alert.setContentText("Credenziali ELETTORE non valide");
                alert.showAndWait();
            }
            navigate("ElettoreView", elettore);   
        }
        return;
    }


    @FXML
    public void home(){
        navigate("HomeView",null);
    }
}