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
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import sample.Main;
import sample.models.Client;
import sample.models.Employe;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddClientController implements Initializable {
    @FXML private JFXTextField cinadd;
    @FXML private JFXTextField codeagenceadd;
    @FXML private JFXTextField nomadd;
    @FXML private JFXTextField usernameadd;
    @FXML private JFXPasswordField passwordadd;

    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private AnchorPane parent;
    @FXML
    private TextField search;
    @FXML
    private Label filename;
    @FXML
    private Label bienvenue;

    private static String path="";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        makeStageDrageable();
        Employe empl=(Employe)LoginController.emp;
        bienvenue.setText("Bienvenue : "+empl.getUsername());
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
    public void onActionSubmit(ActionEvent event){
        String text ="";
        Image img=null;
        if(cinadd.getText().length()!=0 && nomadd.getText().length()!=0 && usernameadd.getText().length()!=0 && passwordadd.getText().length()!=0){
            Client clt =new Client(Integer.parseInt(cinadd.getText()),nomadd.getText());
            clt.setUsername(usernameadd.getText());
            clt.setPassword(passwordadd.getText());
            int etat=clt.addclient(clt,Integer.parseInt(codeagenceadd.getText()),path);
            if(etat==1){
                text="le client été ajouté avec succé ! ";
                img=new Image("sample/images/valid.png");
            }else if(etat==-1){
                text="CIN ou password déjà utilisé ! ";
                img=new Image("sample/images/error.png");
            }else{
                text="L'agence que vous avez entré n'existe pas ! ";
                img=new Image("sample/images/error.png");
            }

        }else{
            text="Tout les champs doivent étre complétés ! ";
            img=new Image("sample/images/error.png");
        }
        Notifications notifbuilder=Notifications.create()
                .title("Add client")
                .text(text)
                .graphic(new ImageView(img))
                .hideAfter(Duration.seconds(5))
                .position(Pos.BOTTOM_RIGHT);
        notifbuilder.show();

    }
    @FXML
    public void onActionUsers(MouseEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("../views/UI.fxml"));
        Main.stage.setScene(new Scene(root));

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
            ShowClientController.Cltsearch=Integer.parseInt(search.getText());
            Parent root=FXMLLoader.load(getClass().getResource("../views/ShowClient.fxml"));
            Main.stage.setScene(new Scene(root));
        }
    }
    @FXML
    public void onActionImage(ActionEvent event){
        FileChooser fc=new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Images files","*.jpg", "*.png", "*.jpeg"));
        File selectedfile=fc.showOpenDialog(null);
        if(selectedfile!=null){
              filename.setText(selectedfile.getName());
              path=selectedfile.getAbsolutePath();
        }else{

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
        Parent root = FXMLLoader.load(getClass().getResource("../views/ListgreenTransactions.fxml"));
        Main.stage.setScene(new Scene(root));
        Main.stage.setResizable(false);
    }
    @FXML
    public void newAccount(MouseEvent e) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/Comptes.fxml"));
        Main.stage.setScene(new Scene(root));
    }
}
