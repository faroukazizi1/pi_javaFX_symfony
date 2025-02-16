package Service;
import Model.user;
import Util.DBconnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class userService implements IService<user> {

    Connection conn;

    public userService() {this.conn = DBconnection.getInstance().getConn();}

    @Override
    public void add(user user) {
        String SQL = "INSERT INTO user(cin, nom, prenom, email, username, password, role, sexe, adresse, numero) VALUES ('"
                + user.getCin() + "','"
                + user.getNom() + "','"
                + user.getPrenom() + "','"
                + user.getEmail() + "','"
                + user.getUsername() + "','"
                + user.getPassword() + "','"
                + user.getRole() + "','"
                + user.getSexe() + "','"
                + user.getAdresse() + "','"
                + user.getNumero() + "')";

        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(SQL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(user user) {
        String SQL = "UPDATE `user` SET `cin`=?,`nom`=?,`prenom`=?,`email`=?,`username`=?,`password`=?,`role`=?,`sexe`=?,`adresse`=?,`numero`=? WHERE id =  ?";
        try{
            PreparedStatement pstmt2 = conn.prepareStatement(SQL);
            pstmt2.setInt(1, user.getCin());
            pstmt2.setString(2, user.getNom());
            pstmt2.setString(3, user.getPrenom());
            pstmt2.setString(4, user.getEmail());
            pstmt2.setString(5, user.getUsername());
            pstmt2.setString(6, user.getPassword());
            pstmt2.setString(7, user.getRole());
            pstmt2.setString(8, user.getSexe());
            pstmt2.setString(9, user.getAdresse());
            pstmt2.setInt(10, user.getNumero());
            pstmt2.setInt(11, user.getId());
            pstmt2.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(user user) {
        String SQL = "DELETE FROM `user` WHERE id = ?";
        try{
            PreparedStatement pstmt = conn.prepareStatement(SQL);
            pstmt.setInt(1, user.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<user> getAll() {
        String req = "SELECT * FROM `user`";
        ArrayList<user> tab_users = new ArrayList<>();
        Statement stm;
        try {
            stm = this.conn.createStatement();
            ResultSet rs=  stm.executeQuery(req);
            while (rs.next()){
                user u = new user();
                u.setPrenom(rs.getString("prenom"));
                u.setNom(rs.getString("nom"));
                u.setCin(rs.getInt("cin"));
                u.setEmail(rs.getString("email"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setRole(rs.getString("role"));
                u.setSexe(rs.getString("sexe"));
                u.setAdresse(rs.getString("adresse"));
                u.setNumero(rs.getInt("numero"));
                u.setId(rs.getInt("id"));

                tab_users.add(u);
            }


        } catch (SQLException ex) {

            System.out.println(ex.getMessage());

        }
        return tab_users;
    }


    //just need it like that because class userService implement interface IService wich this function i need for pormotionService
    @Override
    public List<user> getByUser_id(user u){
        List<user> tab_users = new ArrayList<>();
        return tab_users;
    }


}
