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

public class ProfileCltController implements Initializable {

    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private AnchorPane parent;
    @FXML private ImageView imgprofile;

    @FXML
    private Label cinprofile;
    @FXML
    private Label nomprofile;
    @FXML
    private Label usernameprofile;
    @FXML
    private Label codeagenceprofile;
    @FXML
    private Label bienvenue;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        makeStageDrageable();
        Client clt=(Client)LoginController.emp;
        Image img=new Image("sample/images/test"+clt.getID()+".jpg");
        Client clit=(Client)LoginController.emp;
        bienvenue.setText("Bienvenue "+clit.getUsername());
        imgprofile.setImage(img);
        cinprofile.setText("CIN : "+clt.getCIN());
        nomprofile.setText("Nom : "+clt.getNom());
        usernameprofile.setText("Username : "+clt.getUsername());
        codeagenceprofile.setText("Code Agence : "+clt.getCodeagence());
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
    public void onActionComptes(MouseEvent event) throws IOException {
        Parent root=FXMLLoader.load(getClass().getResource("../views/CrudClt.fxml"));
        Main.stage.setScene(new Scene(root));

    }
    @FXML
    public void onActionTransaction(MouseEvent event) throws IOException {
        Parent root=FXMLLoader.load(getClass().getResource("../views/Transactions.fxml"));
        Main.stage.setScene(new Scene(root));

    }
    @FXML
    public void onLogout(ActionEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("../views/Login.fxml"));
        Main.stage.setScene(new Scene(root));

    }

}

