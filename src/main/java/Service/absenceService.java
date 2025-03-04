package Service;

import Util.DBconnection;
import models.absence;
import models.penalite;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class absenceService implements IService<absence> {
    Connection conn;

    public absenceService() {
        this.conn = DBconnection.getInstance().getConn();
    }

    @Override
    public void add(absence absence) {
        String SQL;
        boolean hasImage = absence.getImagePath() != null && !absence.getImagePath().isEmpty();

        // Vérification du type d'absence
        System.out.println("Ajout de l'absence - Type: " + absence.getType() + ", Image: " + (hasImage ? "Oui" : "Non"));

        // Choisir la requête SQL en fonction de la présence d'une image
        if (hasImage) {
            SQL = "INSERT INTO absence (date, nbr_abs, type, cin, image_path) VALUES (?, ?, ?, ?, ?)";
        } else {
            SQL = "INSERT INTO absence (date, nbr_abs, type, cin) VALUES (?, ?, ?, ?)";
        }

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setDate(1, new java.sql.Date(absence.getDate().getTime())); // Date
            pstmt.setInt(2, absence.getNbr_abs()); // Nombre d'absences
            pstmt.setString(3, absence.getType()); // Type
            pstmt.setInt(4, absence.getCin()); // CIN

            if (hasImage) {
                pstmt.setString(5, absence.getImagePath()); // Ajouter le chemin de l'image
            }

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("L'ajout de l'absence a échoué, aucune ligne affectée.");
            } else {
                System.out.println("Absence ajoutée avec succès.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout : " + e.getMessage());
        }
    }



    @Override
    public void update(absence absence) {
        String SQL = "UPDATE absence SET date = ?, nbr_abs = ?, type = ?, cin = ?, image_path = ? WHERE id_abs = ?"; // Mise à jour avec CIN

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setDate(1, new java.sql.Date(absence.getDate().getTime()));  // date
            pstmt.setInt(2, absence.getNbr_abs());  // nbr_abs
            pstmt.setString(3, absence.getType());  // type
            pstmt.setInt(4, absence.getCin());  // cin

            // Mettre à jour l'image si nécessaire
            if ("Justifiée".equals(absence.getType()) && absence.getImagePath() != null && !absence.getImagePath().isEmpty()) {
                pstmt.setString(5, absence.getImagePath());  // image_path
            } else {
                pstmt.setNull(5, Types.VARCHAR);  // Pas d'image si "Non justifiée"
            }

            pstmt.setInt(6, absence.getId_abs());  // id_abs

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
        String SQL = "SELECT * FROM absence";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            while (rs.next()) {
                int id_abs = rs.getInt("id_abs");
                int cin = rs.getInt("cin");
                Date date = rs.getDate("date");
                int nbr_abs = rs.getInt("nbr_abs");
                String type = rs.getString("type");
                String imagePath = rs.getString("image_path"); // Récupération du chemin de l'image

                absence abs = new absence(id_abs, cin, date, nbr_abs, type, imagePath);
                absences.add(abs);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des absences : " + e.getMessage());
        }

        return absences;
    }

    public List<absence> getByCin(int cin) {
        String SQL = "SELECT * FROM absence WHERE cin = ?"; // Requête pour récupérer toutes les absences d'un cin
        List<absence> absences = new ArrayList<>();  // Liste pour stocker toutes les absences

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, cin);  // On remplace le '?' par le cin
            ResultSet rs = pstmt.executeQuery();

            // On ajoute toutes les absences associées à ce cin
            while (rs.next()) {
                int id_abs = rs.getInt("id_abs");
                Date date = rs.getDate("date");
                int nbr_abs = rs.getInt("nbr_abs");
                String type = rs.getString("type");
                String imagePath = rs.getString("image_path"); // Récupération du chemin de l'image
                absences.add(new absence(id_abs, cin, date, nbr_abs, type, imagePath));  // Ajout à la liste
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des absences : " + e.getMessage());
        }
        return absences;  // Retourne la liste d'absences
    }


    @Override
    public void delete(absence absence) {
        String SQL = "DELETE FROM absence WHERE cin = ?"; // Suppression par CIN

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, absence.getCin()); // Suppression par CIN

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Absence supprimée avec succès !");
            } else {
                System.out.println("Aucune absence trouvée avec ce CIN.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    // Exemple de méthode pour associer une pénalité à une absence
    public void applyPenaliteToAbsence(int absenceId, penalite penalite) {
        String sql = "UPDATE absences SET penalite_id = ? WHERE id_abs = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, penalite.getId_pen());  // ID de la pénalité
            stmt.setInt(2, absenceId);             // ID de l'absence
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
