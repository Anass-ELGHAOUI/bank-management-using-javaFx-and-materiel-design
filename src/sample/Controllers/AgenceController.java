package sample.Controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import sample.Main;
import sample.models.Agence;

public class AgenceController {

    @FXML
    private JFXTextField libelle;
    @FXML
    private JFXTextField adresse;

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

    @FXML
    public void listeAgences(MouseEvent e) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/ListAgences.fxml"));
        Main.stage.setScene(new Scene(root));
    }

    @FXML
    public void deleteAgence(MouseEvent e) throws Exception{
        Parent root=FXMLLoader.load(getClass().getResource("../views/delete_agence.fxml"));
        Main.stage.setScene(new Scene(root));
    }

    @FXML
    public void newAgence(ActionEvent e){
        String libelleTxt=libelle.getText();
        String adresseTxt=adresse.getText();
        if(libelleTxt.length()==0 || adresseTxt.length()==0){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("Vous devez remplir tous les champs");
            alert.initOwner(Main.stage.getOwner());
            alert.show();
        }
        else{


                    try {
                        Agence.addAgence(libelleTxt , adresseTxt);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information");
                        alert.setHeaderText(null);
                        alert.setContentText("Agence ajoutée");
                        alert.initOwner(Main.stage.getOwner());
                        alert.show();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }



        }
    }

}
