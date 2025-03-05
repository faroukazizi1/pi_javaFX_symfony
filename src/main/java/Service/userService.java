package Service;
import Model.promotion;
import Model.user;
import Util.DBconnection;

import javax.swing.text.html.HTMLDocument;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class userService implements IService<user> {

    Connection conn;

    public userService() {this.conn = DBconnection.getInstance().getConn();}

    @Override
    public void add(user user) {
        String SQL = "INSERT INTO user(cin, nom, prenom, email, username, password, role, sexe, adresse, numero) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, user.getCin());
            pstmt.setString(2, user.getNom());
            pstmt.setString(3, user.getPrenom());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getUsername());

            //Hash du mot de passe
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            pstmt.setString(6, hashedPassword);

            pstmt.setString(7, user.getRole());
            pstmt.setString(8, user.getSexe());
            pstmt.setString(9, user.getAdresse());
            pstmt.setInt(10, user.getNumero());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }
    @Override
    public void update(user user) {
        String SQL_SELECT = "SELECT password FROM user WHERE id = ?";
        String SQL_UPDATE = "UPDATE `user` SET `cin`=?, `nom`=?, `prenom`=?, `email`=?, `username`=?, `password`=?, `role`=?, `sexe`=?, `adresse`=?, `numero`=? WHERE id = ?";

        try {
            // 1️⃣ Récupérer l'ancien mot de passe
            PreparedStatement pstmt1 = conn.prepareStatement(SQL_SELECT);
            pstmt1.setInt(1, user.getId());
            ResultSet rs = pstmt1.executeQuery();

            String oldPassword = null;
            if (rs.next()) {
                oldPassword = rs.getString("password"); // L'ancien mot de passe haché
            }
            rs.close();
            pstmt1.close();

            // 2️⃣ Vérifier si l'utilisateur a entré un nouveau mot de passe
            String finalPassword;
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                finalPassword = oldPassword; // Garder l'ancien mot de passe haché
            } else {
                finalPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()); // Hacher le nouveau mot de passe
            }

            // 3️⃣ Mise à jour de l'utilisateur
            PreparedStatement pstmt2 = conn.prepareStatement(SQL_UPDATE);
            pstmt2.setInt(1, user.getCin());
            pstmt2.setString(2, user.getNom());
            pstmt2.setString(3, user.getPrenom());
            pstmt2.setString(4, user.getEmail());
            pstmt2.setString(5, user.getUsername());
            pstmt2.setString(6, finalPassword); // Utiliser le mot de passe final (ancien ou haché)
            pstmt2.setString(7, user.getRole());
            pstmt2.setString(8, user.getSexe());
            pstmt2.setString(9, user.getAdresse());
            pstmt2.setInt(10, user.getNumero());
            pstmt2.setInt(11, user.getId());

            int rowsUpdated = pstmt2.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Utilisateur mis à jour avec succès !");
            } else {
                System.out.println("⚠️ Aucun utilisateur mis à jour.");
            }
            pstmt2.close();

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
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
        String query = "SELECT password FROM user WHERE email = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String hashedPassword = resultSet.getString("password");

                    // 🔑 Vérification du mot de passe saisi avec le hash stocké
                    return BCrypt.checkpw(password, hashedPassword);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erreur SQL lors de l'authentification : " + ex.getMessage());
        }
        return false;
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

    public int getUserIdByEmail(String email) {
        String sql = "SELECT id FROM user WHERE email = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL: " + e.getMessage());
        }
        return -1;
    }

    public void modifyPassword(int userId, String newPassword) {
        String sql = "UPDATE user SET password = ? WHERE id = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            pstmt.setString(1, hashedPassword);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
