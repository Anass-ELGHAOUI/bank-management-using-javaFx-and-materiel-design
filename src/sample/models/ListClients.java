package sample.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ListClients {

    //private static List<Client> listClients;

	/*public static List getListClients() throws Exception{
		listClients = new ArrayList<Client>();
		List<Compte> listCompte = new ArrayList<Compte>();
		Statement st = dao.Connect.getConnection().createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM Client");
		while(rs.next()) {
			Statement st2 = dao.Connect.getConnection().createStatement();
			ResultSet rs2 = st2.executeQuery("SELECT * FROM Compte WHERE IdClient="+ rs.getInt(1));
			while(rs2.next()) {
				/*if(rs2.getInt(1)==1)
				listCompte.add(new Compte());
			}
			listClients.add(new Client(rs.getInt(2),rs.getString(3),rs.getString(4),listCompte));
		}
		return listClients;
	}*/

    public static List<Client> getListClientsByAgence(int CodeAgence) throws Exception {
        List<Client> listClients = new ArrayList<Client>();
        PreparedStatement st = Connect.getConnection().prepareStatement("SELECT * FROM Client Where CodeAgence=?");
        st.setInt(1, CodeAgence);
        ResultSet rs = st.executeQuery();
        if(rs.next()) {
            listClients.add(new Client(rs.getInt(2), rs.getString(3), rs.getString(4), (ArrayList)ListComptes.getComptesEpargneByClient(rs.getInt(1)), (ArrayList)ListComptes.getComptesCourantByClient(rs.getInt(1)),rs.getDate("Last_Login")));
        }
        return null;
    }

    public static List<Client> getListClients(int code) throws Exception {
        List<Client> listClients = new ArrayList<Client>();
        PreparedStatement st = Connect.getConnection().prepareStatement("SELECT * FROM Client Where CodeAgence=?");
        st.setInt(1,code);
        ResultSet rs = st.executeQuery();
        while(rs.next()) {
            listClients.add(new Client(rs.getInt(2), rs.getString(3), rs.getString(4), (ArrayList)ListComptes.getComptesEpargneByClient(rs.getInt(1)), (ArrayList)ListComptes.getComptesCourantByClient(rs.getInt(1)),rs.getDate("Last_Login")));
        }
        return listClients;
    }

    public static ObservableList<Client> getListClientsForTableView(int codeAgence) throws Exception{
            ObservableList<Client> listClients =FXCollections.observableArrayList();
            for(Client c : getListClients(codeAgence)){
                listClients.add(c);
            }
            return listClients;
    }

}