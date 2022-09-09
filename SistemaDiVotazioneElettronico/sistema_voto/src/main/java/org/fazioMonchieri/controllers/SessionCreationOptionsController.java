package org.fazioMonchieri.controllers;

import org.fazioMonchieri.models.Sessione;
import org.fazioMonchieri.models.TipoScrutinio;
import org.fazioMonchieri.models.TipoSessione;
import org.fazioMonchieri.models.Partito;
import org.fazioMonchieri.data.ImplCandidatoDAO;
import org.fazioMonchieri.data.ImplPartitoDAO;
import org.fazioMonchieri.data.ImplSessioneDAO;
import org.fazioMonchieri.data.ImplVotaCandidatoDAO;
import org.fazioMonchieri.data.ImplVotaPartitoDAO;
import org.fazioMonchieri.data.PartitoDAO;
import org.fazioMonchieri.data.VotaCandidatoDAO;
import org.fazioMonchieri.models.Candidato;
import org.fazioMonchieri.models.Gestore;
import org.fazioMonchieri.models.Persona;

import org.fazioMonchieri.utilities.Controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;

import javafx.util.Callback;

import java.util.HashSet;
import java.util.Set;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Date;
import java.util.Iterator;

public class SessionCreationOptionsController extends Controller {

    private ImplSessioneDAO sessioneDAO;

    private ImplCandidatoDAO candidatoDAO;

    private ImplPartitoDAO partitoDAO;

    private ImplVotaCandidatoDAO votaCandidatoDAO;

    private ImplVotaPartitoDAO votaPartitoDAO;

    private Sessione sessione;

    private Set<Candidato> selectedCandidate;

    private Set<Partito> selectedParty;

    @FXML
    private CheckBox groupParty;

    @FXML
    private VBox vBoxTableOptions;

    @FXML
    private VBox vBoxTableChoice;

    @FXML
    private TableView optionsTable;

    @FXML
    private TableView choiceTable;

    @Override
    public void onNavigateFrom(Controller sender, Object parameter) {
        this.sessione = (Sessione) parameter;
        sessioneDAO = ImplSessioneDAO.getInstance();
        candidatoDAO = ImplCandidatoDAO.getInstance();
        partitoDAO = ImplPartitoDAO.getInstance();
        votaCandidatoDAO = ImplVotaCandidatoDAO.getInstance();
        votaPartitoDAO = ImplVotaPartitoDAO.getInstance();
    }

    @Override
    public void init() {
        if (sessione.getTipoSessione() == TipoSessione.votoCategoricoPreferenza)
            groupParty.setDisable(true);
        optionsCandidateTabeleBuilder();
        selectedCandidateTabeleBuilder();
        selectedCandidate = new HashSet<Candidato>();
        selectedParty = new HashSet<Partito>();
    }

    @FXML
    public void partyCandidateSelection() {
        if (optionsTable.getId().equals("candidateTable")) {
            vBoxTableOptions.getChildren().remove(optionsTable);
            vBoxTableChoice.getChildren().remove(choiceTable);
            optionsPartyTableBuilder();
            selectedPartyTabeleBuilder();
            selectedParty.clear();
        } else {
            vBoxTableOptions.getChildren().remove(optionsTable);
            vBoxTableChoice.getChildren().remove(choiceTable);
            optionsCandidateTabeleBuilder();
            selectedCandidateTabeleBuilder();
            selectedCandidate.clear();
        }
    }

    @FXML
    public void createSession() {
        Alert warning_alert = new Alert(AlertType.WARNING);

        if (optionsTable.getId().equals("candidateTable")) {
            if(selectedCandidate.size()<2  ){
                warning_alert.setContentText("Inserisci almeno 2 candidati");
                warning_alert.showAndWait();
                return;
            }
            for(Candidato c : selectedCandidate){
                votaCandidatoDAO.createVotaCandidato(this.sessione.getId(), c.getId());
            }
            if (sessione.getTipoSessione() == TipoSessione.votoCategoricoPreferenza) {
                getCandidateParty();
                if(selectedParty.size()<2){
                    warning_alert.setContentText("Inserisci almeno 2 partiti");
                    warning_alert.showAndWait();
                    return;
                }
                for(Partito p : selectedParty){
                    votaPartitoDAO.createVotaPartito(this.sessione.getId(), p.getId());
                }
            }
        } else {
            if(selectedParty.size()<2){
                warning_alert.setContentText("Inserisci almeno 2 partiti");
                warning_alert.showAndWait();
                return;
            }
            for(Partito p : selectedParty){
                votaPartitoDAO.createVotaPartito(this.sessione.getId(), p.getId());
            }
        } 
        navigate("GestoreView", sessioneDAO.getGestore(this.sessione.getId()));
    }

