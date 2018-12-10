package sample.models;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CompteTableModel {
    private StringProperty codecmpt;
    private DoubleProperty solde;
    private StringProperty type;
    private IntegerProperty CIN;
    private StringProperty etat;

    public String getCodecmpt() {
        return codecmpt.get();
    }

    public StringProperty codecmptProperty() {
        return codecmpt;
    }

    public void setCodecmpt(String codecmpt) {
        this.codecmpt.set(codecmpt);
    }

    public double getSolde() {
        return solde.get();
    }

    public DoubleProperty soldeProperty() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde.set(solde);
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public int getCIN() {
        return CIN.get();
    }

    public IntegerProperty CINProperty() {
        return CIN;
    }

    public void setCIN(int CIN) {
        this.CIN.set(CIN);
    }

    public CompteTableModel(String codecmpt, Double solde, String type, Integer CIN,String etat) {
        this.codecmpt = new SimpleStringProperty(codecmpt);
        this.solde = new SimpleDoubleProperty(solde);
        this.type = new SimpleStringProperty(type);
        this.CIN = new SimpleIntegerProperty(CIN);
        this.etat = new SimpleStringProperty(etat);
    }

    public String getEtat() {
        return etat.get();
    }

    public void setEtat(String etat) {
        this.etat.set(etat);
    }

    public StringProperty etatProperty() {
        return etat;
    }

    public static ObservableList<CompteTableModel> getData() throws Exception{
        ObservableList<CompteTableModel> listComptes = FXCollections.observableArrayList();
        PreparedStatement st = Connect.getConnection().prepareStatement("SELECT CodeCompte,Solde,CIN,etat_cpt FROM CompteCourant,Client " +
                "WHERE CompteCourant.IdClient=Client.IdClient");

        ResultSet rs = st.executeQuery();
        while(rs.next()){
            listComptes.add(new CompteTableModel(rs.getString(1),rs.getDouble(2),"Compte Courant",rs.getInt(3),
                    rs.getInt(4)==0 ? "Actif":"Archivé"));
        }
        st = Connect.getConnection().prepareStatement("SELECT CodeCompte,Solde,CIN,etat_cpt FROM CompteEpargne,Client " +
                "WHERE CompteEpargne.IdClient=Client.IdClient");
        rs = st.executeQuery();
        while(rs.next()){
            listComptes.add(new CompteTableModel(rs.getString(1),rs.getDouble(2),"Compte Epargne",rs.getInt(3),
                    rs.getInt(4)==0 ? "Actif":"Archivé"));
        }

        return listComptes;
    }


    public static ObservableList<CompteTableModel> getDataPersonnalized(String value) throws Exception{
        ObservableList<CompteTableModel> listComptes = FXCollections.observableArrayList();
        PreparedStatement st = Connect.getConnection().prepareStatement("SELECT CodeCompte,Solde,CIN,etat_cpt FROM CompteCourant,Client " +
                "WHERE CompteCourant.IdClient=Client.IdClient and CompteCourant.CodeCompte LIKE ?");
        st.setString(1,"%"+value+"%");

        ResultSet rs = st.executeQuery();
        while(rs.next()){
            listComptes.add(new CompteTableModel(rs.getString(1),rs.getDouble(2),"Compte Courant",rs.getInt(3),
                    rs.getInt(4)==0 ? "Actif":"Archivé"));
        }
        st = Connect.getConnection().prepareStatement("SELECT CodeCompte,Solde,CIN,etat_cpt FROM CompteEpargne,Client " +
                "WHERE CompteEpargne.IdClient=Client.IdClient and CompteEpargne.CodeCompte LIKE ?");
        st.setString(1,"%"+value+"%");
        rs = st.executeQuery();
        while(rs.next()){
            listComptes.add(new CompteTableModel(rs.getString(1),rs.getDouble(2),"Compte Epargne",rs.getInt(3),
                    rs.getInt(4)==0 ? "Actif":"Archivé"));
        }

        return listComptes;
    }
}
