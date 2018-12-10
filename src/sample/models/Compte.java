package sample.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public abstract class Compte implements Comparable{

    protected String code;
    //protected String nom;
    protected double solde;
    private static int counter;
    private ArrayList<Transaction> transactions;
    //private Transaction[] transactions;
    //public static int nbTransact=100;
    //private int nblignesRéel;

    public Compte(/*String nom,*/double solde) {
        code="";
        //this.nom=nom;
        this.solde=solde;
        transactions = new ArrayList<>();
        //transactions = new Transaction[nbTransact];
        //nblignesRéel=-1;
    }

    public Compte(String code,double solde) {
        this.code=code;
        //this.nom=nom;
        this.solde=solde;
        transactions = new ArrayList<>();
    }

    public String getCode(){
        return code;
    }

    public void addTransaction(int type,double montant) throws SQLException,ClassNotFoundException{
		/*if(nblignesRéel==99) return false;
			transactions[++nblignesRéel]= new Transaction(new Date(), type, montant);
		return true;*/
        Transaction tmp = new Transaction(new Date(), type, montant,this);
        transactions.add(tmp);
        tmp.saveTransaction();
    }

    public abstract void deposer(double solde) throws SQLException,ClassNotFoundException;

    public abstract boolean retirer(double montant)  throws SQLException,ClassNotFoundException;

    protected abstract boolean saveAccountSolde() throws SQLException,ClassNotFoundException;

    public boolean hasTransactions() throws SQLException,ClassNotFoundException{
        PreparedStatement st = Connect.getConnection().prepareStatement("SELECT COUNT(*) FROM Transaction WHERE CodeCmpt=?");
        st.setString(1, code);
        ResultSet rs = st.executeQuery();
        rs.next();
        return rs.getInt(1)>0;
    }

    public abstract boolean archiver() throws SQLException,ClassNotFoundException;

    public abstract boolean deleteAccount() throws SQLException,ClassNotFoundException;

    public void afficher() {
        System.out.println(toString()+ "\nl'ensemble des transaction du compte N° " + code +  " : \n");
		/*for(int i=0;i<=nblignesRéel;i++) {
			System.out.println(transactions[i]);
		}*/
        Iterator<Transaction> iterator =transactions.iterator();
        while(iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }


    public static boolean exists(String CodeCompte) throws SQLException,ClassNotFoundException{
        PreparedStatement st= Connect.getConnection().prepareStatement("SELECT * FROM CompteCourant WHERE CodeCompte=? AND etat_cpt=0");
        st.setString(1,CodeCompte);
        if(!st.executeQuery().next()){
            PreparedStatement stt= Connect.getConnection().prepareStatement("SELECT * FROM CompteEpargne WHERE CodeCompte=? AND etat_cpt=0");
            stt.setString(1,CodeCompte);
            ResultSet rs=stt.executeQuery();
            return rs.next();
        }
        return true;
    }
    @Override
    public abstract String toString();

    public double getSolde() {
        return solde;
    }


}
