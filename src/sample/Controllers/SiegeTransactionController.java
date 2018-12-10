package sample.Controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.paint.Paint;
import sample.Main;
import sample.models.Compte;
import sample.models.CompteCourant;
import sample.models.CompteEpargne;

import java.sql.SQLException;

public class SiegeTransactionController {

    @FXML
    private Label errorLabel;
    @FXML
    private JFXTextField code_debit;
    @FXML
    private JFXTextField code_credit;
    @FXML
    private JFXTextField montant;
    @FXML
    private Label credit_lab;
    @FXML
    private Label debit_lab;
    @FXML
    private JFXTextField montant_retrait_TF;
    @FXML
    private JFXTextField montant_depot_TF;
    @FXML
    private Label nc_retrait_lab;
    @FXML
    private Label nc_depot_lab;
    @FXML
    private JFXTextField nc_depot_TF;
    @FXML
    private JFXTextField nc_retrait_TF;
    private boolean montant_depot_flag;
    private boolean montant_retrait_flag;
    private boolean montant_flag;
    private boolean credit_flag;
    private boolean debit_flag;
    private boolean nc_depot_flag;
    private boolean nc_retrait_flag;
    private Compte comptedebit;
    private Compte comptecredit;

    @FXML
    public void onMouseClickedDashboard(MouseEvent e) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/Siege_home.fxml"));
        Scene scene = new Scene(root);
        Main.stage.setScene(scene);
    }

    @FXML
    public void onMouseEntered(MouseEvent e) throws Exception{
        Main.stage.getScene().setCursor(Cursor.HAND);

    }

    @FXML
    public void deleteAccount(MouseEvent e) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/DeleteAccount_siege.fxml"));
        Main.stage.setScene(new Scene(root));
    }

    @FXML
    public void newListTransaction() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/ListTransactions_siege.fxml"));
        Main.stage.setScene(new Scene(root));
        Main.stage.setResizable(false);
    }

    @FXML
    public void addAccount(MouseEvent e) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/Comptes_siege.fxml"));
        Main.stage.setScene(new Scene(root));
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
    private void initialize() {
        credit_flag=false;
        debit_flag=false;
        montant_flag=false;
        montant_depot_flag=false;
        montant_retrait_flag=false;
        nc_depot_flag=false;
        nc_retrait_flag=false;
        code_credit.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.length()>0) {
                if(!Character.isDigit(newValue.charAt(newValue.length()-1)) && Character.isLetter(0)) {
                    //code_credit.setText(newValue.substring(0, newValue.length()-1));
                }
            }
            try {
                if(!Compte.exists(newValue)){
                    credit_lab.setTextFill(Paint.valueOf("RED"));
                    code_credit.setUnFocusColor(Paint.valueOf("RED"));
                    code_credit.setFocusColor(Paint.valueOf("RED"));
                    credit_flag=false;
                }
                else{
                    credit_lab.setTextFill(Paint.valueOf("GREEN"));
                    code_credit.setUnFocusColor(Paint.valueOf("GREEN"));
                    code_credit.setFocusColor(Paint.valueOf("GREEN"));
                    credit_flag=true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        code_debit.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.length()>0) {
                if(!Character.isDigit(newValue.charAt(newValue.length()-1)) && Character.isLetter(newValue.charAt(0))) {
                    //code_debit.setText(newValue.substring(0, newValue.length()-1));
                }
                try {
                    if(!Compte.exists(newValue)){
                        debit_lab.setTextFill(Paint.valueOf("RED"));
                        code_debit.setUnFocusColor(Paint.valueOf("RED"));
                        code_debit.setFocusColor(Paint.valueOf("RED"));
                        debit_flag=false;
                    }
                    else{
                        debit_lab.setTextFill(Paint.valueOf("GREEN"));
                        code_debit.setUnFocusColor(Paint.valueOf("GREEN"));
                        code_debit.setFocusColor(Paint.valueOf("GREEN"));
                        debit_flag=true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        montant.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.length()>0) {
                    if (!Character.isDigit(newValue.charAt(newValue.length() - 1)) && newValue.charAt(newValue.length() - 1) != '.') {
                        montant.setText(newValue.substring(0, newValue.length() - 1));
                    }
                    if(montant.getText().length()>0 && Integer.parseInt(montant.getText())!=0)
                        montant_flag=true;
                }
                else
                    montant_flag=false;
            }
        });
        nc_depot_TF.textProperty().addListener(((observable, oldValue, newValue) -> {
            try{
                if(!Compte.exists(newValue)){
                    nc_depot_lab.setTextFill(Paint.valueOf("RED"));
                    nc_depot_TF.setUnFocusColor(Paint.valueOf("RED"));
                    nc_depot_TF.setFocusColor(Paint.valueOf("RED"));
                    nc_depot_flag=false;
                }
                else{
                    nc_depot_lab.setTextFill(Paint.valueOf("GREEN"));
                    nc_depot_TF.setUnFocusColor(Paint.valueOf("GREEN"));
                    nc_depot_TF.setFocusColor(Paint.valueOf("GREEN"));
                    nc_depot_flag=true;
                }
            }catch(SQLException | ClassNotFoundException e){
                e.printStackTrace();
            }
        }));
        nc_retrait_TF.textProperty().addListener(((observable, oldValue, newValue) -> {
            try{
                if(!Compte.exists(newValue)){
                    nc_retrait_lab.setTextFill(Paint.valueOf("RED"));
                    nc_retrait_TF.setUnFocusColor(Paint.valueOf("RED"));
                    nc_retrait_TF.setFocusColor(Paint.valueOf("RED"));
                    nc_retrait_flag=false;
                }
                else{
                    nc_retrait_lab.setTextFill(Paint.valueOf("GREEN"));
                    nc_retrait_TF.setUnFocusColor(Paint.valueOf("GREEN"));
                    nc_retrait_TF.setFocusColor(Paint.valueOf("GREEN"));
                    nc_retrait_flag=true;
                }
            }catch(SQLException | ClassNotFoundException e){
                e.printStackTrace();
            }
        }));
        montant_depot_TF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.length()>0) {
                    if (!Character.isDigit(newValue.charAt(newValue.length() - 1)) && newValue.charAt(newValue.length() - 1) != '.') {
                        montant_depot_TF.setText(newValue.substring(0, newValue.length() - 1));
                    }
                    if (montant_depot_TF.getText().length() > 0) {
                        try {
                            if (Integer.parseInt(montant_depot_TF.getText()) != 0) {
                                montant_depot_flag = true;
                            }
                        } catch (NumberFormatException e) {
                            montant_depot_flag = false;
                        }
                    }
                }
                    else{
                        montant_depot_flag=false;
                    }
            }
        });

        montant_retrait_TF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.length()>0) {
                    if (!Character.isDigit(newValue.charAt(newValue.length() - 1)) && newValue.charAt(newValue.length() - 1) != '.') {
                        montant_retrait_TF.setText(newValue.substring(0, newValue.length() - 1));
                    }
                    if(montant_retrait_TF.getText().length()>0) {
                        try {
                            if (Integer.parseInt(montant_retrait_TF.getText()) != 0) {
                                montant_retrait_flag = true;
                            }
                        }catch(NumberFormatException e){
                            montant_retrait_flag=false;
                            e.printStackTrace();
                        }
                    }
                    else{
                        montant_retrait_flag=false;
                    }
                }
                else
                    montant_retrait_flag=false;
            }
        });
    }

    @FXML
    public void validateTransaction(ActionEvent e) throws Exception{
        errorLabel.setText("");
        if(debit_flag && credit_flag && montant_flag){
            debit_lab.setTextFill(Paint.valueOf("BLACK"));
            code_debit.setUnFocusColor(Paint.valueOf("BLACK"));
            code_debit.setFocusColor(Paint.valueOf("BLACK"));
            credit_lab.setTextFill(Paint.valueOf("BLACK"));
            code_credit.setUnFocusColor(Paint.valueOf("BLACK"));
            code_credit.setFocusColor(Paint.valueOf("BLACK"));
            debit_flag=false;
            credit_flag=false;
            montant_flag=false;
            //Début transaction
            String CodeCompteDebit = code_debit.getText();
            if(code_debit.getText().charAt(0) == 'C' || code_debit.getText().charAt(0)=='c'){
                //Compte de débit est un compte courant
                comptedebit= CompteCourant.getCompteCourantByCode(code_debit.getText());
            }
            else{
                //Compte de débit est un compte d'epargne
                comptedebit= CompteEpargne.getCompteEpargneByCode(code_debit.getText());
            }
            if(code_credit.getText().charAt(0)=='C' || code_credit.getText().charAt(0)=='c'){
                //Compte de Crédit est un compte courant
                comptecredit= CompteCourant.getCompteCourantByCode(code_credit.getText());
            }
            else{
                //Compte de Crédit est un compte courant
                comptecredit= CompteEpargne.getCompteEpargneByCode(code_credit.getText());
            }

            //TODO Effectuer la transaction
            if(comptecredit.retirer(Double.parseDouble(montant.getText()))){
                comptedebit.deposer(Double.parseDouble(montant.getText()));
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Transaction effectuée");
                alert.setHeaderText(null);
                alert.setContentText("Transaction effectuée. Le compte " + comptedebit.getCode() + " a été débité de " + montant.getText() + " .Le compte " + comptecredit.getCode() +
                        " a été crédité de " + montant.getText());
                alert.initOwner(Main.stage.getOwner());
                alert.show();
                code_credit.setText("");
                code_debit.setText("");
                montant.setText("");
                code_credit.setFocusColor(Color.valueOf("BLACK"));
                code_debit.setFocusColor(Color.valueOf("BLACK"));
                montant.setFocusColor(Color.valueOf("BLACK"));
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Crédit insuffisant pour le compte à créditer");
                alert.initOwner(Main.stage.getOwner());
                alert.show();
            }
        }
        else{
            String error="";
            if(!debit_flag)
                error="Le compte à débiter n'existe pas\n";
            if(!credit_flag)
                error=error+"Le Compte à créditer n'existe pas\n";
            if(!montant_flag)
                error=error+"Le montant ne doit pas être vide";
            errorLabel.setText(error);
        }


    }

    @FXML
    public void depot(){
        if(montant_depot_flag && nc_depot_flag){
            String numCompte = nc_depot_TF.getText();
            Double montant = Double.parseDouble(montant_depot_TF.getText());
            nc_depot_TF.setText("");
            montant_depot_TF.setText("");
            Compte cmpt_depot;
            if(numCompte.toUpperCase().charAt(0)=='C' || numCompte.charAt(0)=='c' ){
                try {
                    cmpt_depot = CompteCourant.getCompteCourantByCode(numCompte.toUpperCase());
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    return;
                }
            }
            else{
                try {
                    cmpt_depot = CompteEpargne.getCompteEpargneByCode(numCompte.toUpperCase());
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    return;
                }
            }
            try {
                cmpt_depot.deposer(montant);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Le compte N° " + cmpt_depot.getCode() + " a été débité de : " + montant + " DHS");
                alert.initOwner(Main.stage.getOwner());
                alert.show();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Une exception est survenue");
                alert.initOwner(Main.stage.getOwner());
                alert.show();
            }


        }
        else{
            if(!montant_depot_flag && !nc_depot_flag){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Le montant ne doit pas être vide !\nAucun compte n'est associé au numéro entré");
                alert.initOwner(Main.stage.getOwner());
                alert.show();
            }
            else{
                if(!montant_depot_flag){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Le montant ne doit pas être vide !");
                    alert.initOwner(Main.stage.getOwner());
                    alert.show();
                }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Aucun compte n'est associé au numéro entré");
                    alert.initOwner(Main.stage.getOwner());
                    alert.show();
                }
            }
        }
    }
    @FXML
    public void retrait(){
        if(montant_retrait_flag && nc_retrait_flag){
            String numCompte = nc_retrait_TF.getText();
            Double montant = Double.parseDouble(montant_retrait_TF.getText());
            nc_depot_TF.setText("");
            montant_depot_TF.setText("");
            Compte cmpt_retrait;
            if(numCompte.charAt(0)=='C' || numCompte.charAt(0)=='c'){
                try {
                    cmpt_retrait = CompteCourant.getCompteCourantByCode(numCompte.toUpperCase());
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    return;
                }
            }
            else{
                try {
                    cmpt_retrait = CompteEpargne.getCompteEpargneByCode(numCompte.toUpperCase());
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    return;
                }
            }
            try {
                if(cmpt_retrait.retirer(montant)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Le compte N° " + cmpt_retrait.getCode() + " a été crédité de : " + montant + " DHS");
                alert.initOwner(Main.stage.getOwner());
                alert.show();
             }
             else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Le compte N° " + cmpt_retrait.getCode() + " n'a pas de solde suffisant pour retirer " + montant + " DHS");
                    alert.initOwner(Main.stage.getOwner());
                    alert.show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Une exception est survenue");
                alert.initOwner(Main.stage.getOwner());
                alert.show();
            }


        }
        else{
            if(!montant_retrait_flag && !nc_retrait_flag){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Le montant ne doit pas être vide !\nAucun compte n'est associé au numéro entré");
                alert.initOwner(Main.stage.getOwner());
                alert.show();
            }
            else{
                if(!montant_retrait_flag){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Le montant ne doit pas être vide !");
                    alert.initOwner(Main.stage.getOwner());
                    alert.show();
                }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Aucun compte n'est associé au numéro entré");
                    alert.initOwner(Main.stage.getOwner());
                    alert.show();
                }
            }
        }
    }

    @FXML
    public void gotoAgence(MouseEvent e) throws Exception{
        Parent root=FXMLLoader.load(getClass().getResource("../views/Agences.fxml"));
        Main.stage.setScene(new Scene(root));
    }
}
