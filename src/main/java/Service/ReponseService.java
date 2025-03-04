package Service;

import Util.DBconnection;
import models.Reponse;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReponseService implements IService<Reponse> {
    private Connection conn;

    public ReponseService() {
        this.conn = DBconnection.getInstance().getConn();
    }

    private void calculerValeursAutomatiques(Reponse reponse) {
        double montantDemande = reponse.getMontantDemande();
        int duree = reponse.getDureeRemboursement();

        double tauxInteret = 5.0;
        double tauxMensuel = tauxInteret / 12 / 100;

        double revenusBruts = montantDemande * 0.4;
        double mensualiteCredit = (tauxMensuel > 0) ?
                (montantDemande * tauxMensuel) / (1 - Math.pow(1 + tauxMensuel, -duree)) :
                montantDemande / duree;

        double potentielCredit = revenusBruts * 0.3;
        double montantAutorise = potentielCredit * 0.9;
        double assurance = montantDemande * 0.02;

        reponse.setRevenusBruts(revenusBruts);
        reponse.setTauxInteret(tauxInteret);
        reponse.setMensualiteCredit(mensualiteCredit);
        reponse.setPotentielCredit(potentielCredit);
        reponse.setMontantAutorise(montantAutorise);
        reponse.setAssurance(assurance);
    }

    @Override
    public void add(Reponse reponse) {
        saveReponse(reponse); // Delegate to saveReponse to avoid duplication
    }

    public boolean saveReponse(Reponse reponse) {
        calculerValeursAutomatiques(reponse);
        String query = "INSERT INTO reponse (id_pret, date_reponse, montant_demande, revenus_bruts, taux_interet, mensualite_credit, potentiel_credit, duree_remboursement, montant_autorise, assurance) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, reponse.getIdPret());
            preparedStatement.setDate(2, reponse.getDateReponse());
            preparedStatement.setDouble(3, reponse.getMontantDemande());
            preparedStatement.setDouble(4, reponse.getRevenusBruts());
            preparedStatement.setDouble(5, reponse.getTauxInteret());
            preparedStatement.setDouble(6, reponse.getMensualiteCredit());
            preparedStatement.setDouble(7, reponse.getPotentielCredit());
            preparedStatement.setInt(8, reponse.getDureeRemboursement());
            preparedStatement.setDouble(9, reponse.getMontantAutorise());
            preparedStatement.setDouble(10, reponse.getAssurance());

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                // Retrieve the generated ID
                try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                    if (rs.next()) {
                        reponse.setIdReponse(rs.getInt(1));
                        System.out.println("Reponse saved with ID: " + reponse.getIdReponse());
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error saving Reponse: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void update(Reponse reponse) {
        calculerValeursAutomatiques(reponse);
        String SQL = "UPDATE reponse SET id_pret=?, date_reponse=?, montant_demande=?, revenus_bruts=?, taux_interet=?, mensualite_credit=?, potentiel_credit=?, duree_remboursement=?, montant_autorise=?, assurance=? WHERE id_reponse=?";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, reponse.getIdPret());
            pstmt.setDate(2, reponse.getDateReponse());
            pstmt.setDouble(3, reponse.getMontantDemande());
            pstmt.setDouble(4, reponse.getRevenusBruts());
            pstmt.setDouble(5, reponse.getTauxInteret());
            pstmt.setDouble(6, reponse.getMensualiteCredit());
            pstmt.setDouble(7, reponse.getPotentielCredit());
            pstmt.setInt(8, reponse.getDureeRemboursement());
            pstmt.setDouble(9, reponse.getMontantAutorise());
            pstmt.setDouble(10, reponse.getAssurance());
            pstmt.setInt(11, reponse.getIdReponse());

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Réponse mise à jour avec succès ! ID: " + reponse.getIdReponse());
            } else {
                System.out.println("Aucune réponse trouvée avec l'ID: " + reponse.getIdReponse());
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Reponse reponse) {
        String SQL = "DELETE FROM reponse WHERE id_reponse=?";
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, reponse.getIdReponse());
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Réponse supprimée ! ID: " + reponse.getIdReponse());
            } else {
                System.out.println("Aucune réponse trouvée avec l'ID: " + reponse.getIdReponse());
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Reponse> getAll() {
        List<Reponse> reponses = new ArrayList<>();
        String SQL = "SELECT * FROM reponse";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            while (rs.next()) {
                Reponse reponse = new Reponse();
                reponse.setIdReponse(rs.getInt("id_reponse"));
                reponse.setIdPret(rs.getInt("id_pret"));
                reponse.setDateReponse(rs.getDate("date_reponse"));
                reponse.setMontantDemande(rs.getDouble("montant_demande"));
                reponse.setRevenusBruts(rs.getDouble("revenus_bruts"));
                reponse.setTauxInteret(rs.getDouble("taux_interet"));
                reponse.setMensualiteCredit(rs.getDouble("mensualite_credit"));
                reponse.setPotentielCredit(rs.getDouble("potentiel_credit"));
                reponse.setDureeRemboursement(rs.getInt("duree_remboursement"));
                reponse.setMontantAutorise(rs.getDouble("montant_autorise"));
                reponse.setAssurance(rs.getDouble("assurance"));

                reponses.add(reponse);
            }
            System.out.println("Retrieved Reponses: " + reponses);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des réponses : " + e.getMessage());
            e.printStackTrace();
        }

        return reponses;
    }
}