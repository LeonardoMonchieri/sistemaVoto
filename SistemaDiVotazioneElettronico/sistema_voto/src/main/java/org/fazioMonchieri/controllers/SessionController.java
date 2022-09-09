package org.fazioMonchieri.controllers;

import org.fazioMonchieri.models.Sessione;
import org.fazioMonchieri.models.TipoScrutinio;
import org.fazioMonchieri.models.TipoSessione;
import org.fazioMonchieri.utilities.Controller;
import org.fazioMonchieri.data.ElettoreDAO;
import org.fazioMonchieri.data.ImplCandidatoDAO;
import org.fazioMonchieri.data.ImplSessioneDAO;
import org.fazioMonchieri.data.PartitoDAO;
import org.fazioMonchieri.models.Candidato;
import org.fazioMonchieri.models.Gestore;
import org.fazioMonchieri.models.Partito;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.chart.PieChart;

import java.io.Serial;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import java.util.Iterator;

public class SessionController extends Controller {

    private Sessione sessione;

    private ImplSessioneDAO sessioneDAO;

    private Map<Integer,Integer> winnerMap;

    @FXML
    private Label sessionTitle;

    @FXML
    private Label sessionId;

    @FXML
    private Label sessionAdmin;

    @FXML
    private Label sessionVotingType;

    @FXML
    private Label sessionBallotType;

    @FXML
    private Label quesitoCandidati;

    @FXML
    private Label question;

    @FXML
    private Label sessionOpenDate;

    @FXML
    private Label sessionCloseLabel;

    @FXML
    private Label CloseLabel;
    
    @FXML
    private Label OpenLabel;

    @FXML
    private Label winner;

    @FXML
    private Label Affluenza; 

    @FXML
    private Button DeleteSession;

    @FXML
    private Button OpenClose;

    @FXML
    private PieChart pieChartTurnOut;

    @FXML
    private AnchorPane pane;

    @Override
    public void onNavigateFrom(Controller sender, Object parameter) {
        this.sessione = (Sessione) parameter;
        this.sessioneDAO = ImplSessioneDAO.getInstance();
    }

    @Override
    public void init() {
        sessionTitle.setText(sessione.getNome());
        sessionId.setText("" + sessione.getId());
        sessionAdmin.setText(""+sessione.getIdGestore());


        TipoSessione sessionType = this.sessione.getTipoSessione();
      

        switch (sessione.getTipoScrutinio()) {
            case maggioranza:
                sessionBallotType.setText("maggioranza");
                break;
            case maggioranzaAssoluta:
                sessionBallotType.setText("maggioranza assoluta");
                break;
            case referendumSenzaQuorum:
                sessionBallotType.setText("referendum senza quorum");
                break;
            case referendumConQuorum:
                sessionBallotType.setText("referendum con quorum");
                break;
        }

      
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        if (this.sessione.status() == 0){
            DeleteSession.setOpacity(1);
            DeleteSession.setDisable(false);
            OpenClose.setText("Apri");
        }
        else {
            OpenLabel.setOpacity(1);
            sessionOpenDate.setText(simpleDateFormat.format(this.sessione.getDataApertura()));
            Affluenza.setOpacity(1);
            buildPieChart();

            if (this.sessione.status() == 1) {
                winnerMap= new HashMap<>();
                if(this.sessione.getTipoSessione()==TipoSessione.referendum){
                    if(this.sessione.getTipoScrutinio()==TipoScrutinio.referendumConQuorum){
                        winnerMap=sessioneDAO.scrutinioReferendumConQuorum(this.sessione.getId());
                    }else{
                        winnerMap=sessioneDAO.scrutinioReferendumSenzaQuorum(this.sessione.getId());
                    }
                    referendumResult();
                }else if(this.sessione.getTipoScrutinio()==TipoScrutinio.maggioranza){
                    winnerMap=sessioneDAO.scrutinioMaggioranza(this.sessione.getId());
                }else if(this.sessione.getTipoScrutinio()==TipoScrutinio.maggioranzaAssoluta){
                    winnerMap=sessioneDAO.scrutinioMaggioranzaAssoluta(this.sessione.getId());
                }
                OpenClose.setOpacity(0);
                OpenClose.setDisable(true);
                CloseLabel.setOpacity(1);
                sessionCloseLabel.setText(simpleDateFormat.format(this.sessione.getDataChiusura()));
            } else if (this.sessione.status() == 2)
                OpenClose.setText("Chiudi");

        }

        if (sessionType == TipoSessione.referendum) {
            sessionVotingType.setText("referendum");
            quesitoCandidati.setText("Quesito:");
            question.setText("\"" + sessioneDAO.getQuesitoReferendum(this.sessione.getId()) + "\"");
        } else {
            quesitoCandidati.setText("Opzioni:");
            if (sessionType == TipoSessione.votoCategorico || sessionType == TipoSessione.votoOrdinale) {
                if (sessioneDAO.getPartiti(this.sessione.getId()) == null)
                    candidateTabeleBuilder();
                else
                    partyTableBuilder();
                if (sessionType == TipoSessione.votoCategorico)
                    sessionVotingType.setText("voto ordinale");
                else
                sessionVotingType.setText("voto categorico");
            } else if (sessionType == TipoSessione.votoCategoricoPreferenza) {
                sessionVotingType.setText("voto categorico preferenza");
                candidateTabeleBuilder();
            }
        }
    }

