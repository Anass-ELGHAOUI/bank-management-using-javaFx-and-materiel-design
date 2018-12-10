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
import sample.models.Employe;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ShowClientController implements Initializable {
    @FXML private ImageView imgshow;
    @FXML private Label cinshow;
    @FXML private Label nomshow;
    @FXML private Label usernameshow;
    @FXML private Label codeagenceshow;
    @FXML private Label etatshow;

    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private AnchorPane parent;
    @FXML
    private AnchorPane content;
    @FXML
    private Label bienvenue;

    @FXML
    private TextField search;
    public static int Cltsearch=12;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        makeStageDrageable();
        Employe empl=(Employe)LoginController.emp;
        bienvenue.setText("Bienvenue : "+empl.getUsername());
        Client clt=Client.findclient(Cltsearch,1);
        Image img=new Image("sample/images/test"+clt.getID()+".jpg");
        imgshow.setImage(img);
        if(clt!=null){
            cinshow.setText("CIN : "+String.valueOf(clt.getCIN()));
            nomshow.setText("Nom : "+clt.getNom());
            usernameshow.setText("Username : "+clt.getUsername());
            codeagenceshow.setText("Code Agence : "+String.valueOf(clt.getCodeagence()));
            etatshow.setText("Etat client : 1");
        }

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
    public void onActionAddClt(MouseEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("../views/AddClt.fxml"));
        Main.stage.setScene(new Scene(root));

    }
    @FXML
    public void onUpdateClt(MouseEvent event) throws IOException {
        Parent root=FXMLLoader.load(getClass().getResource("../views/UpdClt.fxml"));
        Main.stage.setScene(new Scene(root));

    }
    @FXML
    public void onActionRemoveClt(MouseEvent event) throws IOException {
        Parent root=FXMLLoader.load(getClass().getResource("../views/DeleteClt.fxml"));
        Main.stage.setScene(new Scene(root));

    }
    @FXML
    public void onActionUsers(MouseEvent event) throws IOException {
        Parent root=FXMLLoader.load(getClass().getResource("../views/UI.fxml"));
        Main.stage.setScene(new Scene(root));

    }
    @FXML
    public void buttonPressed(KeyEvent e) throws IOException {
        if(e.getCode().equals(KeyCode.ENTER))
        {
            ShowClientController.Cltsearch=Integer.parseInt(search.getText());
            Parent root=FXMLLoader.load(getClass().getResource("../views/ShowClient.fxml"));
            Main.stage.setScene(new Scene(root));
        }
    }
    @FXML
    public void onLogout(ActionEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("../views/Login.fxml"));
        Main.stage.setScene(new Scene(root));

    }
    @FXML
    public void onMouseClickedDashboard(MouseEvent e) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/UIHome.fxml"));
        Scene scene = new Scene(root);
        Main.stage.setScene(scene);
    }
    @FXML
    public void listTransactions(MouseEvent e) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/ListTransactions.fxml"));
        Main.stage.setScene(new Scene(root));
        Main.stage.setResizable(false);
    }
    @FXML
    public void newAccount(MouseEvent e) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/Comptes.fxml"));
        Main.stage.setScene(new Scene(root));
    }

}
