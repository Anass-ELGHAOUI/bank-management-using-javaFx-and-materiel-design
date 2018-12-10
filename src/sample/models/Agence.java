package sample.models;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.PreparedStatement;

import java.util.ArrayList;
import java.util.Date;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Agence {

    private IntegerProperty codeagence;
    private StringProperty nom;
    private StringProperty adresse;
    private ObjectProperty<LocalDate> dateCreation;
    private ArrayList<Client> clients;
    private BooleanProperty archived;


    public Agence(int codeagence,String nom,ArrayList<Client> clients,String adresse,Date date,boolean archived) {
        this.codeagence=new SimpleIntegerProperty(codeagence);
        this.nom=new SimpleStringProperty(nom);
        this.clients=clients;
        this.adresse = new SimpleStringProperty(adresse);
        this.dateCreation= new SimpleObjectProperty<LocalDate>(LocalDate.parse(date.toString()));
        this.archived=new SimpleBooleanProperty(archived);
    }

    public boolean isArchived() {
        return archived.get();
    }

    public String getAdresse() {
        return adresse.get();
    }

    public void setAdresse(String adresse) {
        this.adresse.set(adresse);
    }

    public int getCodeagence() {
        return codeagence.get();
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public StringProperty adresseProperty() {
        return adresse;
    }

    public IntegerProperty codeagenceProperty() {
        return codeagence;
    }


    public void setCodeagence(int codeagence) {
        this.codeagence.set(codeagence);
    }

    public String getNom() {
        return nom.get();
    }

    public LocalDate getDateCreation() {
        return dateCreation.get();
    }

    public void setDateCreation(LocalDate date) {
        dateCreation.set(date);
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public static String getAgenceNameByCIN(int CIN) throws Exception{
        PreparedStatement st = Connect.getConnection().prepareStatement("SELECT NomAgence FROM Agence Where CodeAgence =(SELECT CodeAgence FROM " +
                "Client WHERE CIN=?)");
        st.setInt(1, CIN);
        ResultSet rs = st.executeQuery();
        rs.next();
        return rs.getString(1);
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public void setClients(ArrayList<Client> clients) {
        this.clients = clients;
    }

    @Override
    public String toString() {
        return "Agence [codeagence=" + codeagence.get() + ", nom=" + nom.get() + ", clients=" + clients + "]";
    }

    public static ObservableList<Agence> getListAgences() throws Exception{
        //Initialiser la liste ( c'est un pattern singleton je pense !!)
        ObservableList<Agence> list = FXCollections.observableArrayList();
        //creation du statement
        Statement st = Connect.getConnection().createStatement();
        //execution de la requette ,  et stocker le resultat dans un vecteur
        ResultSet rs = st.executeQuery("SELECT * FROM AGENCE");
        while(rs.next()) {
            //parcours du vecteur ligne par ligne, creation des objets agences et ajouter ces objets à la liste des agences
            list.add(new Agence(rs.getInt("CodeAgence"),rs.getString("NomAgence"),(ArrayList)ListClients.getListClientsByAgence(rs.getInt("CodeAgence")),rs.getString("Adresse"),rs.getDate("Date_Creation"),rs.getBoolean("is_archived")));
        }
        //retourner la liste des agences
        return list;
    }

    public static  ObservableList<Agence> getListAgencesByParameter(String name) throws Exception{
        //Initialiser la liste ( c'est un pattern singleton je pense !!)
        ObservableList<Agence> list = FXCollections.observableArrayList();
        //Le code d'agence initialisé à null
        Integer code=null;
        try {
            //on essaye de voir si l'utilisateur entre un nombre valide.
            code=Integer.parseInt(name);
        }catch(Exception e) {}
        //requette preparé (pas de SQL INJECTION)
        PreparedStatement st;
        //si le code n'est pas null, donc l'utilisateur a entré un nombre
        //Donc on peut chercher par CodeAgence
        if(code!=null) {
            //preparation de la requette
            st= Connect.getConnection().prepareStatement("SELECT * FROM AGENCE WHERE NomAgence LIKE ? or Adresse LIKE ? or CodeAgence=?");
            //Remplissage du 3 éme parametre
            st.setInt(3, code);
        }
        //Sinon , on recherche par nom et par adresse
        else
            st= Connect.getConnection().prepareStatement("SELECT * FROM AGENCE WHERE NomAgence LIKE ? or Adresse LIKE ?");
        //remplir les 2 autres parametres pour les deux cas
        st.setString(1, "%" + name + "%");
        st.setString(2, "%" + name + "%");

        //executer la requette , et stocker le resultat dans un vecteur
        ResultSet rs = st.executeQuery();
        int i=0;
        while(rs.next()) {
            //parcours du vecteur ligne par ligne, creatino des objets agences et ajouter ces objets à la liste des agences
            list.add(new Agence(rs.getInt("CodeAgence"),rs.getString("NomAgence"),(ArrayList)ListClients.getListClientsByAgence(rs.getInt("CodeAgence")),rs.getString("Adresse"),rs.getDate("Date_Creation"),rs.getBoolean("is_archived")));
            System.out.println(list.get(i).getDateCreation());
        }
        //retourner la liste
        return list;
    }


    public static boolean addAgence(String nom,String adresse) throws Exception{
        Connection conn = Connect.getConnection();
        PreparedStatement st = conn.prepareStatement("INSERT INTO AGENCE (NomAgence,Adresse) VALUES(?,?)");
        st.setString(1, nom);
        st.setString(2, adresse);
        return st.execute();
    }

    public static Agence getAgenceByCompte(int codeCompte) throws Exception{
        Agence agence=null;
        Connection conn = Connect.getConnection();
        PreparedStatement st = conn.prepareStatement("SELECT * FROM Agence WHERE IdAgence = (SELECT IdAgence FROM CompteEpargne WHERE CodeCompte=?)");
        st.setInt(1,codeCompte);
        ResultSet rs = st.executeQuery();
        if(rs.next()) {
            //TODO
            // récupérer l'agence à partir du vecteur ResultSet
            // agence = new Agence()
        }
        return agence;
    }
    public static boolean exists(int code) throws SQLException,ClassNotFoundException{
        PreparedStatement st = Connect.getConnection().prepareStatement("SELECT * FROM Agence WHERE CodeAgence=? and is_archived=0");
        st.setInt(1,code);
        return st.executeQuery().next() && code!=4;
    }

    public static boolean hasClients(int code) throws SQLException,ClassNotFoundException{
        PreparedStatement st = Connect.getConnection().prepareStatement("SELECT * FROM Client WHERE CodeAgence=?");
        st.setInt(1,code);
        if(!st.executeQuery().next()){
            st = Connect.getConnection().prepareStatement("SELECT * FROM Client WHERE CodeAgence=?");
            st.setInt(1,code);
            return st.executeQuery().next();
        }
        return true;
    }

    public static boolean archivate(int code) throws SQLException,ClassNotFoundException{
        PreparedStatement st = Connect.getConnection().prepareStatement("UPDATE Agence SET is_archived=1 WHERE CodeAgence=?");
        st.setInt(1,code);
        return st.executeUpdate()>0;
    }

    public static boolean delete(int code) throws SQLException,ClassNotFoundException{
        PreparedStatement st = Connect.getConnection().prepareStatement("DELETE FROM Agence WHERE CodeAgence=?");
        st.setInt(1,code);
        return st.executeUpdate()>0;
    }



}