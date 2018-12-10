package sample.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompteEpargne extends Compte{

    private double tauxInteret;

    public CompteEpargne(String code,double solde,double tauxInteret) {
        super(code,solde);
        this.tauxInteret=tauxInteret;
    }

    public double getTauxInteret() {
        return tauxInteret;
    }

    public void setTauxInteret(double tauxInteret) {
        this.tauxInteret = tauxInteret;
    }

    @Override
    public boolean retirer(double montant)  throws SQLException,ClassNotFoundException{
        if(montant>solde)
            return false;
        solde-=montant;
        addTransaction(1, montant);
        return saveAccountSolde();
    }

    @Override
    public void deposer(double montant) throws SQLException,ClassNotFoundException {
        solde+=solde+(montant+(montant*(tauxInteret/100)));
        addTransaction(1, montant);
        saveAccountSolde();
    }

    @Override
    protected boolean saveAccountSolde() throws SQLException,ClassNotFoundException{
        PreparedStatement st = Connect.getConnection().prepareStatement("UPDATE CompteEpargne SET Solde=? WHERE CodeCompte='"+code+"'");
        st.setDouble(1,solde);
        return st.executeUpdate()>0;
    }

    public static CompteEpargne getCompteEpargneByCode(String Code) throws SQLException,ClassNotFoundException{
        PreparedStatement st= Connect.getConnection().prepareStatement("SELECT * FROM CompteEpargne WHERE CodeCompte=? and etat_cpt=0");
        st.setString(1,Code);
        ResultSet rs = st.executeQuery();
        if(rs.next()){
            return new CompteEpargne(rs.getString(1),rs.getDouble(2),rs.getDouble(4));
        }
        return null;
    }

    @Override
    public boolean archiver() throws  SQLException,ClassNotFoundException{
        PreparedStatement st = Connect.getConnection().prepareStatement("UPDATE CompteEpargne set etat_cpt=1 WHERE codecompte=?");
        st.setString(1,code);
        return st.executeUpdate()>0;
    }

    @Override
    public boolean deleteAccount() throws SQLException,ClassNotFoundException{
        PreparedStatement st = Connect.getConnection().prepareStatement("DELETE FROM CompteEpargne WHERE codecompte=?");
        st.setString(1,code);
        return st.executeUpdate()>0;
    }

    @Override
    public String toString() {
        return "Code : " + code + " \t "  + " \t Solde : " + solde + " \t Taux d'interet : " + tauxInteret;
    }

    @Override
    public int compareTo(Object o) {

        if(((CompteEpargne) o).tauxInteret< tauxInteret)
            return 1;
        if(((CompteEpargne) o).tauxInteret==tauxInteret)
            return -1;
        return 0;
    }

}
