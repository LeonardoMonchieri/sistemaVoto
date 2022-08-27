package org.fazioMonchieri.controllers;

import org.fazioMonchieri.utilities.Controller;
import org.fazioMonchieri.models.Persona;
import org.fazioMonchieri.models.Elettore;
import org.fazioMonchieri.models.Gestore;
import org.fazioMonchieri.models.Sessione;
import org.fazioMonchieri.models.TipoScrutinio;
import org.fazioMonchieri.models.TipoSessione;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Iterator;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;


public class ElettoreController  extends Controller{

    private Elettore elettore;

    private Persona persona;

    @FXML
    private Label name;

    @FXML
    private Label surname;

    @FXML
    private Label cf;


    @FXML
    private TableView<Sessione> sessionTable;

    @FXML
    private TableColumn<Sessione,String> sessionName;

    @FXML
    private TableColumn<Sessione,Date> date;

    @Override
    public void onNavigateFrom(Controller sender, Object parameter){
        this.elettore = (Elettore) parameter;
        this.persona = elettore.getPersona();
    }

    @Override
    public void init() {
        List<Sessione> sessioni = getSessioni();
        Iterator<Sessione> is = sessioni.iterator();
 
        sessionName.setCellValueFactory(new PropertyValueFactory<Sessione,String>("nome"));
        
        date.setCellValueFactory(new PropertyValueFactory<Sessione,Date>("dataApertura"));

        date.setCellFactory(column -> {
            TableCell<Sessione, Date> cell = new TableCell<Sessione, Date>() {
                private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        
                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if(item==null || empty) {
                        setText(null);
                    }
                    else {
                        setText(format.format(item));
                    }
                }
            };
        
            return cell;
        });

        
        while(is.hasNext()){
            sessionTable.getItems().add(is.next());
        }

        sessionTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                List<Object> param = new ArrayList<>();
                param.add(this.persona);
                param.add(newSelection);
                navigate("VotingView",param);
            }
        });

    
        name.setText("Nome: "+persona.getNome());
        surname.setText("Nome: "+persona.getCognome());
        cf.setText("Nome: "+persona.getCodiceFiscale());
    }

    @FXML
    public void logOut() {
        navigate("HomeView");
    }

    private List<Sessione> getSessioni(){

        Gestore g=new Gestore("fafasf", "Marione", "dasdad", this.persona);
        List<Sessione> s= new ArrayList<Sessione>();
        Sessione s1 = new Sessione("dasdea", "Referendum legalizzazione", "dasdad",  TipoSessione.referendum,TipoScrutinio.referendumConQuorum, g);
        Sessione s2 = new Sessione("lojk", "Elezioni categorico", "kljkl",TipoSessione.votoCategorico, TipoScrutinio.maggioranza,   g);
        Sessione s3 = new Sessione("lojk", "Elezioni categorico preferenziale", "kljkl",TipoSessione.votoCategoricoPreferenza, TipoScrutinio.maggioranza,   g);
        Sessione s4 = new Sessione("lojk", "Elezioni ordinale", "kljkl",TipoSessione.votoOrdinale, TipoScrutinio.maggioranza,   g);
        s1.setOpen();
        s2.setOpen();
        s1.setClose();
        s2.setClose();
        s.add(s1);
        s.add(s2);
        s.add(s3);
        s.add(s4);
        
        return s;
    }

    
    
 
}
