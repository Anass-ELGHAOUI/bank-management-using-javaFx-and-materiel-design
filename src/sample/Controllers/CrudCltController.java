package sample.Controllers;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import sample.Main;
import sample.models.Client;
import sample.models.CompteCourant;
import sample.models.CompteEpargne;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CrudCltController implements Initializable {

    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private AnchorPane parent;
    @FXML
    private AnchorPane cptcourant;
    @FXML
    private Label bienvenue;
    @FXML
    private AnchorPane cptepargne;
    @FXML
    private TextField search;
    @FXML
    private Label labelcptcrt;
    @FXML
    private Label title;
    @FXML
    private Label numcptcrt;
    @FXML
    private Label soldecptcrt;
    @FXML
    private Label decouvcptcrt;
    @FXML
    private Label numcptepar;
    @FXML
    private Label soldecptepar;
    @FXML
    private Label tauxcptepar;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        makeStageDrageable();
        Client clit=(Client)LoginController.emp;
        bienvenue.setText("Bienvenue "+clit.getUsername());
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
        RemplissageComptes();


    }
    void RemplissageComptes(){
        Client clt=(Client)LoginController.emp;
        ArrayList<CompteCourant> cptcrt=clt.getComptescourant();
        ArrayList<CompteEpargne> cptepar=clt.getComptesepargnes();
        if(cptcrt.size()==0 && cptepar.size()==0){
            title.setText("No such Compte");
            cptcourant.setVisible(false);
            cptepargne.setVisible(false);
        }else if(cptcrt.size()==0){
            cptepargne.setVisible(false);
            labelcptcrt.setText("Compte Epargne");
            numcptcrt.setText("N° : "+cptepar.get(0).getCode());
            soldecptcrt.setText("Solde : "+cptepar.get(0).getSolde());
            decouvcptcrt.setText("Taux d'interet : "+cptepar.get(0).getTauxInteret());
        }else{
            numcptcrt.setText("N° : "+cptcrt.get(0).getCode());
            soldecptcrt.setText("Solde : "+cptcrt.get(0).getSolde());
            decouvcptcrt.setText("Decouvert autorisé : "+cptcrt.get(0).getDecouvertAutorisé());

            if(cptepar.size()==0){
                cptepargne.setVisible(false);
            }else{
                numcptepar.setText("N° : "+cptepar.get(0).getCode());
                soldecptepar.setText("Solde : "+cptepar.get(0).getSolde());
                tauxcptepar.setText("Taux d'interet : "+cptepar.get(0).getTauxInteret());
            }
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
    public void onActionTransaction(MouseEvent event) throws IOException {
        Parent root=FXMLLoader.load(getClass().getResource("../views/Transactions.fxml"));
        Main.stage.setScene(new Scene(root));

    }
}
