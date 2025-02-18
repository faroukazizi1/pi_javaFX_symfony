package services;

import Util.DBconnection;
import interfaces.IService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    // Connexion à la base de données
    static Connection conn = DBconnection.getInstance().getConn();

    // ✅ Ajouter un utilisateur

    public void add(User user) {
        String query = "INSERT INTO user_test (nom) VALUES (?)";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, user.getNom());
            ps.executeUpdate();
            System.out.println("✅ User added successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Add Error: " + e.getMessage());
        }
    }

    // ✅ Afficher tous les utilisateurs

    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM user_test";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setNom(rs.getString("nom"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("❌ Display Error: " + e.getMessage());
        }
        return users;
    }

    // ✅ Afficher tous les utilisateurs (Retourne en ObservableList)
    public static ObservableList<User> getAll2() {
        ObservableList<User> users = FXCollections.observableArrayList();
        String query = "SELECT * FROM user_test";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setNom(rs.getString("nom"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("❌ Display Error: " + e.getMessage());
        }
        return users;
    }

    // ✅ Update un utilisateur
    public void update(User user) {
        String query = "UPDATE user_test SET nom=? WHERE id=?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, user.getNom());
            ps.setInt(2, user.getId());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ User updated successfully!");
            } else {
                System.out.println("⚠️ No user found with ID: " + user.getId());
            }
        } catch (SQLException e) {
            System.out.println("❌ Update Error: " + e.getMessage());
        }
    }

    // ✅ Supprimer un utilisateur
    public void delete(User user) {
        String query = "DELETE FROM user_test WHERE id=?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, user.getId());
            int rowsDeleted = ps.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("✅ User deleted successfully!");
            } else {
                System.out.println("⚠️ No user found with ID to delete: " + user.getId());
            }
        } catch (SQLException e) {
            System.out.println("❌ Delete Error: " + e.getMessage());
        }
    }

    // ✅ Supprimer un utilisateur (méthode avec ID en paramètre)
    public void delete2(int userId) {
        String query = "DELETE FROM user_test WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            int rowsDeleted = ps.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("✅ User deleted successfully!");
            } else {
                System.out.println("⚠️ No user found with ID: " + userId + " to delete.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Delete Error: " + e.getMessage());
        }
    }
    // ✅ Récupérer un utilisateur par son ID
    public User getUserById(int userId) {
        User user = null;
        String query = "SELECT * FROM user_test WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            // Si l'utilisateur est trouvé, on le crée et on remplit les données
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setNom(rs.getString("nom"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
        return user;
    }
    // ✅ Récupérer un utilisateur par son nom
    public int getUserIdByName(String name) {
        int userId = -1; // Valeur par défaut si l'utilisateur n'est pas trouvé
        String query = "SELECT id FROM user_test WHERE nom=?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            // Si l'utilisateur est trouvé, on récupère l'ID
            if (rs.next()) {
                userId = rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }

        return userId;
    }

}
