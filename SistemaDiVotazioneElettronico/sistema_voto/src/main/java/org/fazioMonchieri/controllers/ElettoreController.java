package org.fazioMonchieri.controllers;

import org.fazioMonchieri.utilities.Controller;
import org.fazioMonchieri.models.Persona;
import org.fazioMonchieri.data.ImplElettoreDAO;
import org.fazioMonchieri.models.Elettore;
import org.fazioMonchieri.models.Sessione;

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
    }

    @Override
    public void init() {
        ImplElettoreDAO elettoreDAO = ImplElettoreDAO.getInstance();
        Persona persona = elettoreDAO.getPersona(this.elettore.getCodiceFiscale());

        List<Sessione> sessioni = elettoreDAO.getSessioniElettore(this.elettore.getId());
        Iterator<Sessione> is = sessioni.iterator();
 
        sessionName.setCellValueFactory(new PropertyValueFactory<Sessione,String>("nome"));
        
        date.setCellValueFactory(new PropertyValueFactory<Sessione,Date>("dataApertura"));

        date.setCellFactory(column -> {
            TableCell<Sessione, Date> cell = new TableCell<Sessione, Date>() {
                private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        
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
                param.add(this.elettore);
                param.add(newSelection);
                navigate("VotingView",param);
            }
        });

    
        name.setText( persona.getNome());
        surname.setText( persona.getCognome());
        cf.setText( this.elettore.getCodiceFiscale());
    }

    @FXML
    public void logOut() {
        navigate("HomeView");
    }
 
}
