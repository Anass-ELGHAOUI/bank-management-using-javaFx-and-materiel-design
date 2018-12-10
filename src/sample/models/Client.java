package sample.models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import sample.Controllers.ShowClientController;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Client {
    private int CIN;
    private int ID;
    private String nom;
    private String username;
    private String password;
    private int codeagence;
    private ObjectProperty<LocalDate> lastLogin;
    private ArrayList<CompteEpargne> comptesepargnes;
    private ArrayList<CompteCourant> comptescourant;


    public LocalDate getLastLogin() {
        return lastLogin.get();
    }

    public ObjectProperty<LocalDate> lastLoginProperty() {
        return lastLogin;
    }

    public void setLastLogin(LocalDate lastLogin) {
        this.lastLogin.set(lastLogin);
    }

    public ArrayList<CompteEpargne> getComptesepargnes() {
        return this.comptesepargnes;
    }

    public void setComptesepargnes(ArrayList<CompteEpargne> comptesepargnes) {
        this.comptesepargnes = comptesepargnes;
    }

    public ArrayList<CompteCourant> getComptescourant() {
        return this.comptescourant;
    }

    public void setComptescourant(ArrayList<CompteCourant> comptescourant) {
        this.comptescourant = comptescourant;
    }

    public int getCodeagence() {
        return codeagence;
    }

    public void setCodeagence(int codeagence) {
        this.codeagence = codeagence;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Client(int CIN, String nom){
        this.CIN=CIN;
        this.nom=nom;
    }
    public Client(int CIN, String nom, String username, ArrayList<CompteEpargne> comptes, ArrayList<CompteCourant> comptescourant, Date lastLogin) {
        this.CIN=CIN;
        this.nom = nom;
        this.username =username;
        this.comptesepargnes=comptes;
        this.comptescourant=comptescourant;
        this.lastLogin= new SimpleObjectProperty<LocalDate>(LocalDate.parse(lastLogin.toString()));
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCIN() {
        return CIN;
    }

    public void setCIN(int CIN) {
        this.CIN = CIN;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int addclient(Client clt,int codeAgence,String path){
        Connection con=Connect.getConnection();

        try {
            if(path.length()==0){
                path="src/sample/images/noph.jpg";
            }
            File img=new File(path);
            FileInputStream fin = new FileInputStream(img);
            Statement exist=con.createStatement();
            ResultSet resultat=exist.executeQuery("SELECT EXISTS( SELECT * FROM gestionbancaire.client WHERE CIN='"+clt.CIN+"' OR password='"+Connect.stringToSha256(clt.password)+"' ) AS client_exists;");
            resultat.next();
            if(resultat.getBoolean("client_exists"))
                return -1;
            ResultSet resultAgence=exist.executeQuery("SELECT EXISTS( SELECT * FROM gestionbancaire.agence WHERE CodeAgence='"+codeAgence+"' ) AS agence_exists;");
            resultAgence.next();
            if(!resultAgence.getBoolean("agence_exists"))
                return -2;
            PreparedStatement statement=con.prepareStatement("INSERT INTO gestionbancaire.client(CIN,Nom,Username,Password,Last_Login,CodeAgence,image) VALUES(?,?,?,?,?,?,?);");
            statement.setInt(1,clt.CIN);
            statement.setString(2,clt.nom);
            statement.setString(3,clt.username);
            statement.setString(4,Connect.stringToSha256(password));
            statement.setDate(5,new java.sql.Date(new java.util.Date().getTime()));
            statement.setInt(6,codeAgence);
            statement.setBinaryStream(7, (InputStream) fin, (int)img.length());
            statement.executeUpdate();
            Client cl=Client.findclient(clt.CIN,0);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static int removeclient(int cin){
        Client clt=findclient(cin,1);
        if(clt!=null){
            int testExiste=0;
            Connection con=Connect.getConnection();
            try {
                Statement exist=con.createStatement();
                ResultSet result=exist.executeQuery("SELECT EXISTS( SELECT * FROM gestionbancaire.comptecourant WHERE IdClient='"+clt.getID()+"' ) AS client_exists;");
                result.next();
                if(result.getBoolean("client_exists")){
                    testExiste=1;
                }
                ResultSet resulta=exist.executeQuery("SELECT EXISTS( SELECT * FROM gestionbancaire.compteepargne WHERE IdClient='"+clt.getID()+"' ) AS client_exists;");
                resulta.next();
                if(resulta.getBoolean("client_exists")){
                    testExiste=1;
                }
                if(testExiste==1){
                    PreparedStatement preparedStatement=con.prepareStatement("UPDATE gestionbancaire.client SET etatClient=? WHERE IdClient=?");
                    preparedStatement.setInt(1,0);
                    preparedStatement.setInt(2,clt.getID());
                    preparedStatement.executeUpdate();
                    return 0;
                }else{
                    PreparedStatement preparedStatement=con.prepareStatement("DELETE FROM gestionbancaire.client WHERE IdClient=?");
                    preparedStatement.setInt(1,clt.getID());
                    preparedStatement.executeUpdate();
                    return 1;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public static Client findclient(int cin,int r){
        Connection con=Connect.getConnection();
        Client clt=null;
        try {
            Statement exist=con.createStatement();
            ResultSet resultat=exist.executeQuery("SELECT EXISTS( SELECT * FROM gestionbancaire.client WHERE CIN='"+cin+"' ) AS client_exists;");
            resultat.next();
            if(resultat.getBoolean("client_exists")){
                ResultSet result=exist.executeQuery("SELECT * FROM gestionbancaire.client WHERE CIN='"+cin+"';");
                result.next();
                clt=new Client(result.getInt("CIN"),result.getString("Nom"));
                clt.setUsername(result.getString("Username"));
                clt.setCodeagence(result.getInt("CodeAgence"));
                clt.setID(result.getInt("IdClient"));
                if(r==0){
                    InputStream in=result.getBinaryStream("image");
                    OutputStream out=new FileOutputStream(new File("src/sample/images/test"+clt.getID()+".jpg"));
                    int c=0;
                    while((c=in.read())>-1){
                        out.write(c);
                    }
                    out.close();
                    in.close();
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clt;
    }
    public static boolean updateclient(Client clt){
        Connection con=Connect.getConnection();
        try {
            PreparedStatement statement;
            if(clt.getPassword().length()==0){
                statement=con.prepareStatement("UPDATE gestionbancaire.client SET Nom=?, Username=?,codeAgence=?, Password=? WHERE CIN=?");
                statement.setString(1,clt.nom);
                statement.setString(2,clt.username);
                statement.setInt(3,clt.codeagence);
                statement.setString(4,clt.getPassword());
                statement.setInt(5,clt.getCIN());
                statement.executeUpdate();
                return true;

            }else{
                statement=con.prepareStatement("UPDATE gestionbancaire.client SET Nom=?, Username=?,codeAgence=? WHERE CIN=?");
                statement.setString(1,clt.nom);
                statement.setString(2,clt.username);
                statement.setInt(3,clt.codeagence);
                statement.setInt(4,clt.getCIN());
                statement.executeUpdate();
                return true;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static ArrayList<Client> getClients(){
        ArrayList<Client> liste=new ArrayList<>();
        Connection con=Connect.getConnection();
        try {
            Statement statement=con.createStatement();
            Client clt=null;
            ResultSet result=statement.executeQuery("SELECT * FROM gestionbancaire.client WHERE etatClient='"+1+"';");
            while (result.next()){
                clt=new Client(result.getInt("CIN"),result.getString("Nom"));
                clt.setUsername(result.getString("Username"));
                clt.setCodeagence(result.getInt("CodeAgence"));
                liste.add(clt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }
    private String correctPath(String path){
        String tmp="";
        for(int i=0;i<path.length();i++){
            tmp+=""+path.charAt(i);
            if(path.charAt(i)=='\\'){
                tmp+="\\";
            }
        }
        return tmp;
    }
    public static Client getClientByUserAndPass(int CIN,String password) throws Exception{
        PreparedStatement st = Connect.getConnection().prepareStatement("SELECT * FROM Client Where CIN=? and password=?");
        st.setInt(1, CIN);
        st.setString(2, password);
        ResultSet rs = st.executeQuery();
        if(rs.next()) {
            System.out.println(rs.getString(4));
            Client clt=new Client(rs.getInt(2), rs.getString(3), rs.getString(4), (ArrayList)ListComptes.getComptesEpargneByClient(rs.getInt(1)), (ArrayList)ListComptes.getComptesCourantByClient(rs.getInt(1)),rs.getDate("Last_Login"));
            clt.setCodeagence(rs.getInt("CodeAgence"));
            clt.setID(rs.getInt(1));
            return clt;
        }
        return null;
    }
    public static Client getClientByCIN(int CIN) throws Exception{
        PreparedStatement st = Connect.getConnection().prepareStatement("SELECT * FROM Client Where CIN=?");
        st.setInt(1, CIN);
        ResultSet rs = st.executeQuery();
        if(rs.next()) {
            System.out.println(rs.getString(4));
            return new Client(rs.getInt(2), rs.getString(3), rs.getString(4), (ArrayList)ListComptes.getComptesEpargneByClient(rs.getInt(1)), (ArrayList)ListComptes.getComptesCourantByClient(rs.getInt(1)),rs.getDate("Last_Login"));
        }
        return null;
    }
    @Override
    public String toString() {
        return "Client [CIN=" + CIN + ", nom=" + nom + ", username=" + username + ", comptesepargnes=" + comptesepargnes
                + ", comptescourant=" + comptescourant + "]";
    }

    public static int getIdClientByCIN(int CIN) throws Exception{
        //initialiser la connexion
        Connection conn = Connect.getConnection();
        PreparedStatement st = conn.prepareStatement("SELECT IdClient FROM Client WHERE CIN=?");
        st.setInt(1,CIN);
        ResultSet rs = st.executeQuery();
        if(rs.next()){
            //Le client existe
            return rs.getInt(1);
        }
        //pas de client avec ce CIN
        return -1;
    }

    public static int getClientAccountsCount(int CIN) throws Exception{
        // Initialiser la variable de connexion
        Connection conn = Connect.getConnection();
        //Nombre de comptes
        int nbraccounts=0;
        //Requette pour savoir le nombre de compte d'epargne du client
        PreparedStatement st = conn.prepareStatement("SELECT COUNT(*) FROM CompteEpargne WHERE IdClient=" +
                "(SELECT IdClient FROM Client WHERE CIN=?) and etat_cpt=0");
        //Requette pour savoir le nombre de compte courant du client
        PreparedStatement st2 = conn.prepareStatement("SELECT COUNT(*) FROM CompteCourant WHERE IdClient=" +
                "(SELECT IdClient FROM Client WHERE CIN=?) and etat_cpt=0");
        st.setInt(1,CIN);
        ResultSet rs = st.executeQuery();
        if(rs.next()){
            // le client a un ou deux comptes d'epargne
            nbraccounts = rs.getInt(1);
        }
        st2.setInt(1,CIN);
        rs=st2.executeQuery();
        if(rs.next()){
            //le client a un ou deux compte courant
            nbraccounts+=rs.getInt(1);
        }
        //retourner le nombre de compte
        return nbraccounts;
    }

    public static int getClientCCAccountsCount(int CIN) throws Exception{
        // Initialiser la variable de connexion
        Connection conn = Connect.getConnection();
        //Nombre de comptes
        int nbraccounts=0;
        //Requette pour savoir le nombre de compte courant du client
        PreparedStatement st2 = conn.prepareStatement("SELECT COUNT(*) FROM CompteCourant WHERE IdClient=" +
                "(SELECT IdClient FROM Client WHERE CIN=?) and etat_cpt=0");
        st2.setInt(1,CIN);
        ResultSet rs=st2.executeQuery();
        if(rs.next()){
            //le client a un ou deux compte courant
            nbraccounts=rs.getInt(1);
        }
        //retourner le nombre de compte
        return nbraccounts;
    }

    public static int getClientCEAccountsCount(int CIN) throws Exception{
        // Initialiser la variable de connexion
        Connection conn = Connect.getConnection();
        //Nombre de comptes
        int nbraccounts=0;
        //Requette pour savoir le nombre de compte courant du client
        PreparedStatement st2 = conn.prepareStatement("SELECT COUNT(*) FROM CompteEpargne WHERE IdClient=" +
                "(SELECT IdClient FROM Client WHERE CIN=?) and etat_cpt=0");
        st2.setInt(1,CIN);
        ResultSet rs=st2.executeQuery();
        if(rs.next()){
            //le client a un ou deux compte courant
            nbraccounts=rs.getInt(1);
        }
        //retourner le nombre de compte
        return nbraccounts;
    }

    public static boolean newCompteCourant(int CIN,double solde,double decouvert,int codeAgence) throws Exception{
        //Récupérer l'identifiant du client à partir du CIN
        int idClient = getIdClientByCIN(CIN);
        // initialiser le codeCompte si c'est le premier à être inséré
        String type="C";
        int code=1;
        //initialiser la connexion
        Connection conn = Connect.getConnection();
        ResultSet rs = conn.createStatement().executeQuery("SELECT CodeCompte FROM CompteCourant ORDER BY codecompte desc limit 1");
        if(rs.next()) {
            //Extraire la partie numérique du code
            code = Integer.parseInt(rs.getString(1).substring(1)) + 1;
        }

        PreparedStatement st = conn.prepareStatement("INSERT INTO CompteCourant VALUES(" +
                "?," +
                "?,?,?,0)");
        st.setString(1,type + code);
        st.setDouble(2,solde);
        st.setInt(3,idClient);
        st.setDouble(4,decouvert);
        return st.execute();
    }

    public static boolean newCompteEpargne(int CIN,double solde,double decouvert,int codeAgence) throws Exception{
        //Récupérer l'identifiant du client à partir du CIN
        int idClient = getIdClientByCIN(CIN);
        // initialiser le codeCompte si c'est le premier à être inséré
        String type="E";
        int code=1;
        //initialiser la connexion
        Connection conn = Connect.getConnection();
        ResultSet rs = conn.createStatement().executeQuery("SELECT CodeCompte FROM CompteEpargne ORDER BY codecompte desc limit 1");
        if(rs.next()) {
            code = Integer.parseInt(rs.getString(1).substring(1)) + 1;

        }
        PreparedStatement st = conn.prepareStatement("INSERT INTO CompteEpargne VALUES(" +
                "?," +
                "?,?,?,0)");
        st.setString(1,type+code);
        st.setDouble(2,solde);
        st.setInt(3,idClient);
        st.setDouble(4,decouvert);
        return st.execute();
    }

    /**
     *
     * @param CIN
     * @return String (retourne le compte courant du client s'il existe, NULL sinon)
     */
    public static String getCCAccount(int CIN) throws SQLException,ClassNotFoundException{
        PreparedStatement st = Connect.getConnection().prepareStatement("SELECT CodeCompte FROM CompteCourant WHERE etat_cpt=0 " +
                "AND IdClient=(SELECT IdClient WHERE CIN=?)");
        st.setInt(1,CIN);
        ResultSet rs= st.executeQuery();
        return rs.next()? rs.getString(1):null;
    }

    /**
     *
     * @param CIN
     * @return String (retourne le compte d'epargne du client s'il existe, NULL sinon)
     */
    public static String getCEAccount(int CIN) throws SQLException,ClassNotFoundException{
        PreparedStatement st = Connect.getConnection().prepareStatement("SELECT CodeCompte FROM CompteEpargne WHERE etat_cpt=0 " +
                "AND IdClient=(SELECT IdClient WHERE CIN=?)");
        st.setInt(1,CIN);
        ResultSet rs= st.executeQuery();
        return rs.next()? rs.getString(1):null;
    }

}

