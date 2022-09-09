package org.fazioMonchieri.controllers;

import org.fazioMonchieri.utilities.Controller;
import org.fazioMonchieri.data.ImplSessioneDAO;
import org.fazioMonchieri.models.Sessione;
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

    
    //Info seggio
    @FXML
    private Label Cap;

    @FXML
    private Label Citta;

    @FXML
    private Label Id;

    @FXML
    private Label Provincia;

    @FXML
    private Label Via;

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
        Cap.setText(seggio.getCAP());

        Citta.setText(seggio.getCitta());;
    
       
        Id.setText(""+seggio.getId());
    

        Provincia.setText(seggio.getProvincia());;
    
        Via.setText(seggio.getVia());

        
        ImplSessioneDAO sessioneDAO = ImplSessioneDAO.getInstance();

        List<Sessione> sessioni = sessioneDAO.getOpenSession();
        Iterator<Sessione> is = sessioni.iterator();
 
        sessionName.setCellValueFactory(new PropertyValueFactory<Sessione,String>("nome"));
        
        date.setCellValueFactory(new PropertyValueFactory<Sessione,Date>("dataApertura"));
        
        while(is.hasNext()){
            sessionTable.getItems().add(is.next());
        }

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

    
}
