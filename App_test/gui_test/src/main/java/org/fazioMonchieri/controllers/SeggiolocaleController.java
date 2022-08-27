package org.fazioMonchieri.controllers;

import org.fazioMonchieri.utilities.Controller;
import org.fazioMonchieri.models.Persona;
import org.fazioMonchieri.models.Elettore;
import org.fazioMonchieri.models.Gestore;
import org.fazioMonchieri.models.Sessione;
import org.fazioMonchieri.models.TipoScrutinio;
import org.fazioMonchieri.models.TipoSessione;
import org.fazioMonchieri.models.Seggio;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Iterator;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;


public class SeggiolocaleController extends Controller {

    private Seggio seggio;

    /* 
    Info seggio
    @FXML
    private Label name;

    @FXML
    private Label surname;

    @FXML
    private Label cf;
    */

    @FXML
    private TableView<Sessione> sessionTable;

    @FXML
    private TableColumn<Sessione,String> sessionName;

    @FXML
    private TableColumn<Sessione,Date> date;

    @Override
    public void onNavigateFrom(Controller sender, Object parameter){
        this.seggio = (Seggio) parameter;
    }

    @Override
    public void init() {
        
        List<Sessione> sessioni = getOpenSession();
        Iterator<Sessione> is = sessioni.iterator();
 
        sessionName.setCellValueFactory(new PropertyValueFactory<Sessione,String>("nome"));
        
        date.setCellValueFactory(new PropertyValueFactory<Sessione,Date>("dataApertura"));
        
        while(is.hasNext()){
            sessionTable.getItems().add(is.next());
        }
        /*Set info seggio
         * 
         * name.setText("Nome: "+persona.getNome());
        surname.setText("Nome: "+persona.getCognome());
        cf.setText("Nome: "+persona.getCodiceFiscale());
         */

        sessionTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                ArrayList<Object> params = new ArrayList<>();
                params.add(newSelection);
                params.add(this.seggio);
                navigate("LocalAccessView",params);
            }
        });
        
    }

    @FXML
    public void logOut() {
        navigate("HomeView");
    }

    //Databse


    private List<Sessione> getOpenSession(){
        Persona testP=new Persona("BNCLNZ12M29H501H", true, "Lorenzo", "Bianchi",  new Date(29,8,2012), "RM");
        Gestore g=new Gestore("fafasf", "Marione", "dasdad", testP);
        List<Sessione> s= new ArrayList<Sessione>();
        Sessione s1 = new Sessione("dasdea", "Referendum legalizzazione", "dasdad",  TipoSessione.votoOrdinale,TipoScrutinio.maggioranza, g);
        Sessione s2 = new Sessione("lojk", "Elezioni provinciali", "kljkl",TipoSessione.votoOrdinale, TipoScrutinio.maggioranza,   g);
        s1.setOpen();
        s2.setOpen();
        s1.setClose();
        s2.setClose();
        s.add(s1);
        s.add(s2);
        return s;
    }

    
}
