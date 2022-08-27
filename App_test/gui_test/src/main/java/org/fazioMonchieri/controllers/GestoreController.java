package org.fazioMonchieri.controllers;

import org.fazioMonchieri.utilities.Controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Iterator;

import org.fazioMonchieri.models.Gestore;
import org.fazioMonchieri.models.Persona;
import org.fazioMonchieri.models.Sessione;
import org.fazioMonchieri.models.TipoScrutinio;
import org.fazioMonchieri.models.TipoSessione;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.Node;

public class GestoreController extends Controller {

    private Gestore gestore;
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
    private TableColumn<Sessione, String> sessionId;

    @FXML
    private TableColumn<Sessione, String> sessionPassword;

    @FXML
    private TableColumn<Sessione, String> sessionName;

    @FXML
    private TableColumn<Sessione, Date> sessionStartDate;

    @FXML
    private TableColumn<Sessione, Date> sessionEndDate;

    @FXML
    private TableColumn<Sessione, Integer> status;

    @Override
    public void onNavigateFrom(Controller sender, Object parameter) {
        this.gestore = (Gestore) parameter;
        this.persona = gestore.getPersona();
    }

    @Override
    public void init() {
        List<Sessione> sessioni = getSessioni();
        Iterator<Sessione> is = sessioni.iterator();

        String pattern = "dd/MM/yyyy HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);


        sessionId.setCellValueFactory(new PropertyValueFactory<Sessione, String>("id"));

        sessionPassword.setCellValueFactory(new PropertyValueFactory<Sessione, String>("password"));

        sessionName.setCellValueFactory(new PropertyValueFactory<Sessione, String>("nome"));

        sessionStartDate.setCellValueFactory(new PropertyValueFactory<Sessione, Date>("dataApertura"));
        
        sessionEndDate.setCellValueFactory(new PropertyValueFactory<Sessione, Date>("dataChiusura"));
        
        sessionEndDate.setCellFactory(column -> {
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

        sessionStartDate.setCellFactory(column -> {
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

        status.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().isClosed()));

        status.setCellFactory(col -> {

            TableCell<Sessione, Integer> cell = new TableCell<>();
            cell.itemProperty().addListener((obs, old, newVal) -> {
                if(newVal!=null){
                    Node graphic = createStatusDot(newVal);
                    cell.graphicProperty().bind(Bindings.when(cell.emptyProperty()).then((Node) null).otherwise(graphic));
                }
            });
            return cell;
        });

        while (is.hasNext()) {
            sessionTable.getItems().add(is.next());
        }

        sessionTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                System.out.println("\n\n\n"+newSelection.getId()+"\n\n\n");
                navigate("SessioneView",newSelection);
            }
        });

        name.setText("Nome: " + persona.getNome());
        surname.setText("Nome: " + persona.getCognome());
        cf.setText("Nome: " + persona.getCodiceFiscale());
    }

    

    private Node createStatusDot(Integer stato) {
        HBox graphicContainer = new HBox();
        graphicContainer.setAlignment(Pos.CENTER);

        if (stato != null) {
            ImageView imageView;
            if (stato == 0)
                imageView = new ImageView(new Image(getClass().getResourceAsStream("/Image/yellowCircle.png")));
            else if (stato == 1)
                imageView = new ImageView(new Image(getClass().getResourceAsStream("/Image/redCircle.png")));
            else
                imageView = new ImageView(new Image(getClass().getResourceAsStream("/Image/greenCircle.png")));
            imageView.setFitHeight(25);
            imageView.setPreserveRatio(true);
            graphicContainer.getChildren().add(imageView);

        }

        return graphicContainer;
    }

    @FXML
    public void logOut() {
        navigate("HomeView");
    }

    @FXML
    public void sessionCreation() {
        navigate("SessionCreationView",this.gestore);
    }

    @FXML
    public void userCreation(){
        navigate("UserCreationView",this.gestore);
    }

    @FXML
    public void candidateCreation(){
        navigate("CandidateCreationView",this.gestore);
    }

    @FXML
    public void partyCreation(){
        navigate("PartyCreationView",this.gestore);
    }


    private List<Sessione> getSessioni() {

        
        List<Sessione> s = new ArrayList<Sessione>();
        Sessione s1 = new Sessione("dasdea", "Elezione Creata", "dasdad", TipoSessione.votoOrdinale,
                TipoScrutinio.maggioranza, this.gestore);
        Sessione s2 = new Sessione("lojk", "Elezione Aperta", "kljkl", TipoSessione.referendum,
                TipoScrutinio.maggioranza, this.gestore);
        Sessione s3 = new Sessione("hgfd", "Elezione Chiusa", "dada", TipoSessione.votoCategoricoPreferenza,
                TipoScrutinio.maggioranza, this.gestore);

        s2.setOpen();

        s3.setOpen();
        s3.setClose();

        s.add(s1);
        s.add(s2);
        s.add(s3);
        return s;
    }
}
