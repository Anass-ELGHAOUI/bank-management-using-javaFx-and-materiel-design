package sample.Controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import sample.Main;
import sample.models.ListTransactionTableModel;
import sample.models.PdfListgenerate;

import java.time.LocalDate;


public class SiegeListTransactionController {

    @FXML
    private TableView table_tran;
    @FXML
    private TableColumn<ListTransactionTableModel, String> numcmpt;
    @FXML
    private TableColumn<ListTransactionTableModel, String> typetran;
    @FXML
    private TableColumn<ListTransactionTableModel, LocalDate> date;
    @FXML
    private TableColumn<ListTransactionTableModel, String> Montant;
    @FXML
    private TextField search;
    @FXML
    private TableColumn<ListTransactionTableModel, String> Cin;

    @FXML
    private void initialize() throws Exception{
        Montant.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMontant()+ ""));
        numcmpt.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodecompte()));
        typetran.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        date.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getDateTran()));
        Cin.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCin()+""));
        table_tran.setEditable(false);
        table_tran.setItems(ListTransactionTableModel.getData());
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                table_tran.setItems(ListTransactionTableModel.getData(newValue));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void onMouseClickedDashboard(MouseEvent e) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/Siege_home.fxml"));
        Scene scene = new Scene(root);
        Main.stage.setScene(scene);
    }

    @FXML
    public void newTransaction(MouseEvent e) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/Transactions_siege.fxml"));
        Main.stage.setScene(new Scene(root));
    }

    @FXML
    public void onComptePressed(MouseEvent e) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/Comptes_siege.fxml"));
        Scene scene = new Scene(root);
        Main.stage.setScene(scene);
        Main.stage.setResizable(false);
    }

    @FXML
    public void export(ActionEvent e){
        ListTransactionTableModel model=(ListTransactionTableModel) table_tran.getSelectionModel().getSelectedItem();
        if(model==null)
            PdfListgenerate.generate();
        else{
            PdfListgenerate.generateReleve(model.getCodecompte());
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText("Le fichier PDF est enregistrer dans : C:\\Users\\Anass\\Documents\\releve_banquaire.pdf");
        alert.initOwner(Main.stage.getOwner());
        alert.show();
    }

    @FXML
    public void logout(ActionEvent e) throws Exception{
        LoginController.emp=null;
        Parent root = FXMLLoader.load(getClass().getResource("../views/login.fxml"));
        Main.stage.setScene(new Scene(root,1024,600));
    }

    @FXML
    public void gotoAgence(MouseEvent e) throws Exception{
        Parent root=FXMLLoader.load(getClass().getResource("../views/Agences.fxml"));
        Main.stage.setScene(new Scene(root));
    }
}
