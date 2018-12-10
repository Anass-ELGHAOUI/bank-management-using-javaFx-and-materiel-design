package sample.Controllers;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import sample.Main;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTreeTableColumn;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.util.Callback;
import sample.models.Client;
import sample.models.Employe;
import sample.models.Pdfgenerate;

public class Controller implements Initializable {

    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private AnchorPane parent;

    @FXML
    private TextField searchinp;

    @FXML
    private ImageView closeicon;

    @FXML
    private JFXTreeTableView<Person> tableview;
    @FXML
    private Label bienvenue;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        makeStageDrageable();
        Employe empl=(Employe)LoginController.emp;
        bienvenue.setText("Bienvenue : "+empl.getUsername());
        Image im=new Image("sample/images/close.png");
        closeicon.setImage(im);
        JFXTreeTableColumn<Person,String> CIN = new JFXTreeTableColumn("CIN");
        CIN.setPrefWidth(160);

        CIN.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Person, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Person, String> param) {
                return param.getValue().getValue().CIN;
            }
        });

        JFXTreeTableColumn<Person,String> Name = new JFXTreeTableColumn("Nom");
        Name.setPrefWidth(160);

        Name.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Person, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Person, String> param) {
                return param.getValue().getValue().Name;
            }
        });

        JFXTreeTableColumn<Person,String> username = new JFXTreeTableColumn("Username");
        username.setPrefWidth(160);

        username.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Person, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Person, String> param) {
                return param.getValue().getValue().Username;
            }
        });
        JFXTreeTableColumn<Person,String> code= new JFXTreeTableColumn("code Agence");
        code.setPrefWidth(112);

        code.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Person, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Person, String> param) {
                return param.getValue().getValue().CodeAgence;
            }
        });

        ArrayList<Client> liste=Client.getClients();
        ObservableList<Person> person = FXCollections.observableArrayList();
        for(int i=0;i<liste.size();i++){
            person.add(new Person(String.valueOf(liste.get(i).getCIN()),liste.get(i).getNom(),liste.get(i).getUsername(),String.valueOf(liste.get(i).getCodeagence())));
        }

        final TreeItem<Person> root = new RecursiveTreeItem<Person>(person, RecursiveTreeObject::getChildren);
        tableview.getColumns().setAll(CIN,Name,username,code);
        tableview.setRoot(root);
        tableview.setShowRoot(false);



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
    class Person extends RecursiveTreeObject<Person> {
        StringProperty CIN;
        StringProperty Name;
        StringProperty Username;
        StringProperty CodeAgence;

        public Person(String CIN,String Name,String Username,String codeAgence)
        {
            this.CIN = new SimpleStringProperty(CIN);
            this.Name  = new SimpleStringProperty(Name);
            this.Username = new SimpleStringProperty(Username);
            this.CodeAgence = new SimpleStringProperty(codeAgence);
        }


    }
    @FXML
    public void onActionExport(ActionEvent event) throws IOException {
        Pdfgenerate.generate();
        Desktop.getDesktop().open(new File("C:\\Users\\Anass\\Desktop\\Clients.pdf"));
    }
    @FXML
    public void onActionAddClt(MouseEvent event) throws IOException {
            Parent root=FXMLLoader.load(getClass().getResource("../views/AddClt.fxml"));
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
    public void onActionclose(MouseEvent event) throws IOException {
        System.exit(0);

    }

    @FXML
    public void buttonPressed(KeyEvent e) throws IOException {
        if(e.getCode().equals(KeyCode.ENTER))
        {
            ShowClientController.Cltsearch=Integer.parseInt(searchinp.getText());
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
