package sample.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import sample.Main;
import sample.models.Client;
import sample.models.Employe;

import java.io.IOException;

public class ComptesController {

    @FXML
    private Label client_cin_label;
    @FXML
    private Label type_account_label;
    @FXML
    private Label solde_label;
    @FXML
    private JFXTextField CIN_TF;
    @FXML
    private JFXComboBox combo_TF;
    @FXML
    private JFXTextField solde_TF;
    @FXML
    private JFXButton btn_next;
    @FXML
    private Label decouvert_label;
    @FXML
    private JFXTextField decouvert_TF;
    @FXML
    private Label taux_label;
    @FXML
    private JFXTextField taux_TF;
    @FXML
    private JFXButton logout;
    //Drapeau pour savoir si le cin est valide
    private boolean cin_flag;
    //Drapeau pour savoir si le combo est valide
    private boolean combo_flag;
    //CIN du client à ajouter
    private int CIN;
    //type du compte à ajouter
    private String typeCompte;
    // valeur du taux d'interet ou découvert autorisé
    private double valueof;
    //solde
    private double solde;
    //flag pour savoir si le taux est valide
    private boolean taux_flag;
    //flag pour savoir si le compte a été créé
    private boolean created_flag;
    @FXML
    private TextField search;

    @FXML
    public void setEnvirenmentFornewAccountEvent(MouseEvent e) {
        client_cin_label.setVisible(true);
        type_account_label.setVisible(true);
        solde_label.setVisible(true);
        CIN_TF.setVisible(true);
        combo_TF.setVisible(true);
        solde_TF.setVisible(true);
        btn_next.setVisible(true);
    }

