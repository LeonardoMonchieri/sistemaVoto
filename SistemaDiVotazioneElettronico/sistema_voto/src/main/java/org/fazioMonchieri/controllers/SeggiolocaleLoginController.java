package org.fazioMonchieri.controllers;

import org.fazioMonchieri.data.ImplSeggioDAO;
import org.fazioMonchieri.models.Seggio;
import org.fazioMonchieri.utilities.Controller;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
        
        Integer id = Integer.parseInt(seggioId.getText().toString().replaceAll("\\s", ""));
        String pw = password.getText().toString().replaceAll("\\s", "");
        
        if(id==null || pw.length()==0){
            result.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
            result.setText("Inserisci le credenziali");   
            return;
        }
        seggioDAO = ImplSeggioDAO.getInstance();
        Seggio seggio= seggioDAO.loginSeggio(id, pw);

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