    @FXML
    public void cancelSession(){

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Annullamento creazione della sessione");
        alert.setContentText("Vuoi annullare la creazione della sessione?");

        
        ButtonType btnYes = new ButtonType("Sí");
        ButtonType btnNo = new ButtonType("no");
        alert.getButtonTypes().setAll(btnYes,btnNo);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnYes){
            Gestore g=sessioneDAO.getGestore(this.sessione.getId());
            sessioneDAO.delete(this.sessione.getId());
            navigate("GestoreView", g);
            return;
        } else {
            return;
        }        
    }

    // Table maker

    // Candidate option Table
    private void optionsCandidateTabeleBuilder() {
        List<Candidato> c = candidatoDAO.getCandidati();
        Iterator<Candidato> ic = c.iterator();

        optionsTable = new TableView<Candidato>();

        TableColumn<Candidato, String> partito = new TableColumn<>("Partito/Gruppo");

        partito.setCellValueFactory(param -> new SimpleObjectProperty(candidatoDAO.getPartito(param.getValue().getId()).getNome()));

        TableColumn<Candidato, String> candidato = new TableColumn<>("Candidato");

        candidato.setCellValueFactory(
            param -> new SimpleObjectProperty<>(candidatoDAO.getNomeCompleto(param.getValue().getId())));

        addButtonToTableCandidate();

        optionsTable.getColumns().add(partito);
        optionsTable.getColumns().add(candidato);

        while (ic.hasNext()) {
            optionsTable.getItems().add(ic.next());
        }

        optionsTable.setColumnResizePolicy(optionsTable.CONSTRAINED_RESIZE_POLICY);
        optionsTable.setPrefHeight(400);
        optionsTable.setPrefWidth(400);
        optionsTable.setId("candidateTable");
        vBoxTableOptions.getChildren().add(optionsTable);
    }

    // Party option table
    private void optionsPartyTableBuilder() {
        List<Partito> p = partitoDAO.getPartiti(); 
        Iterator<Partito> ip = p.iterator();

        optionsTable = new TableView<Partito>();
        TableColumn<Partito, String> partito = new TableColumn<>("Partito/Gruppo");

        partito.setCellValueFactory(new PropertyValueFactory<Partito, String>("nome"));

        addButtonToTableParty();

        optionsTable.getColumns().add(partito);

        while (ip.hasNext()) {
            optionsTable.getItems().add(ip.next());
        }

        optionsTable.setColumnResizePolicy(optionsTable.CONSTRAINED_RESIZE_POLICY);
        optionsTable.setPrefHeight(400);
        optionsTable.setPrefWidth(400);
        optionsTable.setId("partyTable");
        vBoxTableOptions.getChildren().add(optionsTable);
    }

    // Candidate selected table
    public void selectedCandidateTabeleBuilder() {
        choiceTable = new TableView<Candidato>();

        TableColumn<Candidato, String> partito = new TableColumn<>("Partito/Gruppo");

        partito.setCellValueFactory(param -> new SimpleObjectProperty(candidatoDAO.getPartito(param.getValue().getId()).getNome()));

        TableColumn<Candidato, String> candidato = new TableColumn<>("Candidato");

        candidato.setCellValueFactory(
            param -> new SimpleObjectProperty<>(candidatoDAO.getNomeCompleto(param.getValue().getId())));

        subButtonToTableCandidate();

        choiceTable.getColumns().add(partito);
        choiceTable.getColumns().add(candidato);

        choiceTable.setColumnResizePolicy(optionsTable.CONSTRAINED_RESIZE_POLICY);
        choiceTable.setPrefHeight(400);
        choiceTable.setPrefWidth(400);
        choiceTable.setId("candidateChoiceTable");
        vBoxTableChoice.setMargin(choiceTable, new Insets(40.0,10.0,0.0,0.0));
        vBoxTableChoice.getChildren().add(choiceTable);
        

    }

    // Party selected table
    public void selectedPartyTabeleBuilder() {
        choiceTable = new TableView<Partito>();

        TableColumn<Partito, String> partito = new TableColumn<>("Partito/Gruppo");
        partito.setCellValueFactory(new PropertyValueFactory<Partito, String>("nome"));

        subButtonToTableParty();

        choiceTable.getColumns().add(partito);

        choiceTable.setColumnResizePolicy(optionsTable.CONSTRAINED_RESIZE_POLICY);
        choiceTable.setPrefHeight(400);
        choiceTable.setPrefWidth(400);
        choiceTable.setId("partyChoiceTable");
        vBoxTableChoice.setMargin(choiceTable, new Insets(40.0,10.0,0.0,0.0));
        vBoxTableChoice.getChildren().add(choiceTable);

    }

    // Table button
    private void addButtonToTableCandidate() {
        TableColumn<Candidato, Void> colBtn = new TableColumn("Add");

        Callback<TableColumn<Candidato, Void>, TableCell<Candidato, Void>> cellFactory = new Callback<TableColumn<Candidato, Void>, TableCell<Candidato, Void>>() {
            @Override
            public TableCell<Candidato, Void> call(final TableColumn<Candidato, Void> param) {
                final TableCell<Candidato, Void> cell = new TableCell<Candidato, Void>() {

                    private final Button btn = new Button("+");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Candidato data = getTableView().getItems().get(getIndex());
                            getTableView().getItems().remove(getIndex());
                            selectedCandidate.add(data);
                            choiceTable.getItems().add(data);
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

        optionsTable.getColumns().add(colBtn);

    }

    private void addButtonToTableParty() {
        TableColumn<Partito, Void> colBtn = new TableColumn<Partito, Void>("Add");

        Callback<TableColumn<Partito, Void>, TableCell<Partito, Void>> cellFactory = new Callback<TableColumn<Partito, Void>, TableCell<Partito, Void>>() {
            @Override
            public TableCell<Partito, Void> call(final TableColumn<Partito, Void> param) {
                final TableCell<Partito, Void> cell = new TableCell<Partito, Void>() {

                    private final Button btn = new Button("+");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Partito data = getTableView().getItems().get(getIndex());
                            getTableView().getItems().remove(getIndex());
                            selectedParty.add(data);
                            choiceTable.getItems().add(data);
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

        optionsTable.getColumns().add(colBtn);

    }

    private void subButtonToTableCandidate() {
        TableColumn<Candidato, Void> colBtn = new TableColumn<Candidato, Void>("Remove");

        Callback<TableColumn<Candidato, Void>, TableCell<Candidato, Void>> cellFactory = new Callback<TableColumn<Candidato, Void>, TableCell<Candidato, Void>>() {
            @Override
            public TableCell<Candidato, Void> call(final TableColumn<Candidato, Void> param) {
                final TableCell<Candidato, Void> cell = new TableCell<Candidato, Void>() {

                    private final Button btn = new Button("-");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Candidato data = getTableView().getItems().get(getIndex());
                            getTableView().getItems().remove(getIndex());
                            selectedCandidate.remove(data);
                            optionsTable.getItems().add(data);
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

        choiceTable.getColumns().add(colBtn);

    }

    private void subButtonToTableParty() {
        TableColumn<Partito, Void> colBtn = new TableColumn<Partito,Void>("Remove");

        Callback<TableColumn<Partito, Void>, TableCell<Partito, Void>> cellFactory = new Callback<TableColumn<Partito, Void>, TableCell<Partito, Void>>() {
            @Override
            public TableCell<Partito, Void> call(final TableColumn<Partito, Void> param) {
                final TableCell<Partito, Void> cell = new TableCell<Partito, Void>() {

                    private final Button btn = new Button("-");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Partito data = getTableView().getItems().get(getIndex());
                            getTableView().getItems().remove(getIndex());
                            selectedParty.remove(data);
                            optionsTable.getItems().add(data);
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

        choiceTable.getColumns().add(colBtn);

    }

    public void getCandidateParty() {
        for (Candidato c : selectedCandidate) {
            selectedParty.add(candidatoDAO.getPartito(c.getId()));
        }
    }

}
