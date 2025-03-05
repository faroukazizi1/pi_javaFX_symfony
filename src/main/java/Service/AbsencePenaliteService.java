package Service;

import Util.DBconnection;
import models.absence;
import models.penalite;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class AbsencePenaliteService {
    private Connection conn;

    public AbsencePenaliteService() {
        this.conn = DBconnection.getInstance().getConn();
    }

    // Méthode pour obtenir les informations d'absence et de pénalité par CIN
    public Map<String, Object> getAbsenceAndPenaliteInfoByCin(int cin) {
        Map<String, Object> result = new HashMap<>();
        String sql = "SELECT a.cin, a.nbr_abs, p.type AS penalite_type, p.seuil_abs " +
                "FROM absence a " +
                "LEFT JOIN penalite p ON a.cin = p.cin " +
                "WHERE a.cin = ?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, cin);
            rs = pstmt.executeQuery();

            // Vérifier si le ResultSet contient des données
            if (rs.next()) {
                result.put("cin", rs.getInt("cin"));
                result.put("nbr_abs", rs.getInt("nbr_abs"));

                // Vérifier si des informations de pénalité sont disponibles
                String penaliteType = rs.getString("penalite_type");
                if (penaliteType != null) {
                    result.put("penalite_type", penaliteType);
                    // Gestion explicite du cas où seuil_abs est NULL
                    int seuil = rs.getInt("seuil_abs");
                    if (rs.wasNull()) {
                        result.put("seuil", "Non défini");
                    } else {
                        result.put("seuil", seuil);
                    }
                } else {
                    result.put("penalite_type", "Aucune pénalité");
                    result.put("seuil", "Non défini");
                }
            } else {
                System.out.println("Aucune donnée trouvée pour le CIN " + cin);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des informations : " + e.getMessage());
            result.put("error", "Erreur de base de données");
        } finally {
            // Assurez-vous de fermer manuellement les ressources dans le bloc finally
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                System.out.println("Erreur lors de la fermeture des ressources : " + e.getMessage());
            }
        }

        return result;  // Retourne une map vide si aucune donnée n'a été trouvée
    }

    // Méthode pour détecter les fraudes d'absences
    public String detectFraudulentAbsences(int cinRecherche) throws SQLException {
        StringBuilder fraudMessages = new StringBuilder();
        String query = "SELECT Date FROM absence WHERE cin = ? AND Date >= CURDATE() - INTERVAL 6 MONTH";

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, cinRecherche);  // Filtrer par CIN
            rs = stmt.executeQuery();

            while (rs.next()) {
                Date dateAbsence = rs.getDate("Date");

                if (dateAbsence != null) {
                    LocalDate localDate = dateAbsence.toLocalDate();
                    DayOfWeek dayOfWeek = localDate.getDayOfWeek();

                    if (dayOfWeek == DayOfWeek.MONDAY || dayOfWeek == DayOfWeek.FRIDAY) {
                        fraudMessages.append(String.format(
                                " ⚠ Une possibilité de fraude a été détectée pour CIN: %d (Absence : %s, Date: %s) \n",
                                cinRecherche, dayOfWeek, localDate));
                    }
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de la détection des fraudes", e);
        } finally {
            // Assurez-vous de fermer manuellement les ressources dans le bloc finally
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                System.out.println("Erreur lors de la fermeture des ressources : " + e.getMessage());
            }
        }

        return fraudMessages.toString();
    }
}