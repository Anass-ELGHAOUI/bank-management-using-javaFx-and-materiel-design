package sample.Controllers;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.sun.prism.paint.Color;
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
import sample.models.Connect;
import sample.models.Employe;
import sample.models.Pdfgenerate;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UpdCltController implements Initializable {
    @FXML private JFXTextField cinupd;
    @FXML private JFXTextField codeagenceupd;
    @FXML private JFXTextField nomupd;
    @FXML private JFXTextField usernameupd;
    @FXML private JFXPasswordField passwordupd;
    @FXML private JFXButton subupdate;

    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private AnchorPane parent;
    @FXML
    private TextField searchinp;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        codeagenceupd.setVisible(false);
        nomupd.setVisible(false);
        usernameupd.setVisible(false);
        passwordupd.setVisible(false);
        subupdate.setVisible(false);
        makeStageDrageable();

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
    public void onActionUpdate(ActionEvent event){
        int etat=0;
        if(codeagenceupd.getText().length()==0){
                etat=1;
                codeagenceupd.setFocusColor(javafx.scene.paint.Color.valueOf("RED"));
                codeagenceupd.setUnFocusColor(javafx.scene.paint.Color.valueOf("RED"));
        }
        if(nomupd.getText().length()==0){
            etat=1;
            nomupd.setFocusColor(javafx.scene.paint.Color.valueOf("RED"));
            nomupd.setUnFocusColor(javafx.scene.paint.Color.valueOf("RED"));
        }
        if(usernameupd.getText().length()==0){
            etat=1;
            usernameupd.setFocusColor(javafx.scene.paint.Color.valueOf("RED"));
            usernameupd.setUnFocusColor(javafx.scene.paint.Color.valueOf("RED"));
        }
        Client clt=null;
        if(etat==0){
            cinupd.setFocusColor(javafx.scene.paint.Color.valueOf("BLACK"));
            cinupd.setUnFocusColor(javafx.scene.paint.Color.valueOf("BLACK"));
            usernameupd.setFocusColor(javafx.scene.paint.Color.valueOf("BLACK"));
            usernameupd.setUnFocusColor(javafx.scene.paint.Color.valueOf("BLACK"));
            nomupd.setFocusColor(javafx.scene.paint.Color.valueOf("BLACK"));
            nomupd.setUnFocusColor(javafx.scene.paint.Color.valueOf("BLACK"));
            clt=new Client(Integer.parseInt(cinupd.getText()),nomupd.getText());
            clt.setUsername(usernameupd.getText());
            clt.setCodeagence(Integer.parseInt(codeagenceupd.getText()));
        }
        if(passwordupd.getText().length()!=0 && etat==0){
             clt.setPassword(Connect.stringToSha256(passwordupd.getText()));
        }else if(etat==0){
             clt.setPassword("");
        }
        if(etat==0){
            String txt="";
            Image img=null;
            boolean et=Client.updateclient(clt);
            if(et==false){
                txt="Echec de modification !";
                img=new Image("sample/images/error.png");
            }else{
                txt="Modification avec succé !";
                img=new Image("sample/images/valid.png");
            }
            Notifications notifbuilder=Notifications.create()
                    .title("Update client")
                    .text(txt)
                    .graphic(new ImageView(img))
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.BOTTOM_RIGHT);
            notifbuilder.show();
        }


    }
    @FXML
    public void onUpdateClt(MouseEvent event) throws IOException {
        Parent root=FXMLLoader.load(getClass().getResource("../views/UpdClt.fxml"));
        Main.stage.setScene(new Scene(root));

    }
    @FXML
    public void onActionAddClt(MouseEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("../views/AddClt.fxml"));
        Main.stage.setScene(new Scene(root));

    }
    @FXML
    public void onActionRemoveClt(MouseEvent event) throws IOException {
        Parent root=FXMLLoader.load(getClass().getResource("../views/DeleteClt.fxml"));
        Main.stage.setScene(new Scene(root));

    }
    @FXML
    public void buttonPressed(KeyEvent e) throws IOException {
        if(e.getCode().equals(KeyCode.ENTER))
        {

            Client clt=Client.findclient(Integer.parseInt(cinupd.getText()),1);
            if(clt==null){
                cinupd.setFocusColor(javafx.scene.paint.Color.valueOf("RED"));
                cinupd.setUnFocusColor(javafx.scene.paint.Color.valueOf("RED"));
            }else{
                codeagenceupd.setVisible(true);
                codeagenceupd.setText("1");
                nomupd.setVisible(true);
                nomupd.setText(clt.getNom());
                usernameupd.setVisible(true);
                usernameupd.setText(clt.getUsername());
                passwordupd.setVisible(true);
                subupdate.setVisible(true);
                cinupd.setEditable(false);
                cinupd.setFocusColor(javafx.scene.paint.Color.valueOf("BLACK"));
                cinupd.setUnFocusColor(javafx.scene.paint.Color.valueOf("BLACK"));
            }

        }
    }
    @FXML
    public void searchPressed(KeyEvent e) throws IOException {
        if(e.getCode().equals(KeyCode.ENTER))
        {
            ShowClientController.Cltsearch=Integer.parseInt(searchinp.getText());
            Parent root=FXMLLoader.load(getClass().getResource("../views/ShowClient.fxml"));
            Main.stage.setScene(new Scene(root));
        }
    }
    @FXML
    public void onActionUsers(MouseEvent event) throws IOException {
        Parent root=FXMLLoader.load(getClass().getResource("../views/UI.fxml"));
        Main.stage.setScene(new Scene(root));

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
