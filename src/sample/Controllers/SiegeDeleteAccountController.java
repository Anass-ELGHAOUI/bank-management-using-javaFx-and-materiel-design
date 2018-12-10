package sample.Controllers;


import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import sample.Main;
import sample.models.Compte;
import sample.models.CompteCourant;
import sample.models.CompteEpargne;

import java.sql.SQLException;

public class SiegeDeleteAccountController {

    @FXML
    private Label errorLabel;
    @FXML
    private Label account_num_lab;
    @FXML
    private JFXTextField account_number_TF;
    private boolean account_flag;

    @FXML
    public void onMouseSorted(MouseEvent e){
        Main.stage.getScene().setCursor(Cursor.DEFAULT);
    }

    @FXML
    public void onMouseEntered(MouseEvent e) throws Exception{
        Main.stage.getScene().setCursor(Cursor.HAND);

    }
    @FXML
    public void onMouseClickedDashboard(MouseEvent e) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/Siege_home.fxml"));
        Scene scene = new Scene(root);
        Main.stage.setScene(scene);
    }

    @FXML
    public void newListTransaction(MouseEvent e) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/ListTransactions_siege.fxml"));
        Main.stage.setScene(new Scene(root));
        Main.stage.setResizable(false);
    }

    @FXML
    public void deleteAccount(ActionEvent e){
        if(account_flag){
            account_flag=false;
            account_num_lab.setTextFill(Color.valueOf("BLACK"));
            account_number_TF.setFocusColor(Color.valueOf("BLACK"));
            Compte tmp;
            try{
            if(account_number_TF.getText().charAt(0) == 'C')
                tmp = CompteCourant.getCompteCourantByCode(account_number_TF.getText());
            else
                tmp= CompteEpargne.getCompteEpargneByCode(account_number_TF.getText());
                    if(tmp.hasTransactions()){
                        // Le compte a des transactions. Il faut juste l'archiver
                        if(tmp.archiver()){
                            account_number_TF.setText("");
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Message d'information");
                            alert.setHeaderText(null);
                            alert.setContentText("Le compte N° " + tmp.getCode() + " a été archivé");
                            alert.initOwner(Main.stage.getOwner());
                            alert.show();
                        }
                        else{
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Message d'information");
                            alert.setHeaderText(null);
                            alert.setContentText("Erreur lors de l'archivation du compte N° " + tmp.getCode());
                            alert.initOwner(Main.stage.getOwner());
                            alert.show();
                        }
                    }
                    else{
                        //Supprimer le compte
                        if(tmp.deleteAccount()){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Message d'information");
                            alert.setHeaderText(null);
                            alert.setContentText("Le compte N° " + tmp.getCode() + " a été supprimé");
                            alert.initOwner(Main.stage.getOwner());
                            alert.show();
                        }
                        else{
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Message d'information");
                            alert.setHeaderText(null);
                            alert.setContentText("Erreur lors de la suppression du compte N° " + tmp.getCode());
                            alert.initOwner(Main.stage.getOwner());
                            alert.show();
                        }
                    }

                } catch (SQLException | ClassNotFoundException e1) {
                    e1.printStackTrace();

            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Le compte à supprimer n'existe pas");
            alert.initOwner(Main.stage.getOwner());
            alert.show();
        }
    }

    @FXML
    public void logout(ActionEvent e) throws Exception{
        LoginController.emp=null;
        Parent root = FXMLLoader.load(getClass().getResource("../views/login.fxml"));
        Main.stage.setScene(new Scene(root));
    }


    @FXML
    public void addAccount(MouseEvent e) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/Comptes_siege.fxml"));
        Main.stage.setScene(new Scene(root));
    }

    @FXML
    public void newTransaction(MouseEvent e) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/Transactions_siege.fxml"));
        Main.stage.setScene(new Scene(root));
    }

    @FXML
    private void initialize() {
        errorLabel.setText("");
        account_flag=false;
        account_number_TF.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if(!Compte.exists(newValue)){
                    account_flag=false;
                    account_num_lab.setTextFill(Color.valueOf("RED"));
                    account_number_TF.setFocusColor(Color.valueOf("RED"));
                }
                else{
                    account_flag=true;
                    account_num_lab.setTextFill(Color.valueOf("GREEN"));
                    account_number_TF.setFocusColor(Color.valueOf("GREEN"));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void gotoAgence(MouseEvent e) throws Exception{
        Parent root=FXMLLoader.load(getClass().getResource("../views/Agences.fxml"));
        Main.stage.setScene(new Scene(root));
    }
}
