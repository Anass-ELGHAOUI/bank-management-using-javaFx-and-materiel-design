package sample.Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import sample.Main;
import sample.models.Agence;
import sample.models.Client;
import sample.models.CompteTableModel;
import sample.models.Employe;


public class SiegeController {

    @FXML
    private TableView comptesTable;
    @FXML
    private TableColumn<CompteTableModel, String> c_cin;
    @FXML
    private TableColumn<CompteTableModel, String> solde;
    @FXML
    private TableColumn<CompteTableModel,String> type;
    @FXML
    private TableColumn<CompteTableModel,String> codecmpt;
    @FXML
    private TableColumn<CompteTableModel, String> etat;
    @FXML
    private Label helloLabel;
    @FXML
    private Label agence;
    @FXML
    private Label name;
    @FXML
    private TextField search;

    public SiegeController(){
        super();
    }

    @FXML
    public void onComptePressed(MouseEvent e) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/Comptes_siege.fxml"));
        Scene scene = new Scene(root);
        Main.stage.setScene(scene);
        Main.stage.setResizable(false);
    }

    @FXML
    private void initialize(){

        try{
            helloLabel.setText("Bienvenue " + ((Employe) LoginController.emp).getNom());
            c_cin.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCIN()+ ""));
            codecmpt.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodecmpt()));
            type.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
            solde.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSolde()+""));
            etat.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEtat()));
            comptesTable.setEditable(false);
            comptesTable.setItems(CompteTableModel.getData());
        }catch(Exception e){
            e.printStackTrace();
        }
        comptesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                CompteTableModel model=(CompteTableModel) comptesTable.getSelectionModel().getSelectedItem();
                try {
                    Client c = Client.getClientByCIN(model.getCIN());
                    name.setText(c.getNom());
                    agence.setText(Agence.getAgenceNameByCIN(c.getCIN()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        search.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                comptesTable.setItems(CompteTableModel.getDataPersonnalized(newValue));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void listTransactions(MouseEvent e) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/ListTransactions_siege.fxml"));
        Main.stage.setScene(new Scene(root));
        Main.stage.setResizable(false);
    }

    @FXML
    public void logout(ActionEvent e) throws Exception{
        LoginController.emp=null;
        Parent root = FXMLLoader.load(getClass().getResource("../views/login.fxml"));
        Main.stage.setScene(new Scene(root));
    }

    @FXML
    public void gotoAgence(MouseEvent e) throws Exception{
        Parent root=FXMLLoader.load(getClass().getResource("../views/Agences.fxml"));
        Main.stage.setScene(new Scene(root));
    }


    @FXML
    public void onMouseEntered(MouseEvent e){
        Main.stage.getScene().setCursor(Cursor.HAND);
    }

    @FXML
    public void onMouseSorted(MouseEvent e){
        Main.stage.getScene().setCursor(Cursor.DEFAULT);
    }



}

