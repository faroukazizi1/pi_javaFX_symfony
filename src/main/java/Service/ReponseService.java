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
        reponse.setMontantAutorise(montantAutorise);
        reponse.setAssurance(assurance);
    }

    @Override
    public void add(Reponse reponse) {
        calculerValeursAutomatiques(reponse);
        String SQL = "INSERT INTO reponse (date_reponse, montant_demande, revenus_bruts, taux_interet, mensualite_credit, duree_remboursement, montant_autorise, assurance) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setDate(1, reponse.getDateReponse());
            pstmt.setDouble(2, reponse.getMontantDemande());
            pstmt.setDouble(3, reponse.getRevenusBruts());
            pstmt.setDouble(4, reponse.getTauxInteret());
            pstmt.setDouble(5, reponse.getMensualiteCredit());
            pstmt.setInt(6, reponse.getDureeRemboursement());
            pstmt.setDouble(7, reponse.getMontantAutorise());
            pstmt.setDouble(8, reponse.getAssurance());

            pstmt.executeUpdate();
            System.out.println("Réponse ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    @Override
    public void update(Reponse reponse) {
        calculerValeursAutomatiques(reponse);
        String SQL = "UPDATE reponse SET date_reponse=?, montant_demande=?, revenus_bruts=?, taux_interet=?, mensualite_credit=?, duree_remboursement=?, montant_autorise=?, assurance=? WHERE id_reponse=?";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setDate(1, reponse.getDateReponse());
            pstmt.setDouble(2, reponse.getMontantDemande());
            pstmt.setDouble(3, reponse.getRevenusBruts());
            pstmt.setDouble(4, reponse.getTauxInteret());
            pstmt.setDouble(5, reponse.getMensualiteCredit());
            pstmt.setInt(6, reponse.getDureeRemboursement());
            pstmt.setDouble(7, reponse.getMontantAutorise());
            pstmt.setDouble(8, reponse.getAssurance());
            pstmt.setInt(9, reponse.getIdReponse()); // Supposons que l'objet Reponse contienne l'ID.

            pstmt.executeUpdate();
            System.out.println("Réponse mise à jour avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour : " + e.getMessage());
        }
    }


    @Override
    public void delete(Reponse reponse) {
        String SQL = "DELETE FROM reponse WHERE id_reponse=?";
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, reponse.getIdReponse());
            pstmt.executeUpdate();
            System.out.println("Réponse supprimée !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression : " + e.getMessage());
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
                reponse.setDateReponse(rs.getDate("date_reponse"));
                reponse.setMontantDemande(rs.getDouble("montant_demande"));
                reponse.setRevenusBruts(rs.getDouble("revenus_bruts"));
                reponse.setTauxInteret(rs.getDouble("taux_interet"));
                reponse.setMensualiteCredit(rs.getDouble("mensualite_credit"));
                reponse.setDureeRemboursement(rs.getInt("duree_remboursement"));
                reponse.setMontantAutorise(rs.getDouble("montant_autorise"));
                reponse.setAssurance(rs.getDouble("assurance"));

                reponses.add(reponse);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des réponses : " + e.getMessage());
        }

        return reponses;
    }
    public boolean saveReponse(Reponse reponse) {
        String query = "INSERT INTO reponse (date_reponse, montant_demande, revenus_bruts, taux_interet, mensualite_credit, potentiel_credit, duree_remboursement, montant_autorise, assurance) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setDate(1, java.sql.Date.valueOf(java.time.LocalDate.now())); // Assuming dateReponse is of type LocalDate
            preparedStatement.setDouble(2, reponse.getMontantDemande());
            preparedStatement.setDouble(3, reponse.getRevenusBruts());
            preparedStatement.setDouble(4, reponse.getTauxInteret());
            preparedStatement.setDouble(5, reponse.getMensualiteCredit());
            preparedStatement.setDouble(6, reponse.getPotentielCredit()); // Now included
            preparedStatement.setInt(7, reponse.getDureeRemboursement());
            preparedStatement.setDouble(8, reponse.getMontantAutorise());
            preparedStatement.setDouble(9, reponse.getAssurance());

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0; // Return true if at least one row is inserted
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Reponse> getPromotionsByUserId(int id){
        List<Reponse> reponses = new ArrayList<>();
        return reponses;
    }

    public boolean authenticateUser(String username, String password){
        return false;
    }



}
