package Services;

import Util.DBconnection;
import models.BulletinPaie;
import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

public class BulletinPaieService {
    private static Connection conn = DBconnection.getInstance().getConn();

    public static int createBulletinPaie(BulletinPaie bulletin) {
        String query = "INSERT INTO BulletinPaie (employe_id, mois, annee, salaire_brut, deductions, salaire_net) VALUES (?, ?, ?, ?, ?, ?)";
        int generatedId = -1;
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, bulletin.getEmployeId());
            stmt.setString(2, bulletin.getMois());
            stmt.setInt(3, bulletin.getAnnee());
            stmt.setBigDecimal(4, bulletin.getSalaireBrut());
            stmt.setBigDecimal(5, bulletin.getDeductions());
            stmt.setBigDecimal(6, bulletin.getSalaireNet());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                generatedId = rs.getInt(1);  // Get generated ID


                String email = getEmailByUserId(bulletin.getEmployeId());

                if (email != null) {
                    String subject = "Votre bulletin de paie - " + bulletin.getMois() + " " + bulletin.getAnnee();
                    String body = "Bonjour,\n\nVotre bulletin de paie a été généré.\n\n" +
                            "Mois: " + bulletin.getMois() + "\n" +
                            "Année: " + bulletin.getAnnee() + "\n" +
                            "Salaire Brut: " + bulletin.getSalaireBrut() + "\n" +
                            "Déductions: " + bulletin.getDeductions() + "\n" +
                            "Salaire Net: " + bulletin.getSalaireNet() + "\n\n" +
                            "Cordialement,\nL'équipe RH";

                    EmailService.sendEmail(email, subject, body);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedId;
    }


    public static BulletinPaie getBulletinPaieById(int id) {
        String query = "SELECT * FROM BulletinPaie WHERE id = ?";
        BulletinPaie bulletin = null;

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                bulletin = new BulletinPaie();
                bulletin.setId(rs.getInt("id"));
                bulletin.setEmployeId(rs.getInt("employe_id"));
                bulletin.setMois(rs.getString("mois"));
                bulletin.setAnnee(rs.getInt("annee"));
                bulletin.setSalaireBrut(rs.getBigDecimal("salaire_brut"));
                bulletin.setDeductions(rs.getBigDecimal("deductions"));
                bulletin.setSalaireNet(rs.getBigDecimal("salaire_net"));
                bulletin.setDateGeneration(rs.getTimestamp("date_generation").toLocalDateTime());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bulletin;
    }

    public static List<BulletinPaie> getBulletinsPaieByMonthAndYear(int month, int year) {
        String query = "SELECT * FROM bulletinpaie WHERE mois = ? AND annee = ?";
        List<BulletinPaie> bulletins = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, month);
            stmt.setInt(2, year);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                BulletinPaie bulletin = new BulletinPaie();
                bulletin.setId(rs.getInt("id"));
                bulletin.setEmployeId(rs.getInt("employe_id"));
                bulletin.setMois(rs.getString("mois"));
                bulletin.setAnnee(rs.getInt("annee"));
                bulletin.setSalaireBrut(rs.getBigDecimal("salaire_brut"));
                bulletin.setDeductions(rs.getBigDecimal("deductions"));
                bulletin.setSalaireNet(rs.getBigDecimal("salaire_net"));
                bulletin.setDateGeneration(rs.getTimestamp("date_generation").toLocalDateTime());
                bulletins.add(bulletin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bulletins;
    }

    public static void updateBulletinPaie(BulletinPaie bulletin) {
        String query = "UPDATE BulletinPaie SET mois = ?, annee = ?, salaire_brut = ?, deductions = ?, salaire_net = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, bulletin.getMois());
            stmt.setInt(2, bulletin.getAnnee());
            stmt.setBigDecimal(3, bulletin.getSalaireBrut());
            stmt.setBigDecimal(4, bulletin.getDeductions());
            stmt.setBigDecimal(5, bulletin.getSalaireNet());
            stmt.setInt(6, bulletin.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static BigDecimal calculerSalaireNet(BigDecimal salaireBrut, BigDecimal deductions) {
        Map<BigDecimal, BigDecimal> tranches = new LinkedHashMap<>();
        tranches.put(new BigDecimal("416"), new BigDecimal("0.00"));
        tranches.put(new BigDecimal("833"), new BigDecimal("0.15"));
        tranches.put(new BigDecimal("1666"), new BigDecimal("0.25"));
        tranches.put(new BigDecimal("2500"), new BigDecimal("0.30"));
        tranches.put(new BigDecimal("3333"), new BigDecimal("0.33"));
        tranches.put(new BigDecimal("4166"), new BigDecimal("0.36"));
        tranches.put(new BigDecimal("5833"), new BigDecimal("0.38"));
        tranches.put(new BigDecimal("8333"), new BigDecimal("0.40"));

        BigDecimal impots = BigDecimal.ZERO;
        BigDecimal dernierSeuil = BigDecimal.ZERO;

        for (Map.Entry<BigDecimal, BigDecimal> tranche : tranches.entrySet()) {
            BigDecimal plafond = tranche.getKey();
            BigDecimal taux = tranche.getValue();

            if (salaireBrut.compareTo(dernierSeuil) > 0) {
                BigDecimal montantImposable = salaireBrut.min(plafond).subtract(dernierSeuil);
                impots = impots.add(montantImposable.multiply(taux));
                dernierSeuil = plafond;
            }
        }
        BigDecimal prime = salaireBrut.compareTo(new BigDecimal("40000")) > 0 ? new BigDecimal("500") : BigDecimal.ZERO;
        BigDecimal salaireNet = salaireBrut.subtract(impots).subtract(deductions).add(prime);
        return salaireNet.compareTo(BigDecimal.ZERO) > 0 ? salaireNet : BigDecimal.ZERO;
    }
    public static String getEmailByUserId(int userId) {
        String query = "SELECT email FROM user WHERE id = ?";
        String email = null;

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                email = rs.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return email;
    }

}
