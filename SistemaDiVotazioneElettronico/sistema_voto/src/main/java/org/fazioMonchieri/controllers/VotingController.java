package org.fazioMonchieri.controllers;

import org.fazioMonchieri.utilities.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.fazioMonchieri.data.ImplCandidatoDAO;
import org.fazioMonchieri.data.ImplPartitoDAO;
import org.fazioMonchieri.data.ImplReferendumDAO;
import org.fazioMonchieri.data.ImplSessioneDAO;
import org.fazioMonchieri.data.ImplVotaCandidatoDAO;
import org.fazioMonchieri.data.ImplVotaDAO;
import org.fazioMonchieri.data.ImplVotaPartitoDAO;
import org.fazioMonchieri.models.Candidato;
import org.fazioMonchieri.models.Elettore;
import org.fazioMonchieri.models.Partito;
import org.fazioMonchieri.models.Sessione;
import org.fazioMonchieri.models.TipoSessione;

import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class VotingController extends Controller {

    private Elettore elettore;

    private Sessione sessione;

    private List<Candidato> selectedCandidate;

    private List<Partito> selectedparty;

    private Boolean selectedOption;

    private ImplVotaPartitoDAO votaPartitoDAO ;

    private ImplVotaCandidatoDAO votaCandidatoDAO;

    private ImplPartitoDAO partitoDAO;

    private ImplReferendumDAO referendumDAO;

    private ImplSessioneDAO sessioneDAO;

    @FXML
    private Text title;

    @FXML
    private Text question;

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
        this.elettore = (Elettore) params.get(0);
        this.sessione = (Sessione) params.get(1);

        sessioneDAO = ImplSessioneDAO.getInstance();
    }

    @Override
    public void init() {
        title.setText(this.sessione.getNome());
        if (sessione.getTipoSessione() == TipoSessione.votoCategorico
                || sessione.getTipoSessione() == TipoSessione.votoOrdinale) {
            if (sessioneDAO.getCandidati(this.sessione.getId()) != null) {
                votaCandidatoDAO= ImplVotaCandidatoDAO.getInstance();
                selectedCandidate = new ArrayList<>();
                buildCandidateTable(null);
            } else {
                votaPartitoDAO= ImplVotaPartitoDAO.getInstance();
                selectedparty = new ArrayList<>();
                buildPartyTable();
            }
        } else if (sessione.getTipoSessione() == TipoSessione.referendum) {
            referendumDAO = ImplReferendumDAO.getInstance();
            question.setText(sessioneDAO.getQuesitoReferendum(this.sessione.getId()));

            yesBtn.setDisable(false);
            yesBtn.setOpacity(1);

            noBtn.setDisable(false);
            noBtn.setOpacity(1);

        } else {
            votaCandidatoDAO= ImplVotaCandidatoDAO.getInstance();
            votaPartitoDAO= ImplVotaPartitoDAO.getInstance();
            selectedCandidate = new ArrayList<>();
            selectedparty = new ArrayList<>();
            buildPartyTable();
            buildCandidateTable(null);
        }
        
    }

    @FXML
    public void exit(){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Uscita dalla sessione");
        alert.setContentText("Uscire dalla sessione?");

        
        ButtonType btnYes = new ButtonType("Sí");
        ButtonType btnNo = new ButtonType("no");
        alert.getButtonTypes().setAll(btnYes,btnNo);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnYes){
            navigate("HomeView");
            return;
        } else {
            return;
        }
    }

    @FXML
    public void vote() throws IOException{
        ImplVotaDAO votaDAO = ImplVotaDAO.getInstance();
        if(votaDAO.hasVoted(this.elettore.getId(), this.sessione.getId())){    
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("IMPOSSIBILE VOTARE");
            alert.setContentText("L'utente ha votato su un altro dispositivo");
            alert.showAndWait();

            navigate("HomeView");
            return;
        }
        
        if (sessione.getTipoSessione() == TipoSessione.votoCategorico) {
            if(selectedCandidate!=null) votaCandidatoDAO.votaCandidatoCategorico(this.sessione.getId(), selectedCandidate.get(0));
            else if(selectedparty!=null) votaPartitoDAO.votaPartitoCategorico(this.sessione.getId(), selectedparty.get(0));
        } else if (sessione.getTipoSessione() == TipoSessione.votoCategoricoPreferenza) {
            if(selectedparty!=null) votaPartitoDAO.votaPartitoCategorico(this.sessione.getId(),selectedparty.get(0));
            if(selectedCandidate!=null){
                for(Candidato c : selectedCandidate){
                    votaCandidatoDAO.votaCandidatoCategorico(this.sessione.getId(), c);
                }
            }
        } else if (sessione.getTipoSessione() == TipoSessione.votoOrdinale) {
            if(selectedCandidate!=null) votaCandidatoDAO.votaCandidatoOrdinale(this.sessione.getId(), selectedCandidate);
            else if(selectedparty!=null) votaPartitoDAO.votaPartitoOrdinale(this.sessione.getId(), selectedparty);
        } else if (sessione.getTipoSessione() == TipoSessione.referendum && selectedOption!=null) {   
            referendumDAO = ImplReferendumDAO.getInstance();   
            referendumDAO.votaReferendum(this.sessione.getId(), selectedOption);
        }


        sessioneDAO.votazione(this.elettore.getId(), this.sessione.getId());

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Voto completato");
        alert.setContentText("Il voto é stato registrato");
        alert.showAndWait();

        
        navigate("HomeView");
    }

   

    // Table builder

    private void buildCandidateTable(Partito filter) {
        Optional<Partito> pt = Optional.ofNullable(filter);
        List<Candidato> c = sessioneDAO.getCandidati(this.sessione.getId());
        Iterator<Candidato> ic = c.iterator();

        ImplCandidatoDAO candidatoDAO = ImplCandidatoDAO.getInstance();

        candidateTable = new TableView<Candidato>();

        TableColumn<Candidato, String> partito = new TableColumn<>("Partito/Gruppo");

        partito.setCellValueFactory(param -> new SimpleObjectProperty( candidatoDAO.getPartito(param.getValue().getId() ).getNome()));

        TableColumn<Candidato, String> candidato = new TableColumn<>("Candidato");

        candidato.setCellValueFactory(
                param -> new SimpleObjectProperty<>(candidatoDAO.getNomeCompleto(param.getValue().getId())));

        voteButtonToTableCandidate();

        candidateTable.getColumns().add(partito);
        candidateTable.getColumns().add(candidato);

        while (ic.hasNext()) {
            Candidato nextCandidato = ic.next();
            if(pt.isPresent()){
                if(nextCandidato.getIdPartito().equals(filter.getId())){
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
        List<Partito> p = sessioneDAO.getPartiti(this.sessione.getId());
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
        TableColumn<Candidato, Void> colBtn = new TableColumn<Candidato, Void>("Vote");
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
        TableColumn<Partito, Void> colBtn = new TableColumn<Partito, Void> ("Vote");

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

        yesBtn.setStyle(
                " -fx-background-color:#87c1fc, linear-gradient(#a7d0f8 50%,  #61affd 100%), radial-gradient(center 50% -40%, radius 200%,  #8bc1f7 45%,rgba(230,230,230,0) 50%); -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 3, 0.0 , 0 , 1 );");

        yesBtn.setDisable(true);
        yesBtn.setOpacity(1);

        noBtn.setDisable(true);
        noBtn.setOpacity(1);
    }

    @FXML
    private void voteNo(){
        selectedOption = false;

        noBtn.setStyle(
                " -fx-background-color:#87c1fc, linear-gradient(#a7d0f8 50%,  #61affd 100%), radial-gradient(center 50% -40%, radius 200%,  #8bc1f7 45%,rgba(230,230,230,0) 50%); -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 3, 0.0 , 0 , 1 );");

        yesBtn.setDisable(true);
        yesBtn.setOpacity(1);

        noBtn.setDisable(true);
        noBtn.setOpacity(1);
    }


}
