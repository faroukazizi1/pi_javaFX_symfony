package Service;

import Util.DBconnection;
import models.absence;
import models.penalite;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class absenceService implements IService<absence> {
    Connection conn;

    public absenceService() {
        this.conn = DBconnection.getInstance().getConn();
    }

    @Override
    public void add(absence absence) {
        String SQL = "INSERT INTO absence (id_abs, date, nbr_abs, type) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, absence.getId_abs());  // id_abs
            pstmt.setDate(2, new java.sql.Date(absence.getDate().getTime()));  // date
            pstmt.setInt(3, absence.getNbr_abs());  // nbr_abs
            pstmt.setString(4, absence.getType());  // type

            pstmt.executeUpdate();
            System.out.println("Absence ajoutée à la base de données : " + absence);
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    @Override
    public void update(absence absence) {
        String SQL = "UPDATE absence SET date = ?, nbr_abs = ?, type = ? WHERE id_abs = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setDate(1, new java.sql.Date(absence.getDate().getTime())); // date
            pstmt.setInt(2, absence.getNbr_abs());  // nbr_abs
            pstmt.setString(3, absence.getType());  // type
            pstmt.setInt(4, absence.getId_abs());  // id_abs

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Absence mise à jour avec succès : " + absence);
            } else {
                System.out.println("Aucune absence trouvée avec l'id : " + absence.getId_abs());
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    @Override
    public List<absence> getAll() {
        List<absence> absences = new ArrayList<>();
        String SQL = "SELECT * FROM absence";  // La requête SQL pour obtenir toutes les absences

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            // Parcourir les résultats de la requête
            while (rs.next()) {
                int id_abs = rs.getInt("id_abs");
                Date date = rs.getDate("date");
                int nbr_abs = rs.getInt("nbr_abs");
                String type = rs.getString("type");

                // Créer un objet absence avec les données récupérées et l'ajouter à la liste
                absence abs = new absence(id_abs, date, nbr_abs, type);
                absences.add(abs);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des absences : " + e.getMessage());
        }

        return absences;  // Retourner la liste des absences
    }

    @Override
    public void delete(absence absence) {
        String SQL = "DELETE FROM absence WHERE id_abs = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, absence.getId_abs());  // id_abs

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Absence supprimée avec succès !");
            } else {
                System.out.println("Aucune absence trouvée avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }
    // Exemple de méthode pour associer une pénalité à une absence
    public void applyPenaliteToAbsence(int absenceId, penalite penalite) {
        // Logique pour mettre à jour l'absence et lui associer la pénalité.
        // Cela pourrait signifier une mise à jour dans la base de données ou une modification de l'état de l'absence.

        // Si tu utilises une base de données, voici un exemple de mise à jour :
        String sql = "UPDATE absences SET penalite_id = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, penalite.getId_pen());  // ID de la pénalité
            stmt.setInt(2, absenceId);             // ID de l'absence
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



