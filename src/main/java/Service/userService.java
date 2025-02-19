package Service;
import Model.promotion;
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

    public boolean authenticateUser(String username, String password) {
        String query = "SELECT * FROM user WHERE email = ? AND password = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // If a row is found, the user is authenticated
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            return false;
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            return false;
        }
    }

    public user HetUser(String email) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        user u = null; // Initialisation de la variable c

        try {
            String query = "SELECT * FROM user WHERE email = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                u = new user( rs.getInt("id"),rs.getInt("cin"),rs.getString("nom"),rs.getString("email"),rs.getString("prenom"), rs.getString("username"), rs.getString("password"), rs.getString("role"), rs.getString("sexe"), rs.getString("adresse"), rs.getInt("numero"));

            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return u;


    }

    public List<user> getPromotionsByUserId(int userId) {
        List<user> promotions = new ArrayList<>();
        return promotions;

    }




}
