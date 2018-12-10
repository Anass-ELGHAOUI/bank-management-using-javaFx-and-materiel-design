package sample.Controllers;

import javafx.beans.property.SimpleObjectProperty;
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

import javax.xml.soap.Text;
import java.time.LocalDate;

public class ListAgencesController {

    @FXML
    private TextField searchbar;
    @FXML
    private TableView<Agence> table;
    @FXML
    private TableColumn<Agence, String> codeagence;
    @FXML
    private TableColumn<Agence, String> nomagence;
    @FXML
    private TableColumn<Agence, String> adresseagance;
    @FXML
    private TableColumn<Agence,LocalDate> datecreation;
    @FXML
    private TableColumn<Agence , String> etat;
    @FXML
    private TextField search;

    @FXML
    public void initialize(){
        try{
            nomagence.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
            adresseagance.setCellValueFactory(cellData -> cellData.getValue().adresseProperty());
            codeagence.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().codeagenceProperty().get() + ""));
            // lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

            datecreation.setCellValueFactory(cellData -> new SimpleObjectProperty<LocalDate>(cellData.getValue().getDateCreation()));
            etat.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isArchived()? "Archivé":"Actif"));
            table.setEditable(true);
            table.setItems(Agence.getListAgences());
            search.textProperty().addListener((observable, oldValue, newValue) -> {
                if(search.getText().equals("")) {
                    try {
                        table.setItems(Agence.getListAgences());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        table.setItems(Agence.getListAgencesByParameter(search.getText()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }catch(Exception e){

        }
    }
    @FXML
    public void gotoAgence(MouseEvent e) throws Exception{
        Parent root=FXMLLoader.load(getClass().getResource("../views/Agences.fxml"));
        Main.stage.setScene(new Scene(root));
    }
    @FXML
    public void deleteAgence(MouseEvent e) throws Exception{
        Parent root=FXMLLoader.load(getClass().getResource("../views/delete_agence.fxml"));
        Main.stage.setScene(new Scene(root));
    }
    @FXML
    public void onMouseClickedDashboard(MouseEvent e) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/Siege_home.fxml"));
        Scene scene = new Scene(root);
        Main.stage.setScene(scene);
    }
    @FXML
    public void onMouseEntered(MouseEvent e){
        Main.stage.getScene().setCursor(Cursor.HAND);
    }

    @FXML
    public void onMouseSorted(MouseEvent e){
        Main.stage.getScene().setCursor(Cursor.DEFAULT);
    }

    @FXML
    public void logout(ActionEvent e) throws Exception{
        LoginController.emp=null;
        Parent root = FXMLLoader.load(getClass().getResource("../views/login.fxml"));
        Main.stage.setScene(new Scene(root));
    }
    @FXML
    public void newListTransaction(MouseEvent e) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/ListTransactions_siege.fxml"));
        Main.stage.setScene(new Scene(root));
        Main.stage.setResizable(false);
    }
    @FXML
    public void newAccount(MouseEvent e) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/Comptes_siege.fxml"));
        Main.stage.setScene(new Scene(root));
    }

}
