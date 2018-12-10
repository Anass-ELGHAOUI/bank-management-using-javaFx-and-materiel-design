package sample.Controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import sample.Main;
import sample.models.Client;
import sample.models.Compte;
import sample.models.CompteCourant;
import sample.models.CompteEpargne;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CltTransactionController implements Initializable {

    @FXML
    private JFXTextField code_debit;
    @FXML
    private JFXTextField code_credit;
    @FXML
    private ImageView imgtran;
    @FXML
    private JFXTextField montant;
    @FXML
    private Label credit_lab;
    @FXML
    private Label debit_lab;
    @FXML
    private Label bienvenue;

    @FXML
    private AnchorPane parent;
    private boolean montant_depot_flag;
    private boolean montant_retrait_flag;
    private boolean montant_flag;
    private boolean credit_flag;
    private boolean debit_flag;
    private boolean nc_depot_flag;
    private boolean nc_retrait_flag;
    private Compte comptedebit;
    private Compte comptecredit;
    private double xOffset = 0;
    private double yOffset = 0;

    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        makeStageDrageable();
        Image img=new Image("sample/images/virement.png");
        imgtran.setImage(img);
        Client clit=(Client)LoginController.emp;
        bienvenue.setText("Bienvenue "+clit.getUsername());
        credit_flag = false;
        debit_flag = false;
        montant_flag = false;
        code_credit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 0) {
                if (!Character.isDigit(newValue.charAt(newValue.length() - 1)) && Character.isLetter(0)) {
                    //code_credit.setText(newValue.substring(0, newValue.length()-1));
                }
            }
            try {
                Client clt = ((Client) LoginController.emp);
                boolean condition=false;
                if(code_credit.getText().charAt(0)=='C'){
                    if(!clt.getComptescourant().isEmpty()){
                        condition=(clt.getComptescourant().get(0).getCode().equals(newValue));
                    }
                }else{
                    if(!clt.getComptesepargnes().isEmpty()){
                        condition=(clt.getComptesepargnes().get(0).getCode().equals(newValue));
                    }
                }
                if (!condition) {
                    credit_lab.setTextFill(Paint.valueOf("RED"));
                    code_credit.setUnFocusColor(Paint.valueOf("RED"));
                    code_credit.setFocusColor(Paint.valueOf("RED"));
                    credit_flag = false;
                } else {
                    credit_lab.setTextFill(Paint.valueOf("GREEN"));
                    code_credit.setUnFocusColor(Paint.valueOf("GREEN"));
                    code_credit.setFocusColor(Paint.valueOf("GREEN"));
                    credit_flag = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        code_debit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 0) {
                if (!Character.isDigit(newValue.charAt(newValue.length() - 1)) && Character.isLetter(newValue.charAt(0))) {
                    //code_debit.setText(newValue.substring(0, newValue.length()-1));
                }
                try {
                    if (!Compte.exists(newValue)) {
                        debit_lab.setTextFill(Paint.valueOf("RED"));
                        code_debit.setUnFocusColor(Paint.valueOf("RED"));
                        code_debit.setFocusColor(Paint.valueOf("RED"));
                        debit_flag = false;
                    } else {
                        debit_lab.setTextFill(Paint.valueOf("GREEN"));
                        code_debit.setUnFocusColor(Paint.valueOf("GREEN"));
                        code_debit.setFocusColor(Paint.valueOf("GREEN"));
                        debit_flag = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        montant.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.length() > 0) {
                    if (!Character.isDigit(newValue.charAt(newValue.length() - 1)) && newValue.charAt(newValue.length() - 1) != '.') {
                        montant.setText(newValue.substring(0, newValue.length() - 1));
                    }
                    if (montant.getText().length() > 0 && Integer.parseInt(montant.getText()) != 0)
                        montant_flag = true;
                } else
                    montant_flag = false;
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
    public void onMouseClickedDashboard(MouseEvent e) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/UIHome.fxml"));
        Scene scene = new Scene(root);
        Main.stage.setScene(scene);
    }

    @FXML
    public void onMouseEntered(MouseEvent e) throws Exception{
        Main.stage.getScene().setCursor(Cursor.HAND);

    }

    @FXML
    public void deleteAccount(MouseEvent e) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/DeleteAccount.fxml"));
        Main.stage.setScene(new Scene(root));
    }

    @FXML
    public void newListTransaction() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/ListTransactions.fxml"));
        Main.stage.setScene(new Scene(root));
        Main.stage.setResizable(false);
    }

    @FXML
    public void addAccount(MouseEvent e) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/Comptes.fxml"));
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
    public void validateTransaction(ActionEvent e) throws Exception{

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
                Client clt=(Client)LoginController.emp;
                LoginController.emp=Client.getClientByCIN(clt.getCIN());
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText(error);
            alert.initOwner(Main.stage.getOwner());
            alert.show();
        }


    }
    @FXML
    public void onActionProfile(MouseEvent event) throws IOException {
        Parent root=FXMLLoader.load(getClass().getResource("../views/ProfileClt.fxml"));
        Main.stage.setScene(new Scene(root));

    }
    @FXML
    public void onLogout(ActionEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("../views/Login.fxml"));
        Main.stage.setScene(new Scene(root));

    }
    @FXML
    public void onActionComptes(MouseEvent event) throws IOException {
        Parent root=FXMLLoader.load(getClass().getResource("../views/CrudClt.fxml"));
        Main.stage.setScene(new Scene(root));

    }
}
