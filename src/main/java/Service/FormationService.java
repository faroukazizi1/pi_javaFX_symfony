package Service;

import Model.Formation;
import Util.DBconnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormationService implements IService<Formation> {
    private final Connection conn;

    public FormationService() {
        this.conn = DBconnection.getInstance().getConn();
    }

    @Override
    public void add(Formation formation) {
        String SQL = "INSERT INTO formation (Titre, Description, Date_D, Date_F, Duree, Image, id_Formateur) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, formation.getTitre());
            pstmt.setString(2, formation.getDescription());
            pstmt.setDate(3, formation.getDate_D());
            pstmt.setDate(4, formation.getDate_F());
            pstmt.setInt(5, formation.getDuree());
            pstmt.setString(6, formation.getImage());
            pstmt.setInt(7, formation.getId_Formateur());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la formation : " + e.getMessage());
        }
    }

    @Override
    public void update(Formation formation) {
        String SQL = "UPDATE formation SET Titre = ?, Description = ?, Date_D = ?, Date_F = ?, Duree = ?, Image = ?, id_Formateur = ? WHERE id_form = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, formation.getTitre());
            pstmt.setString(2, formation.getDescription());
            pstmt.setDate(3, formation.getDate_D());
            pstmt.setDate(4, formation.getDate_F());
            pstmt.setInt(5, formation.getDuree());
            pstmt.setString(6, formation.getImage());
            pstmt.setInt(7, formation.getId_Formateur());
            pstmt.setInt(8, formation.getId_form());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de la formation : " + e.getMessage());
        }
    }

    @Override
    public void delete(Formation formation) {
        String SQL = "DELETE FROM formation WHERE id_form = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, formation.getId_form());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la formation : " + e.getMessage());
        }
    }

    @Override
    public List<Formation> getAll() {
        String req = "SELECT * FROM formation";
        List<Formation> formations = new ArrayList<>();

        try (Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery(req)) {

            while (rs.next()) {
                formations.add(new Formation(
                        rs.getInt("id_form"),
                        rs.getString("Titre"),
                        rs.getString("Description"),
                        rs.getDate("Date_D"),
                        rs.getDate("Date_F"),
                        rs.getInt("Duree"),
                        rs.getString("Image"),
                        rs.getInt("id_Formateur")
                ));
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la récupération des formations : " + ex.getMessage());
        }
        return formations;
    }

    public List<Formation> searchByTitle(String title) {
        String req = "SELECT * FROM formation WHERE Titre LIKE ?";
        List<Formation> formations = new ArrayList<>();

        try (PreparedStatement pst = conn.prepareStatement(req)) {
            pst.setString(1, "%" + title + "%");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                formations.add(new Formation(
                        rs.getInt("id_form"),
                        rs.getString("Titre"),
                        rs.getString("Description"),
                        rs.getDate("Date_D"),
                        rs.getDate("Date_F"),
                        rs.getInt("Duree"),
                        rs.getString("Image"),
                        rs.getInt("id_Formateur")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche de formation : " + e.getMessage());
        }
        return formations;
    }

    public Map<String, Integer> getStatistiquesParMois() {
        Map<String, Integer> statistiques = new HashMap<>();
        statistiques.put("Janvier", 5);
        statistiques.put("Février", 3);
        statistiques.put("Mars", 7);
        statistiques.put("Avril", 2);
        statistiques.put("Mai", 9);
        return statistiques;
    }

    public String getEmailFormateurById(int idFormateur) {
        String email = null;
        String sql = "SELECT email FROM formateur WHERE id_formateur = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idFormateur);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                email = rs.getString("email");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'email du formateur : " + e.getMessage());
        }
        return email;
    }

    public List<Formation> getAllFormations() {
        List<Formation> formations = new ArrayList<>();
        String query = "SELECT * FROM formation";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                formations.add(new Formation(
                        resultSet.getInt("id_form"),
                        resultSet.getString("Titre"),
                        resultSet.getString("Description"),
                        resultSet.getDate("Date_D"),
                        resultSet.getDate("Date_F"),
                        resultSet.getInt("Duree"),
                        resultSet.getString("Image"),
                        resultSet.getInt("id_Formateur")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des formations : " + e.getMessage());
        }
        return formations;
    }
}
