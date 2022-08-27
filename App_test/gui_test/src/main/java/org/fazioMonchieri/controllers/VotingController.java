package org.fazioMonchieri.controllers;

import org.fazioMonchieri.utilities.Controller;
import org.fazioMonchieri.utilities.SchedaElettorale;
import org.fazioMonchieri.utilities.SchedaElettoraleCandidato;
import org.fazioMonchieri.utilities.SchedaElettoraleCategoricoPreferenza;

import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;


import org.fazioMonchieri.models.Candidato;
import org.fazioMonchieri.models.Partito;
import org.fazioMonchieri.models.Persona;
import org.fazioMonchieri.models.Referendum;
import org.fazioMonchieri.models.Sessione;
import org.fazioMonchieri.models.TipoSessione;

import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class VotingController extends Controller {

    private Persona persona;

    private Sessione sessione;

    private List<Candidato> selectedCandidate;

    private List<Partito> selectedparty;

    private Boolean selectedOption;


    @FXML
    private Label title;

    @FXML
    private Label question;

    @FXML
    private HBox hBoxOptions;

    @FXML
    private TableView<Candidato> candidateTable;

    @FXML
    private TableView<Partito> partyTeble;

    @FXML
    private Button yesBtn;

    @FXML
    private Button noBtn;

    @FXML
    private Button voteBtn;

    @Override
    public void onNavigateFrom(Controller sender, Object parameter) {
        ArrayList<Object> params = (ArrayList<Object>) parameter;
        this.persona = (Persona) params.get(0);
        this.sessione = (Sessione) params.get(1);
    }

    @Override
    public void init() {
        
        if (sessione.getTipoSessione() == TipoSessione.votoCategorico
                || sessione.getTipoSessione() == TipoSessione.votoOrdinale) {
            if (getCandidati() != null) {
                selectedCandidate = new ArrayList<>();
                buildCandidateTable(null);
                
            } else {
                selectedparty = new ArrayList<>();
                buildPartyTable();
            }
        } else if (sessione.getTipoSessione() == TipoSessione.referendum) {

            question.setText(getRefQuesito(this.sessione.getId()));

            yesBtn.setDisable(false);
            yesBtn.setOpacity(1);

            noBtn.setDisable(false);
            noBtn.setOpacity(1);

        } else {
            selectedCandidate = new ArrayList<>();
            selectedparty = new ArrayList<>();
            buildPartyTable();
            buildCandidateTable(null);
        }
        
    }

    @FXML
    public void vote(){
        if (sessione.getTipoSessione() == TipoSessione.votoCategorico) {
            //Registra scheda elettorale
        } else if (sessione.getTipoSessione() == TipoSessione.votoCategoricoPreferenza) {
            //Registra scheda elettorale
        } else if (sessione.getTipoSessione() == TipoSessione.votoOrdinale) {
            //Registra scheda elettorale
        } else if (sessione.getTipoSessione() == TipoSessione.referendum) {
            //Registra scheda elettorale
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Voto completato");
        alert.setContentText("Il voto é stato registrato");
        alert.showAndWait();
        
        navigate("HomeView");
    }

   

    // Table builder

    private void buildCandidateTable(Partito filter) {
        Optional<Partito> pt = Optional.ofNullable(filter);
        List<Candidato> c = getCandidati();
        Iterator<Candidato> ic = c.iterator();

        candidateTable = new TableView<Candidato>();

        TableColumn<Candidato, String> partito = new TableColumn<>("Partito/Gruppo");

        partito.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPartito().getNome()));

        TableColumn<Candidato, String> candidato = new TableColumn<>("Candidato");

        candidato.setCellValueFactory(
                param -> new SimpleObjectProperty<>(param.getValue().getPersona().getCompleteName()));

        voteButtonToTableCandidate();

        candidateTable.getColumns().add(partito);
        candidateTable.getColumns().add(candidato);

        while (ic.hasNext()) {
            Candidato nextCandidato = ic.next();
            if(pt.isPresent()){
                if(nextCandidato.getPartito().equals(filter)){
                    candidateTable.getItems().add(nextCandidato);
                }
            }else{
                candidateTable.getItems().add(nextCandidato);
            }
            
        }

        candidateTable.setColumnResizePolicy(candidateTable.CONSTRAINED_RESIZE_POLICY);
        candidateTable.setPrefHeight(400);
        candidateTable.setPrefWidth(400);
        candidateTable.setId("candidateTable");
        hBoxOptions.getChildren().add(candidateTable);
        hBoxOptions.setMargin(candidateTable, new Insets(10.0,10.0,0.0,10.0));
    }

    // Party option table
    private void buildPartyTable() {
        List<Partito> p = getPartito();
        Iterator<Partito> ip = p.iterator();

        partyTeble = new TableView<Partito>();
        TableColumn<Partito, String> partito = new TableColumn<>("Partito/Gruppo");

        partito.setCellValueFactory(new PropertyValueFactory<Partito, String>("nome"));

        voteButtonToTableParty();

        partyTeble.getColumns().add(partito);

        while (ip.hasNext()) {
            partyTeble.getItems().add(ip.next());
        }

        partyTeble.setColumnResizePolicy(partyTeble.CONSTRAINED_RESIZE_POLICY);
        partyTeble.setPrefHeight(400);
        partyTeble.setPrefWidth(400);
        partyTeble.setId("partyTable");
        hBoxOptions.getChildren().add(partyTeble);
        hBoxOptions.setMargin(partyTeble, new Insets(10.0,10.0,0.0,10.0));
    }

    // Table button
    private void voteButtonToTableCandidate() {
        TableColumn<Candidato, Void> colBtn = new TableColumn("Vote");
        Callback<TableColumn<Candidato, Void>, TableCell<Candidato, Void>> cellFactory = new Callback<TableColumn<Candidato, Void>, TableCell<Candidato, Void>>() {
            @Override
            public TableCell<Candidato, Void> call(final TableColumn<Candidato, Void> param) {
                ImageView circle = new ImageView(new Image(getClass().getResourceAsStream("/Image/circle.png")));
                circle.setFitHeight(25);
                circle.setPreserveRatio(true);
                final TableCell<Candidato, Void> cell = new TableCell<Candidato, Void>() {

                    Button btn = new Button("", circle);
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            ImageView circleX = new ImageView(
                                    new Image(getClass().getResourceAsStream("/Image/circleX.png")));
                            circleX.setFitHeight(25);
                            circleX.setPreserveRatio(true);

                            Candidato c = getTableView().getItems().get(getIndex());

                            btn.setGraphic(circleX);
                            //Voting
                            if(!selectedCandidate.contains(c)) selectedCandidate.add(c);
                            if(sessione.getTipoSessione()==TipoSessione.votoCategorico){
                                
                                candidateTable.setDisable(true);
                                candidateTable.setOpacity(1);
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);

        candidateTable.getColumns().add(colBtn);

    }

    private void voteButtonToTableParty() {
        TableColumn<Partito, Void> colBtn = new TableColumn("Vote");

        Callback<TableColumn<Partito, Void>, TableCell<Partito, Void>> cellFactory = new Callback<TableColumn<Partito, Void>, TableCell<Partito, Void>>() {
            @Override
            public TableCell<Partito, Void> call(final TableColumn<Partito, Void> param) {
                ImageView circle = new ImageView(new Image(getClass().getResourceAsStream("/Image/circle.png")));
                circle.setFitHeight(25);

                circle.setPreserveRatio(true);
                final TableCell<Partito, Void> cell = new TableCell<Partito, Void>() {

                    Button btn = new Button("", circle);

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            ImageView circleX = new ImageView(
                                    new Image(getClass().getResourceAsStream("/Image/circleX.png")));
                            circleX.setFitHeight(25);
                            circleX.setPreserveRatio(true);
                            btn.setGraphic(circleX);
                            Partito p = getTableView().getItems().get(getIndex());
                            //Vote
                            if(!selectedparty.contains(p)) selectedparty.add(p);
                            if (sessione.getTipoSessione() == TipoSessione.votoCategoricoPreferenza) {
                                partyTeble.setDisable(true);
                                partyTeble.setOpacity(1);
                                hBoxOptions.getChildren().remove(candidateTable);
                                buildCandidateTable(p);
                            }
                            else if(sessione.getTipoSessione() == TipoSessione.votoCategorico){
                                partyTeble.setDisable(true);
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);

        partyTeble.getColumns().add(colBtn);

    }

    @FXML
    private void voteYes(){
        selectedOption = true;
    }

    @FXML
    private void voteNo(){
        selectedOption = false;
    }


    // DB Test

    // DB managaer function
    public String getRefQuesito(String sessionId) {
        Referendum refTest = new Referendum(this.sessione, "Vuoi abolire la schiavitú?");
        return refTest.getQuesito();
    }

    public static List<Candidato> getCandidati() {
        List<Candidato> c = new ArrayList<>();
        Partito p1 = new Partito("1234", "Lega Nord", new Date());
        Partito p2 = new Partito("4321", "Partito Democratico", new Date());
        Partito p3 = new Partito("5678", "Forza Italia", new Date());

        Persona pr1 = new Persona("SLVMTT73C09F205R", true, "Matteo", "Salvini", new Date(3, 9, 1973), "MI");
        Persona pr2 = new Persona("MLNGRG77A55H501C", false, "Giorgia", "Meloni", new Date(15, 1, 1977), "RM");
        Persona pr3 = new Persona("LTTNRC66P20H501D", true, "Enrico", "Letta", new Date(20, 8, 1966), "RM");

        Candidato c1 = new Candidato("1234", "Segretario", pr1, p1);
        Candidato c2 = new Candidato("4321", "Segretario", pr2, p2);
        Candidato c3 = new Candidato("5678", "Segretario", pr3, p3);

        c.add(c1);
        c.add(c2);
        c.add(c3);

        return c;
    }

    public static List<Partito> getPartito() {
        List<Partito> p = new ArrayList<>();

        Partito p1 = new Partito("1234", "Lega Nord", new Date());
        Partito p2 = new Partito("4321", "Partito Democratico", new Date());
        Partito p3 = new Partito("5678", "Forza Italia", new Date());

        p.add(p1);
        p.add(p2);
        p.add(p3);

        return p;

    }

}
