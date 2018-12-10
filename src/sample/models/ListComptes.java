package sample.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ListComptes {

    public static List<Compte> getComptesEpargneByClient(int idclient) throws Exception{
        PreparedStatement st= Connect.getConnection().prepareStatement("SELECT * from CompteEpargne Where IdClient=?");
        st.setInt(1, idclient);
        ResultSet rss = st.executeQuery();
        ArrayList<Compte> comptes= new ArrayList<Compte>();
        while(rss.next()) {
            comptes.add(new CompteEpargne(rss.getString(1), rss.getDouble(2),rss.getFloat(4)));
        }
        return comptes;
    }

    public static List<Compte> getComptesCourantByClient(int idclient) throws Exception{
        PreparedStatement st= Connect.getConnection().prepareStatement("SELECT * from CompteCourant Where IdClient=?");
        st.setInt(1, idclient);
        ResultSet rss = st.executeQuery();
        ArrayList<Compte> comptes= new ArrayList<Compte>();
        while(rss.next()) {
            comptes.add(new CompteCourant(rss.getString(1), rss.getDouble(2),rss.getFloat(4)));
        }
        return comptes;
    }


}