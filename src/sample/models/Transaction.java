package sample.models;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class Transaction{

    private Date date;
    private int type;
    private double montant;
    private Compte compte;


    public Transaction(Date date,int type, double montant,Compte compte) {
        this.date=date;
        this.type=type;
        this.montant=montant;
        this.compte=compte;
    }

    public Date getDate() {
        return date;
    }

    public int getType() {
        return type;
    }


    public double getMontant() {
        return montant;
    }


    @Override
    public String toString() {
        String typet= type==0? "Dépot":"retrait";
        return "Date de transaction : " + date + "\nType de transaction : " + typet + "\nMontant de transaction : " + montant;
    }
    public boolean saveTransaction()throws SQLException,ClassNotFoundException{
        PreparedStatement st = Connect.getConnection().prepareStatement("INSERT INTO Transaction (DateTran,TypeTran,Montant,CodeCmpt) VALUES(?,?,?,?);");
        st.setDate(1, new java.sql.Date(date.getTime()));
        st.setInt(2,type);
        st.setDouble(3,montant);
        st.setString(4,compte.code);
        return st.execute();
    }

}

