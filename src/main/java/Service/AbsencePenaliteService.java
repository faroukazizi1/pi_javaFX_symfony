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

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cin);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                result.put("cin", rs.getInt("cin"));
                result.put("nbr_abs", rs.getInt("nbr_abs"));

                // Vérifier si des informations de pénalité sont disponibles
                String penaliteType = rs.getString("penalite_type");
                if (penaliteType != null) {
                    result.put("penalite_type", penaliteType);
                    // Gestion explicite du cas où seuil_abs est NULL
                    int seuil = rs.getInt("seuil_abs");
                    if (rs.wasNull()) {  // Si seuil_abs est NULL
                        result.put("seuil", "Non défini");
                    } else {
                        result.put("seuil", seuil);
                    }
                } else {
                    result.put("penalite_type", "Aucune pénalité");
                    result.put("seuil", "Non défini");  // ou 0, selon ta logique
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des informations : " + e.getMessage());
        }

        return result;  // Retourne une map vide si aucune donnée n'a été trouvée
    }

    public String detectFraudulentAbsences(int cinRecherche) throws SQLException {
        StringBuilder fraudMessages = new StringBuilder();
        String query = "SELECT Date FROM absence WHERE cin = ? AND Date >= CURDATE() - INTERVAL 6 MONTH";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, cinRecherche);  // Filtrer par CIN
            try (ResultSet rs = stmt.executeQuery()) {
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
            }
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de la détection des fraudes", e);
        }

        return fraudMessages.toString();
    }





}
