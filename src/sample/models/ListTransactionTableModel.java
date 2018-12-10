package sample.models;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Date;

public class ListTransactionTableModel {
    private StringProperty codecompte;
    private StringProperty type;
    private ObjectProperty<LocalDate> dateTran;
    private DoubleProperty montant;
    private IntegerProperty Cin;

    public int getCin() {
        return Cin.get();
    }

    public void setCin(int cin) {
        this.Cin.set(cin);
    }

    public ListTransactionTableModel(String code, String type, Date dateTran, double Montant,int Cin){
        this.dateTran= new SimpleObjectProperty<LocalDate>(LocalDate.parse(dateTran.toString()));
        this.codecompte = new SimpleStringProperty(code);
        this.type = new SimpleStringProperty(type);
        this.montant = new SimpleDoubleProperty(Montant);
        this.Cin=new SimpleIntegerProperty(Cin);
    }

    public String getCodecompte() {
        return codecompte.get();
    }

    public StringProperty codecompteProperty() {
        return codecompte;
    }

    public void setCodecompte(String codecompte) {
        this.codecompte.set(codecompte);
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

    public LocalDate getDateTran() {
        return dateTran.get();
    }

    public ObjectProperty<LocalDate> dateTranProperty() {
        return dateTran;
    }

    public void setDateTran(LocalDate dateTran) {
        this.dateTran.set(dateTran);
    }

    public double getMontant() {
        return montant.get();
    }

    public DoubleProperty montantProperty() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant.set(montant);
    }

    public static ObservableList<ListTransactionTableModel> getData() throws Exception{
        ObservableList<ListTransactionTableModel> listTransactions = FXCollections.observableArrayList();
        PreparedStatement st = Connect.getConnection().prepareStatement("SELECT DateTran,TypeTran,Montant,Transaction.CodeCmpt,C.CIN FROM  " +
                "Transaction,CompteCourant CC,Client C Where Transaction.CodeCmpt=CC.CodeCompte" +
                " and CC.IdClient=C.IdClient");
        ResultSet rs = st.executeQuery();
        while(rs.next()){
            listTransactions.add(new ListTransactionTableModel(rs.getString(4),rs.getInt(2)==1?"Retrait" : "Dépot",
                    rs.getDate(1),rs.getDouble(3),rs.getInt(5)));
        }
        // Récupérer les transactions des comptes d'epargnes
        st = Connect.getConnection().prepareStatement("SELECT DateTran,TypeTran,Montant,Transaction.CodeCmpt,C.CIN FROM  " +
                "Transaction,CompteEpargne CE,Client C Where Transaction.CodeCmpt=CE.CodeCompte" +
                " and CE.IdClient=C.IdClient ");
        rs = st.executeQuery();
        while(rs.next()){
            listTransactions.add(new ListTransactionTableModel(rs.getString(4),rs.getInt(2)==1?"Retrait" : "Dépot",
                    rs.getDate(1),rs.getDouble(3),rs.getInt(5)));
        }

        return listTransactions;
    }

    public static ObservableList<ListTransactionTableModel> getData(String value) throws Exception{
        ObservableList<ListTransactionTableModel> listTransactions = FXCollections.observableArrayList();
        PreparedStatement st = Connect.getConnection().prepareStatement("SELECT DateTran,TypeTran,Montant,Transaction.CodeCmpt,C.CIN FROM " +
                " Transaction,CompteCourant CC,Client C Where Transaction.CodeCmpt=CC.CodeCompte" +
                " and CC.IdClient=C.IdClient and (CC.CodeCompte LIKE ? or C.CIN=?) ");
        st.setString(1,"%"+value+"%");
        try {
            st.setInt(2, Integer.parseInt(value));
        }catch(NumberFormatException e){
            st.setInt(2, 0);
        }

        ResultSet rs = st.executeQuery();
        while(rs.next()){
            listTransactions.add(new ListTransactionTableModel(rs.getString(4),rs.getInt(2)==1?"Retrait" : "Dépot",
                    rs.getDate(1),rs.getDouble(3),rs.getInt(5)));
        }
        // Récupérer les transactions des comptes d'epargnes
        st = Connect.getConnection().prepareStatement("SELECT DateTran,TypeTran,Montant,Transaction.CodeCmpt,C.CIN FROM Transaction,CompteEpargne CC,Client C Where Transaction.CodeCmpt=CC.CodeCompte and CC.IdClient=C.IdClient and (CC.CodeCompte LIKE ? or C.CIN=?) ");
        st.setString(1,"%"+value+"%");
        try {
            st.setInt(2, Integer.parseInt(value));
        }catch(NumberFormatException e){
            st.setInt(2, 0);
        }
        rs = st.executeQuery();
        while(rs.next()){
            listTransactions.add(new ListTransactionTableModel(rs.getString(4),rs.getInt(2)==1?"Retrait" : "Dépot",
                    rs.getDate(1),rs.getDouble(3),rs.getInt(5)));
        }

        return listTransactions;
    }

    public static ObservableList<ListTransactionTableModel> getDataReleve(String code) throws Exception{
        ObservableList<ListTransactionTableModel> listTransactions = FXCollections.observableArrayList();
        PreparedStatement st = Connect.getConnection().prepareStatement("SELECT DateTran,TypeTran,Montant,Transaction.CodeCmpt,C.CIN FROM  " +
                "Transaction,CompteCourant CC,Client C Where Transaction.CodeCmpt=CC.CodeCompte" +
                " and CC.IdClient=C.IdClient and CC.CodeCompte=?");
        st.setString(1,code);
        ResultSet rs = st.executeQuery();
        while(rs.next()){
            listTransactions.add(new ListTransactionTableModel(rs.getString(4),rs.getInt(2)==1?"Retrait" : "Dépot",
                    rs.getDate(1),rs.getDouble(3),rs.getInt(5)));
        }
        // Récupérer les transactions des comptes d'epargnes
        st = Connect.getConnection().prepareStatement("SELECT DateTran,TypeTran,Montant,Transaction.CodeCmpt,C.CIN FROM  " +
                "Transaction,CompteEpargne CE,Client C Where Transaction.CodeCmpt=CE.CodeCompte" +
                " and CE.IdClient=C.IdClient and CE.CodeCompte=?");
        st.setString(1,code);
        rs = st.executeQuery();
        while(rs.next()){
            listTransactions.add(new ListTransactionTableModel(rs.getString(4),rs.getInt(2)==1?"Retrait" : "Dépot",
                    rs.getDate(1),rs.getDouble(3),rs.getInt(5)));
        }

        return listTransactions;
    }
}

