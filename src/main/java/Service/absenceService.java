package Service;

import Model.user;
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
        String SQL = "INSERT INTO absence (id_abs, date, nbr_abs, type, cin) VALUES (?, ?, ?, ?, ?)"; // Le CIN est à la fin dans la base

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, absence.getId_abs());  // id_abs
            pstmt.setDate(2, new java.sql.Date(absence.getDate().getTime()));  // date
            pstmt.setInt(3, absence.getNbr_abs());  // nbr_abs
            pstmt.setString(4, absence.getType());  // type
            pstmt.setInt(5, absence.getCin());  // cin

            pstmt.executeUpdate();
            System.out.println("Absence ajoutée à la base de données : " + absence);
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    @Override
    public void update(absence absence) {
        String SQL = "UPDATE absence SET date = ?, nbr_abs = ?, type = ?, cin = ? WHERE id_abs = ?"; // Mise à jour avec CIN

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setDate(1, new java.sql.Date(absence.getDate().getTime()));  // date
            pstmt.setInt(2, absence.getNbr_abs());  // nbr_abs
            pstmt.setString(3, absence.getType());  // type
            pstmt.setInt(4, absence.getCin());  // cin
            pstmt.setInt(5, absence.getId_abs());  // id_abs

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

                absence abs = new absence(id_abs, cin, date, nbr_abs, type);
                absences.add(abs);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des absences : " + e.getMessage());
        }

        return absences;
    }

    @Override
    public List<absence> getPromotionsByUserId(int id) {
        return List.of();
    }

    @Override
    public boolean authenticateUser(String username, String password) {
        return false;
    }

    public absence getByCin(int cin) {
        String SQL = "SELECT * FROM absence WHERE cin = ?"; // Requête par CIN
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, cin);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id_abs = rs.getInt("id_abs");
                Date date = rs.getDate("date");
                int nbr_abs = rs.getInt("nbr_abs");
                String type = rs.getString("type");
                return new absence(id_abs, cin, date, nbr_abs, type);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'absence : " + e.getMessage());
        }
        return null;
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

    public void modifyPassword(int userId, String newPassword){

    }

    public int getUserIdByEmail(String email){
        return 0;
    }

    public user HetUser(String email){
        return null;
    }


}