    @FXML
    public void onMouseClickedDashboard(MouseEvent e) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/UIHome.fxml"));
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
    public void next(ActionEvent e) throws Exception{
        //test de validation
        created_flag=false;
        if(combo_flag && cin_flag){
            CIN=Integer.parseInt(CIN_TF.getText());
            typeCompte = combo_TF.getValue() + "";
            System.out.println("Validated");
            solde= solde_TF.getText().equals("") ? 0 : Double.parseDouble(solde_TF.getText());
            if(typeCompte.contains("épargne")) {
                if(taux_flag){
                if (Client.getClientCEAccountsCount(CIN) == 0) {
                    if (taux_TF.getText().equals(""))
                        valueof = 0d;
                    else
                        valueof = Double.parseDouble(taux_TF.getText());
                    Client.newCompteEpargne(CIN, solde, valueof, ((Employe) LoginController.emp).getAgence().getCodeagence());
                    created_flag = true;
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Ce client dispose déja d'un compte épargne");
                    alert.initOwner(Main.stage.getOwner());
                    alert.show();
                }
            }else{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Attention");
                    alert.setHeaderText(null);
                    alert.setContentText("Le taux doit être compros entre 0 et 100%. Seul les caractères numériques seront acceptés");
                    alert.initOwner(Main.stage.getOwner());
                    alert.show();
                }
            }
            else{
                if(Client.getClientCCAccountsCount(CIN)==0) {
                    if (decouvert_TF.getText().equals(""))
                        valueof = 0d;
                    else
                        valueof = Double.parseDouble(decouvert_TF.getText());
                    Client.newCompteCourant(CIN, solde, valueof, ((Employe) LoginController.emp).getAgence().getCodeagence());
                    created_flag=true;
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Ce client dispose déja d'un compte courant");
                    alert.initOwner(Main.stage.getOwner());
                    alert.show();
                }
            }

        // initialisation du formulaire
            CIN_TF.setText("");
            combo_TF.setValue(0);
            solde_TF.setText("");
            decouvert_TF.setText("");
            taux_TF.setText("");
            decouvert_label.setVisible(false);
            decouvert_TF.setVisible(false);
            taux_label.setVisible(false);
            taux_TF.setVisible(false);
            combo_flag=false;
            cin_flag=false;
            taux_flag=false;
            client_cin_label.setTextFill(Paint.valueOf("BLACK"));
            CIN_TF.setUnFocusColor(Paint.valueOf("BLACK"));
            CIN_TF.setFocusColor(Paint.valueOf("BLACK"));
            taux_label.setTextFill(Paint.valueOf("BLACK"));
            taux_TF.setUnFocusColor(Paint.valueOf("BLACK"));
            taux_TF.setFocusColor(Paint.valueOf("BLACK"));
            type_account_label.setTextFill(Paint.valueOf("BLACK"));
            combo_TF.setUnFocusColor(Paint.valueOf("BLACK"));
            combo_TF.setFocusColor(Paint.valueOf("BLACK"));
            //si le compte est créé
            if(created_flag) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Message d'information");
                alert.setHeaderText(null);
                alert.setContentText("Un compte de type \"" + typeCompte + " vient d'être créé pour le client porteur de la CIN " + CIN);
                alert.initOwner(Main.stage.getOwner());
                alert.show();
            }

        }
        else{
            if(!cin_flag){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Le CIN entré ne correspond au aucun client de l'agence");
                alert.initOwner(Main.stage.getOwner());
                alert.show();
            }
            if(!combo_flag){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attention");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez spécifier le type de compte");
                alert.initOwner(Main.stage.getOwner());
                alert.show();
            }

        }

    }

    @FXML
    public void newTransaction(MouseEvent e) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/TransactionsEmp.fxml"));
        Main.stage.setScene(new Scene(root));
    }

    @FXML
    public void newListTransaction(MouseEvent e) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/ListTransactions.fxml"));
        Main.stage.setScene(new Scene(root));
        Main.stage.setResizable(false);
    }

    @FXML
    public void deleteAccount(MouseEvent e) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/DeleteAccount.fxml"));
        Main.stage.setScene(new Scene(root));
    }

    @FXML
    private void initialize(){
    //    client_cin_label.setVisible(false);
    //    type_account_label.setVisible(false);
    //    solde_label.setVisible(false);
    //    CIN_TF.setVisible(false);
    //    combo_TF.setVisible(false);
    //    solde_TF.setVisible(false);
    //    btn_next.setVisible(false);
        decouvert_label.setVisible(false);
        decouvert_TF.setVisible(false);
        taux_label.setVisible(false);
        taux_TF.setVisible(false);
        combo_flag=false;
        cin_flag=false;
        taux_flag=false;
        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("Compte d'épargne");
        list.add("Compte Courant");
        combo_TF.setItems(list);
        CIN_TF.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                //Ne doit accepter que les entiers
                if(newValue.length()>0) {
                    if(!Character.isDigit(newValue.charAt(newValue.length()-1))) {
                        CIN_TF.setText(newValue.substring(0, newValue.length()-1));
                    }
                    if(newValue.length()>0){
                        int idclient = Client.getIdClientByCIN(Integer.parseInt(CIN_TF.getText()));
                        if (idclient == -1) {
                            // pas de client avec ce CIN
                            cin_flag=false;
                            System.out.println("No Client");
                            client_cin_label.setTextFill(Paint.valueOf("RED"));
                            CIN_TF.setUnFocusColor(Paint.valueOf("RED"));
                            CIN_TF.setFocusColor(Paint.valueOf("RED"));
                        }
                        else{
                            cin_flag=true;
                            client_cin_label.setTextFill(Paint.valueOf("GREEN"));
                            CIN_TF.setUnFocusColor(Paint.valueOf("GREEN"));
                            CIN_TF.setFocusColor(Paint.valueOf("GREEN"));
                        }
                    }
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        });
        combo_TF.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(combo_TF.getValue().equals("Compte d'épargne") || combo_TF.getValue().equals("Compte Courant")) {
                combo_flag=true;
                type_account_label.setTextFill(Paint.valueOf("GREEN"));
                combo_TF.setFocusColor(Paint.valueOf("GREEN"));
                combo_TF.setUnFocusColor(Paint.valueOf("GREEN"));
                if(combo_TF.getValue().equals("Compte d'épargne")){
                    decouvert_TF.setVisible(false);
                    decouvert_label.setVisible(false);
                    taux_TF.setVisible(true);
                    taux_label.setVisible(true);
                }
                else{
                    taux_TF.setVisible(false);
                    taux_label.setVisible(false);
                    decouvert_TF.setVisible(true);
                    decouvert_label.setVisible(true);
                }
            }
            else{
                combo_flag = false;
                type_account_label.setTextFill(Paint.valueOf("RED"));
                combo_TF.setFocusColor(Paint.valueOf("RED"));
                combo_TF.setUnFocusColor(Paint.valueOf("RED"));
            }
        });



        solde_TF.textProperty().addListener((observable, oldValue, newValue) -> {
            //Ne doit accepter que les doubles
            if(newValue.length()>0) {
                if(!Character.isDigit(newValue.charAt(newValue.length()-1)) && newValue.charAt(newValue.length()-1)!='.') {
                    solde_TF.setText(newValue.substring(0, newValue.length()-1));
                }
            }

        });

        taux_TF.textProperty().addListener((observable, oldValue, newValue) -> {
            //Ne doit accepter que les doubles
            if(newValue.length()>0) {
                if(!Character.isDigit(newValue.charAt(newValue.length()-1)) && newValue.charAt(newValue.length()-1)!='.') {
                    taux_TF.setText(newValue.substring(0, newValue.length()-1));
                }
                if(taux_TF.getText().length()>0){
                    if(Integer.parseInt(taux_TF.getText())<0 | Integer.parseInt(taux_TF.getText())>100){
                        taux_flag=false;
                    }
                    else{
                        taux_flag=true;
                    }
                }
                else{
                    taux_flag=false;
                }
            }

        });

        decouvert_TF.textProperty().addListener((observable, oldValue, newValue) -> {
            //Ne doit accepter que les doubles
            if(newValue.length()>0) {
                if(!Character.isDigit(newValue.charAt(newValue.length()-1)) && newValue.charAt(newValue.length()-1)!='.') {
                    decouvert_TF.setText(newValue.substring(0, newValue.length()-1));
                }
            }

        });



    }
    @FXML
    public void onActionUsers(MouseEvent event) throws IOException {
        Parent root=FXMLLoader.load(getClass().getResource("../views/UI.fxml"));
        Main.stage.setScene(new Scene(root));

    }


}
