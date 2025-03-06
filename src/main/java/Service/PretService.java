package Service;

import Model.user;
import Util.DBconnection;
import models.Pret;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PretService implements IService<Pret> {
    private final Connection conn;

    public PretService() {
        this.conn = DBconnection.getInstance().getConn();
    }

    @Override
    public void add(Pret pret) {
        String SQL = "INSERT INTO pret (Montant_pret, Date_pret, TMM, Taux, Revenus_bruts, Age_employe, Duree_remboursement, Categorie) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setFloat(1, pret.getMontantPret());
            pstmt.setDate(2, new Date(pret.getDatePret().getTime()));
            pstmt.setFloat(3, pret.getTmm());
            pstmt.setFloat(4, pret.getTaux());
            pstmt.setFloat(5, pret.getRevenusBruts());
            pstmt.setInt(6, pret.getAgeEmploye());
            pstmt.setInt(7, pret.getDureeRemboursement());
            pstmt.setString(8, pret.getCategorie());

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        pret.setIdPret(generatedKeys.getInt(1));
                    }
                }
                System.out.println("✅ Prêt ajouté avec succès : " + pret);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout du prêt : " + e.getMessage());
        }
    }

    @Override
    public void update(Pret pret) {
        String SQL = "UPDATE pret SET Montant_pret = ?, Date_pret = ?, TMM = ?, Taux = ?, Revenus_bruts = ?, Age_employe = ?, Duree_remboursement = ?, Categorie = ? WHERE ID_pret = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setFloat(1, pret.getMontantPret());
            pstmt.setDate(2, new Date(pret.getDatePret().getTime()));
            pstmt.setFloat(3, pret.getTmm());
            pstmt.setFloat(4, pret.getTaux());
            pstmt.setFloat(5, pret.getRevenusBruts());
            pstmt.setInt(6, pret.getAgeEmploye());
            pstmt.setInt(7, pret.getDureeRemboursement());
            pstmt.setString(8, pret.getCategorie());
            pstmt.setInt(9, pret.getIdPret());

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Prêt mis à jour avec succès : " + pret);
            } else {
                System.out.println("⚠ Aucun prêt trouvé avec l'ID : " + pret.getIdPret());
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    @Override
    public void delete(Pret pret) {
        delete(pret.getIdPret());
    }

    public void delete(int idPret) {
        String SQL = "DELETE FROM pret WHERE ID_pret = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, idPret);

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✅ Prêt supprimé avec succès (ID : " + idPret + ")");
            } else {
                System.out.println("⚠ Aucun prêt trouvé avec l'ID : " + idPret);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @Override
    public List<Pret> getAll() {
        List<Pret> prets = new ArrayList<>();
        String SQL = "SELECT * FROM pret";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            while (rs.next()) {
                Pret pret = new Pret(
                        rs.getInt("ID_pret"),
                        rs.getFloat("Montant_pret"),
                        rs.getDate("Date_pret"),
                        rs.getFloat("TMM"),
                        rs.getFloat("Taux"),
                        rs.getFloat("Revenus_bruts"),
                        rs.getInt("Age_employe"),
                        rs.getInt("Duree_remboursement"),
                        rs.getString("Categorie")
                );
                prets.add(pret);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des prêts : " + e.getMessage());
        }
        return prets;
    }
    public Pret getById(int id) {
        String SQL = "SELECT * FROM pret WHERE id_pret = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Pret(
                        rs.getInt("id_pret"),
                        rs.getFloat("Montant_pret"),
                        rs.getDate("Date_pret"),
                        rs.getFloat("TMM"),
                        rs.getFloat("Taux"),
                        rs.getFloat("Revenus_bruts"),
                        rs.getInt("Age_employe"),
                        rs.getInt("Duree_remboursement"),
                        rs.getString("Categorie")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Integer> getAllPretIds() {
        List<Pret> prets = getAll(); // Assuming this method fetches all Pret objects
        return prets.stream().map(Pret::getIdPret).collect(Collectors.toList());
    }

    public List<Pret> getPromotionsByUserId(int id){
        List<Pret> prets = new ArrayList<>();
        return prets;
    }

    public boolean authenticateUser(String username, String password){
        return false;
    }

    public user HetUser(String email){
        return null;
    }

    public int getUserIdByEmail(String email){
        return 0;
    }
    public void modifyPassword(int userId, String newPassword){

    }


}
