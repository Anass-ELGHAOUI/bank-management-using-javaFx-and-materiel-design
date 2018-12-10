package sample.Controllers;

import com.jfoenix.controls.JFXButton;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import sample.Main;
import sample.models.Client;
import sample.models.Connect;
import sample.models.Employe;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class LoginController implements Initializable {


    private double xOffset = 0;
    private double yOffset = 0;
    @FXML private ImageView loginimg;
    @FXML private ImageView fullimg;
    @FXML private ImageView closeimg;
    @FXML private ImageView logoentr;
    @FXML
    private AnchorPane parent;

    private Date authDate;
    private int cin;
    @FXML
    private JFXButton logbtn;
    @FXML
    private JFXTextField usernameTF;
    @FXML
    private JFXPasswordField passField;
    //private Client cli;
    protected static Object emp;


    public LoginController(int cin) {
        authDate = new Date();
        this.cin=cin;
        emp=null;
    }
    public LoginController() {
        //authDate = new Date();
        //username=usernameTF.getText();
        emp=null;
    }

    public static Object getEmp(){
        return emp;
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        makeStageDrageable();
        Image img=new Image("sample/images/1.jpg");
        fullimg.setImage(img);
        Image imgi=new Image("sample/images/log.png");
        loginimg.setImage(imgi);
        Image imgid=new Image("sample/images/close-icon.png");
        closeimg.setImage(imgid);
        Image imgl=new Image("sample/images/logo.png");
        logoentr.setImage(imgl);
        usernameTF.textProperty().addListener((observable, oldValue, newValue) -> {
            //Ne doit accepter que les nombres
            if(newValue.length()>0) {
                if(!Character.isDigit(newValue.charAt(newValue.length()-1))) {
                    usernameTF.setText(newValue.substring(0, newValue.length()-1));
                }
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
    public void onActionClose(MouseEvent event){
        System.exit(0);
    }

    @FXML
    public void onClickLoginController(ActionEvent e) {

        //System.out.println(cin);
        Window owner = logbtn.getScene().getWindow();
        try {
            if(usernameTF.getText().equals("") || passField.getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attention");
                alert.setHeaderText(null);
                alert.setContentText("Aucun des champs ne doit être vide");
                alert.initOwner(owner);
                alert.show();
                return;
            }
			/*if(usernameTF.getText().contains(" ")) {
				Alert alert = new Alert(Alert.AlertType.WARNING);
		        alert.setTitle("Attention");
		        alert.setHeaderText(null);
		        alert.setContentText("Le nom d'utilisateur ne doit pas contenir des espaces");
		        alert.initOwner(owner);
		        alert.show();
		        return;
			}*/
            cin=Integer.parseInt(usernameTF.getText());
            if(!authentificate()){

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Authentification Error");
                alert.setHeaderText(null);
                alert.setContentText("Incorrect username or password. Please retry !");
                alert.initOwner(owner);
                alert.show();
            }
            else {

                if(emp instanceof Client) {
                    ClientInterface();
                }
                else {
                    if(((Employe) emp).getAgence().getCodeagence()!=4) {
                        launchInterface();
                    }
                    else{
                        launchSiegeInterface();
                    }
                }


                //new SiegeController(owner);

            }
        } catch (Exception e1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Authentification Error");
            alert.setHeaderText(null);
            alert.setContentText("An error was occured ! please retry or contact the administrator");
            alert.initOwner(owner);
            alert.show();
            e1.printStackTrace();
        }

    }

    public boolean authentificate() throws Exception{

        emp = Client.getClientByUserAndPass(cin, Connect.stringToSha256(passField.getText()));
        if(emp==null) {
            emp= Employe.getEmployeByUserAndPass(cin, Connect.stringToSha256(passField.getText()));
        }
        return emp==null? false:true;
    }

    public void launchInterface() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../views/UI.fxml"));
        //window.getScene().setRoot(root);
        //Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        Scene scene = new Scene(root);
        //Main.stage.initOwner();
        Main.stage.setScene(scene);
        Main.stage.setResizable(false);
    }

    public void launchSiegeInterface() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/Siege_home.fxml"));
        //window.getScene().setRoot(root);
        //Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        Scene scene = new Scene(root);
        //Main.stage.initOwner();
        Main.stage.setScene(scene);
    }
    public void ClientInterface() throws Exception{
        Parent root=FXMLLoader.load(getClass().getResource("../views/CrudClt.fxml"));
        Main.stage.setScene(new Scene(root));
    }


}
