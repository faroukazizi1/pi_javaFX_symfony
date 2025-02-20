package services;


import Util.DBconnection;
import interfaces.IService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectServices implements IService<Project> {
    // Connexion à la base de données
    static Connection conn = DBconnection.getInstance().getConn();


    // ✅ Ajouter un projet (PreparedStatement)/ Protège contre les injections SQL	/ Légèrement plus lent / Plus propre et lisible*/
    @Override
    public void add(Project project) {
        String query = "INSERT INTO project (titre, description, statut, date_debut, date_fin) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, project.getTitre());
            ps.setString(2, project.getDescription());
            ps.setString(3, project.getStatut());
            ps.setDate(4, project.getDate_debut());
            ps.setDate(5, project.getDate_fin());

            ps.executeUpdate();
            System.out.println("Project added successfully!");
        } catch (SQLException e) { System.out.println(" Add Error : " + e.getMessage()); }
    }

    // ✅ Affichage de tous les projets
    @Override
    public List<Project> getAll() {
        List<Project> projects = new ArrayList<>();
        String query = "SELECT * FROM project";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Project project = new Project();
                project.setId(rs.getInt("id"));
                project.setTitre(rs.getString("titre"));
                project.setDescription(rs.getString("description"));
                project.setStatut(rs.getString("statut"));
                project.setDate_debut(rs.getDate("date_debut"));
                project.setDate_fin(rs.getDate("date_fin"));
                projects.add(project);
            }
        } catch (SQLException e) { System.out.println(" Display Error: " + e.getMessage()); }
        return projects;
    }

    public static ObservableList<Project> getAll2() {
        ObservableList<Project> projects = FXCollections.observableArrayList();
        String query = "SELECT * FROM project";

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Project project = new Project();
                project.setId(rs.getInt("id"));
                project.setTitre(rs.getString("titre"));
                project.setDescription(rs.getString("description"));
                project.setStatut(rs.getString("statut"));
                project.setDate_debut(rs.getDate("date_debut"));
                project.setDate_fin(rs.getDate("date_fin"));
                projects.add(project);
            }
        } catch (SQLException e) {
            System.out.println(" Display Error: " + e.getMessage());
        }

        return projects;
    }
    // ✅ Update un projet
    public void update(Project project) {
        String query = "UPDATE project SET titre=?, description=?, statut=?, date_debut=?, date_fin=? WHERE id=?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, project.getTitre());
            ps.setString(2, project.getDescription());
            ps.setString(3, project.getStatut());
            ps.setDate(4, project.getDate_debut());
            ps.setDate(5, project.getDate_fin());
            ps.setInt(6, project.getId());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println(" Project updated successfully!");
            } else { System.out.println("⚠ No project found with ID: " + project.getId()); }
        } catch (SQLException e) { System.out.println(" Update Error: " + e.getMessage()); }
    }

    // ✅ Supprimer un projet (Delete)
    public void delete(Project project) {
        String query = "DELETE FROM project WHERE id=?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, project.getId()); // Utilisation de l'ID du projet
            int rowsDeleted = ps.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println(" Project deleted successfully!");
            } else { System.out.println(" No project found with ID to delete: " + project.getId()); }
        } catch (SQLException e) { System.out.println(" Delete Error: " + e.getMessage()); }
    }
    public void delete2(int projectId) {
        String query = "DELETE FROM project WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            // Set the project ID passed as a parameter
            ps.setInt(1, projectId);

            // Execute the update and check how many rows were deleted
            int rowsDeleted = ps.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println(" Project deleted successfully!");
            } else {
                System.out.println(" No project found with ID: " + projectId + " to delete.");
            }
        } catch (SQLException e) {
            System.out.println(" Delete Error: " + e.getMessage());
        }
    }
    // Get a project by its ID
    public Project getProjectById(int projectId) {
        Project project = null;
        String query = "SELECT * FROM project WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, projectId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                project = new Project();
                project.setId(rs.getInt("id"));
                project.setTitre(rs.getString("titre"));
                project.setDescription(rs.getString("description"));
                project.setStatut(rs.getString("statut"));
                project.setDate_debut(rs.getDate("date_debut"));
                project.setDate_fin(rs.getDate("date_fin"));
            }
        } catch (SQLException e) {
            System.out.println(" Get Project by ID Error: " + e.getMessage());
        }

        return project;
    }

    // Get a project ID by its title (titre)
    public int getProjectIdByTitre(String titre) {
        int projectId = -1; // Default value for non-existent project
        String query = "SELECT id FROM project WHERE titre = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, titre);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                projectId = rs.getInt("id"); // Extract the project ID
            }
        } catch (SQLException e) {
            System.out.println(" Get Project ID by Titre Error: " + e.getMessage());
        }

        return projectId;
    }
}
