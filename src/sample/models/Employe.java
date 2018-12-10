package sample.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

public class Employe {

    private int IdEmploye;
    private String nom;
    private int cin;
    private String username;
    private Date created_at;
    private Date updated_at;
    private boolean isAdmin;
    private Agence agence;

    public Employe(int idEmploye, String nom, int cin,String username, Date created_at, Date updated_at, boolean isAdmin,
                   Agence agence) {
        IdEmploye = idEmploye;
        this.nom = nom;
        this.username = username;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.isAdmin = isAdmin;
        this.agence = agence;
        this.cin=cin;
    }

    public int getIdEmploye() {
        return IdEmploye;
    }

    public void setIdEmploye(int idEmploye) {
        IdEmploye = idEmploye;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Agence getAgence() {
        return agence;
    }

    public void setAgence(Agence agence) {
        this.agence = agence;
    }


    public static Employe getEmployeByUserAndPass(int CIN,String password) throws Exception{
        PreparedStatement st = Connect.getConnection().prepareStatement("SELECT * FROM Employe Where cin=? and pass=?");
        st.setInt(1, CIN);
        st.setString(2, password);
        ResultSet rs = st.executeQuery();
        if(rs.next()) {
            //récupération des informations de l'agence
            st= Connect.getConnection().prepareStatement("SELECT * from agence Where CodeAgence=?");
            st.setInt(1, rs.getInt(9));
            ResultSet rss = st.executeQuery();
            Agence agence=null;
            if(!rss.next())
                //erreur ! l'agence n'est pas identifié !!
                return null;
            //récupérer les clients de l'agences par la methode getListClientsByAgence et création de l'objet Agence
            agence = new Agence(rss.getInt(1),rss.getString(2),(ArrayList)ListClients.getListClientsByAgence(rss.getInt(1)),rss.getString(3),rss.getDate("Date_Creation"),rss.getBoolean("is_archived"));
            return new Employe(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getString(4),rs.getDate(6),rs.getDate(7),rs.getInt(8)==1? true:false,agence);
        }
        return null;
    }

    @Override
    public String toString() {
        return "Employe [IdEmploye=" + IdEmploye + ", nom=" + nom + ", username=" + username + ", created_at="
                + created_at + ", updated_at=" + updated_at + ", isAdmin=" + isAdmin + ", agence=" + agence + "]";
    }





}

