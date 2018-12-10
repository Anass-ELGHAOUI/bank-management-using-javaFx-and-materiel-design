package sample.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompteCourant extends Compte{

    private double decouvertAutorisé;

    public CompteCourant(String code,double solde,double decouvertAutorisé) {
        super(code,solde);
        this.decouvertAutorisé=decouvertAutorisé;
    }

    public double getDecouvertAutorisé() {
        return decouvertAutorisé;
    }

    public void setDecouvertAutorisé(double decouvertAutorisé) {
        this.decouvertAutorisé = decouvertAutorisé;
    }

    @Override
    public boolean retirer(double montant) throws SQLException,ClassNotFoundException{
        if((solde+decouvertAutorisé)<montant)
            return false;
        solde-=montant;
        addTransaction(1, montant);
        return saveAccountSolde();
    }

    @Override
    public void deposer(double solde) throws SQLException,ClassNotFoundException{
        addTransaction(0, solde);
        this.solde+=solde;
        saveAccountSolde();
    }

    @Override
    protected boolean saveAccountSolde() throws SQLException,ClassNotFoundException{
        PreparedStatement st = Connect.getConnection().prepareStatement("UPDATE CompteCourant SET Solde=? WHERE CodeCompte='"+code+"'");
        st.setDouble(1,solde);
        int val=st.executeUpdate();
        System.out.println(val);
        return val>0;
    }


    public static CompteCourant getCompteCourantByCode(String Code) throws SQLException,ClassNotFoundException{
        PreparedStatement st= Connect.getConnection().prepareStatement("SELECT * FROM CompteCourant WHERE CodeCompte=? and etat_cpt=0");
        st.setString(1,Code);
        ResultSet rs = st.executeQuery();
        if(rs.next()){
            return new CompteCourant(rs.getString(1),rs.getDouble(2),rs.getDouble(4));
        }
        return null;
    }

    @Override
    public String toString() {
        return "Code : " + code + " \t "  + " \t Solde : " + solde + "\t Découvert autorisé : " + decouvertAutorisé;
    }

    @Override
    public int compareTo(Object o) {

        if(((CompteCourant) o).decouvertAutorisé< decouvertAutorisé)
            return 1;
        if(((CompteCourant) o).decouvertAutorisé==decouvertAutorisé)
            return -1;
        return 0;
    }


    @Override
    public boolean archiver() throws  SQLException,ClassNotFoundException{
        PreparedStatement st = Connect.getConnection().prepareStatement("UPDATE CompteCourant set etat_cpt=1 WHERE codecompte=?");
        st.setString(1,code);
        return st.executeUpdate()>0;
    }

    @Override
    public boolean deleteAccount() throws SQLException,ClassNotFoundException{
        PreparedStatement st = Connect.getConnection().prepareStatement("DELETE FROM CompteCourant WHERE codecompte=?");
        st.setString(1,code);
        return st.executeUpdate()>0;
    }

}
