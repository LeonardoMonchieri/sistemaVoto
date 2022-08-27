package org.fazioMonchieri.controllers;

import org.fazioMonchieri.models.Sessione;
import org.fazioMonchieri.models.TipoScrutinio;
import org.fazioMonchieri.models.TipoSessione;
import org.fazioMonchieri.models.Partito;
import org.fazioMonchieri.models.Candidato;
import org.fazioMonchieri.models.Persona;

import org.fazioMonchieri.utilities.TableViewUtils;
import org.fazioMonchieri.utilities.Controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
import java.util.Date;
import java.util.Iterator;

public class SessionCreationOptionsController extends Controller {

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
        System.out.println(optionsTable);
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

            selectedCandidatePrint();
            // Add Voti candiato
            if (sessione.getTipoSessione() == TipoSessione.votoCategoricoPreferenza) {
                getCandidateParty();
                if(selectedParty.size()<2){
                    warning_alert.setContentText("Inserisci almeno 2 partiti");
                    warning_alert.showAndWait();
                    return;
                }
                selectedPartyPrint();
                // Add voti partito
            }
        } else {
            if(selectedParty.size()<2){
                warning_alert.setContentText("Inserisci almeno 2 partiti");
                warning_alert.showAndWait();
                return;
            }
            selectedPartyPrint();
            // Add voti partito
        }
       
        navigate("GestoreView", sessione.getGestore());
    }

    @FXML
    public void cancelSession(){
        //Delete Session from db
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText("Creazione avvenuta con successo");
        alert.setContentText("Verrai reinderizzato alla tua pagina gestore");
        alert.showAndWait();
        navigate("GestoreView", this.sessione.getGestore());
    }

    // Table maker

    // Candidate option Table
    private void optionsCandidateTabeleBuilder() {
        List<Candidato> c = getCandidati();
        Iterator<Candidato> ic = c.iterator();

        optionsTable = new TableView<Candidato>();

        TableColumn<Candidato, String> partito = new TableColumn<>("Partito/Gruppo");

        partito.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPartito().getNome()));

        TableColumn<Candidato, String> candidato = new TableColumn<>("Candidato");

        candidato.setCellValueFactory(
                param -> new SimpleObjectProperty<>(param.getValue().getPersona().getCompleteName()));

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
        List<Partito> p = getPartito(0);
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

        partito.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPartito().getNome()));

        TableColumn<Candidato, String> candidato = new TableColumn<>("Candidato");

        candidato.setCellValueFactory(
                param -> new SimpleObjectProperty<>(param.getValue().getPersona().getCompleteName()));

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
        TableColumn<Partito, Void> colBtn = new TableColumn("Add");

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
        TableColumn<Candidato, Void> colBtn = new TableColumn("Remove");

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
        TableColumn<Partito, Void> colBtn = new TableColumn("Remove");

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
            selectedParty.add(c.getPartito());
        }
    }

    // DB Manager
    public List<Candidato> getCandidati() {
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

    public List<Partito> getPartito(int i) {
        List<Partito> p = new ArrayList<>();

        Partito p1 = new Partito("1234", "Lega Nord", new Date());
        Partito p2 = new Partito("4321", "Partito Democratico", new Date());
        Partito p3 = new Partito("5678", "Forza Italia", new Date());

        p.add(p1);
        p.add(p2);
        p.add(p3);
        if (i == 0)
            return p;

        return null;

    }

    //TEST
    public void selectedCandidatePrint(){
        System.out.println("CANDIDATI:");
        for(Candidato c : selectedCandidate){
            System.out.println("-"+c.getPersona().getCompleteName());
        }
        return;
    }


    public void selectedPartyPrint(){
        System.out.println("PARTIT:");
        for(Partito p : selectedParty){
            System.out.println("-"+p.getNome());
        }
        return;
    }

}
