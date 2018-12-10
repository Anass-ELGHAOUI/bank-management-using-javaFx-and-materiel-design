package sample.Controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import sample.Main;
import sample.models.Agence;
import sample.models.Employe;

import java.net.URL;
import java.util.ResourceBundle;

public class DeleteAgenceController implements Initializable{

    @FXML
    private JFXTextField code;
    @FXML
    private AnchorPane parent;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    public void gotoAgence(MouseEvent e) throws Exception{
        Parent root= FXMLLoader.load(getClass().getResource("../views/Agences.fxml"));
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        makeStageDrageable();
        code.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                //Ne doit accepter que les entiers
                if (newValue.length() > 0) {
                    if (!Character.isDigit(newValue.charAt(newValue.length() - 1))) {
                        code.setText(newValue.substring(0, newValue.length() - 1));
                    }
                }
            } catch (Exception e) {

            }
        });
    }
    private void makeStageDrageable() {
        parent.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Main.stage.setX(event.getScreenX() - xOffset);
                Main.stage.setY(event.getScreenY() - yOffset);
                Main.stage.setOpacity(0.7f);
            }
        });
        parent.setOnDragDone((e) -> {
            Main.stage.setOpacity(1.0f);
        });
        parent.setOnMouseReleased((e) -> {
            Main.stage.setOpacity(1.0f);
        });

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
    public void deleteAgence(ActionEvent e) throws  Exception{
        int code_int=0;
        try {
             code_int = Integer.parseInt(code.getText());
        }catch(NumberFormatException ee){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("Le numéro d'agence ne peut pas être vide");
            alert.initOwner(Main.stage.getOwner());
            alert.show();
            return;
        }
        if(!Agence.exists(code_int)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("Le numéro d'agence que vous avez entré n'existe pas");
            alert.initOwner(Main.stage.getOwner());
            alert.show();
        }
        else{
            if(Agence.hasClients(code_int)){
                Agence.archivate(code_int);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("INFORMATION");
                alert.setHeaderText(null);
                alert.setContentText("L'agence a été archivé");
                alert.initOwner(Main.stage.getOwner());
                alert.show();
            }
            else{
                Agence.delete(code_int);
                Agence.archivate(code_int);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("INFORMATION");
                alert.setHeaderText(null);
                alert.setContentText("L'agence a été supprimé");
                alert.initOwner(Main.stage.getOwner());
                alert.show();
            }
        }
    }
}
