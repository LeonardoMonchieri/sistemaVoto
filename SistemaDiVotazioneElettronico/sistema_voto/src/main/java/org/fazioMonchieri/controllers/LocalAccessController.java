package org.fazioMonchieri.controllers;

import java.util.ArrayList;

import org.fazioMonchieri.data.ImplElettoreDAO;
import org.fazioMonchieri.data.ImplReferendumDAO;
import org.fazioMonchieri.data.ImplVotaDAO;
import org.fazioMonchieri.models.Elettore;
import org.fazioMonchieri.models.Seggio;
import org.fazioMonchieri.models.Sessione;
import org.fazioMonchieri.utilities.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LocalAccessController extends Controller{

    private Seggio seggio;

    private Sessione sessione;

    @FXML
    private Label result;

    @FXML
    private Label sessionTitle;

    @FXML
    private TextField cf;


    @Override
    public void onNavigateFrom(Controller sender, Object parameter){
        ArrayList<Object> params = (ArrayList<Object>) parameter;
        this.sessione = (Sessione) params.get(0);
        this.seggio = (Seggio) params.get(1);
    }

    @Override
    public void init() {
        sessionTitle.setText(this.sessione.getNome());
    }

    @FXML
    void login() throws Exception {
        
        ImplVotaDAO votaDAO = ImplVotaDAO.getInstance();
        ImplElettoreDAO elettoreDAO = ImplElettoreDAO.getInstance();


        String CF = cf.getText().toString().replaceAll("\\s", "");
        
        if(CF.length()==0){
            result.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
            result.setText("Inserisci codice fiscale valido");   
            return;
        }
        Elettore elettore = elettoreDAO.getElettore(CF);
        boolean check=votaDAO.hasVoted(elettore.getId(),this.sessione.getId());

        if(!check){
            ArrayList<Object> params = new ArrayList<>();
            params.add(elettore);
            params.add(this.sessione);
            navigate("votingView", params);
        }
        else{
            result.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
            result.setText("L'utente ha giá votato");
        }
        return;
    }

    @FXML
    public void back(){
        navigate("SeggioLocaleView", this.seggio);
    }

    //DB
    public boolean checkSessionAccess(String cf, String s_id){
        return true;
    }

    
    
}