    @FXML
    public void back() {
        navigate("GestoreView", sessioneDAO.getGestore(this.sessione.getId()));
    }

    @FXML
    public void delete(){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Cancellazione Sessione");
        alert.setContentText("Vuoi cancellare la sessione?");

        ButtonType btnYes = new ButtonType("Sí");
        ButtonType btnNo = new ButtonType("no");
        alert.getButtonTypes().setAll(btnYes,btnNo);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnYes){

            Gestore g=sessioneDAO.getGestore(this.sessione.getId());
            sessioneDAO.delete(this.sessione.getId());
            navigate("GestoreView",g) ;
            return;
        } else {
            return;
        }
    }

    @FXML
    public void updateSessionState(){
        if (this.sessione.status() == 0){
            sessioneDAO = ImplSessioneDAO.getInstance();
            sessioneDAO.openSessione(this.sessione.getId(), this.sessione.getIdGestore()); //Apri la sessione
        }
        else if(this.sessione.status() == 2){
            sessioneDAO = ImplSessioneDAO.getInstance();
            sessioneDAO.closeSessione(this.sessione.getId(), this.sessione.getIdGestore()); //Chiudi sessione
        }
        navigate("GestoreView", sessioneDAO.getGestore(this.sessione.getId()));
    }

    //Table maker
    private void candidateTabeleBuilder() {
        ImplCandidatoDAO candidatoDAO=ImplCandidatoDAO.getInstance();

        List<Candidato> c = sessioneDAO.getCandidati(this.sessione.getId());
        

        TableView<Candidato> candidateTable = new TableView<Candidato>();
        TableColumn<Candidato, String> partito = new TableColumn<>("Partito/Gruppo");

        partito.setCellValueFactory(param -> new SimpleObjectProperty(candidatoDAO.getPartito(param.getValue().getId()).getNome()));

        TableColumn<Candidato, String> candidato = new TableColumn<>("Candidato");

        candidato.setCellValueFactory(
                param -> new SimpleObjectProperty<>(candidatoDAO.getNomeCompleto(param.getValue().getId())));

        candidateTable.getColumns().add(partito);
        candidateTable.getColumns().add(candidato);

        Integer pd=0;
        if (this.sessione.status() == 1 && winnerMap!=null) {  
            TableColumn<Candidato, Integer> voti = new TableColumn<>("Punteggio");
            voti.setCellValueFactory(param -> new SimpleObjectProperty<>( winnerMap.get(param.getValue().getId() )) );
            candidateTable.getColumns().add(voti);
            if(this.sessione.getTipoSessione()==TipoSessione.votoCategoricoPreferenza){
                String np=null;
                Iterator<Integer> i=winnerMap.keySet().iterator();
                while(np==null){
                    Integer cId= i.next();
                    pd=candidatoDAO.getPartito(cId).getId();
                    np = candidatoDAO.getPartito(cId).getNome();
                }
                question.setText(np);
                question.setLayoutY(415);
                question.setLayoutX(233);
                
                winner.setOpacity(1);
                winner.setLayoutY(420);
                winner.setText("Il partito vincente é: ");
            }
        }
        if(this.sessione.status() == 1 && winnerMap==null){
            if(winnerMap==null){
                winner.setOpacity(1);
                winner.setText("Nessun parito/candidato ha raggiunto \nla maggioranza assoluta");
                winner.setLayoutX(100);
                winner.setLayoutY(430);
            }
        }
        
        Iterator<Candidato> ic = c.iterator();
        while (ic.hasNext()) {
            Candidato cndt=ic.next();
            if(this.sessione.getTipoSessione()==TipoSessione.votoCategoricoPreferenza && this.sessione.status() == 1){
                if(cndt.getIdPartito()!=pd) continue;
            }
            candidateTable.getItems().add(cndt);
        }

        candidateTable.setColumnResizePolicy(candidateTable.CONSTRAINED_RESIZE_POLICY);
        candidateTable.setLayoutX(100);
        candidateTable.setLayoutY(190);
        candidateTable.setPrefHeight(240);
        candidateTable.setPrefWidth(400);
        pane.getChildren().add(candidateTable);
    }

    private void partyTableBuilder() {
        List<Partito> p = sessioneDAO.getPartiti(this.sessione.getId());
        Iterator<Partito> ip = p.iterator();

        TableView<Partito> partyTable = new TableView<Partito>();
        TableColumn<Partito, String> partito = new TableColumn<>("Partito/Gruppo");

        partito.setCellValueFactory(new PropertyValueFactory<Partito, String>("nome"));

        partyTable.getColumns().add(partito);

        if (this.sessione.status() == 1 && this.sessione.getTipoSessione()!=TipoSessione.votoCategoricoPreferenza && winnerMap!=null) {
            TableColumn<Partito, Integer> voti = new TableColumn<>("Punteggio");
            voti.setCellValueFactory(param -> new SimpleObjectProperty<>( winnerMap.get(param.getValue().getId() )) );
            partyTable.getColumns().add(voti);
        }

        if(this.sessione.status() == 1 && winnerMap==null){
            winner.setOpacity(1);
            winner.setText("Nessun parito/candidato ha raggiunto \nla maggioranza assoluta");
            winner.setLayoutX(100);
            winner.setLayoutY(430);
        }
       

        while(ip.hasNext()) {
            partyTable.getItems().add(ip.next());
        }

       

        partyTable.setColumnResizePolicy(partyTable.CONSTRAINED_RESIZE_POLICY);
        partyTable.setLayoutX(100);
        partyTable.setLayoutY(190);
        partyTable.setPrefHeight(240);
        partyTable.setPrefWidth(400);
        pane.getChildren().add(partyTable);
    }

    private void referendumResult(){
        if(winnerMap==null){
            System.out.println("NO QUORUM 2");
            winner.setOpacity(1);
            winner.setText("Non é stato raggiunto il quorum");
            winner.setLayoutX(100);
            winner.setLayoutY(230);
            return;
        }
        System.out.println("OK QUORUM 2");
        TableView refTable = new TableView<>();

        TableColumn<Map, String> yesCol = new TableColumn<>("Si");
            yesCol.setCellValueFactory(new MapValueFactory<>(1));

        TableColumn<Map, String> noCol = new TableColumn<>("No");
            noCol.setCellValueFactory(new MapValueFactory<>(0));
        
            refTable.getColumns().add(yesCol);
            refTable.getColumns().add(noCol);
        
        ObservableList<Map<Integer, Integer>> items =
            FXCollections.<Map<Integer, Integer>>observableArrayList();
        
        items.add(winnerMap);

        refTable.getItems().addAll(items);

        refTable.setColumnResizePolicy(refTable.CONSTRAINED_RESIZE_POLICY);
        refTable.setLayoutX(65);
        refTable.setLayoutY(230);
        refTable.setPrefHeight(70);
        pane.getChildren().add(refTable);

        winner.setDisable(false);
        winner.setText("Esiti: ");

    }

    private void buildPieChart() {
        int votanti = this.sessioneDAO.getVotanti(this.sessione.getId());
        int nonVotanti = this.sessioneDAO.getElettori() - votanti ;
        ObservableList<PieChart.Data> turnOutData = FXCollections.observableArrayList(
                new PieChart.Data("Non votanti: " + nonVotanti, nonVotanti),
                new PieChart.Data("Votanti: " + votanti, votanti));

        pieChartTurnOut.setData(turnOutData);
        pieChartTurnOut.setDisable(false);  
    }

    
}
