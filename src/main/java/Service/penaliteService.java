package Service;

import Model.user;
import Util.DBconnection;
import models.penalite;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class penaliteService implements IService<penalite> {
    private Connection conn;

    public penaliteService() {
        this.conn = DBconnection.getInstance().getConn();
        try {
            if (this.conn == null || this.conn.isClosed()) {
                System.out.println("Erreur : Connexion non établie dans penaliteService !");
            } else {
                System.out.println("Connexion bien reçue dans penaliteService !");
                this.conn.setAutoCommit(true); // Activation du commit automatique
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(penalite penalite) {
        String SQL = "INSERT INTO penalite (type, seuil_abs, cin) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, penalite.getType());
            pstmt.setInt(2, penalite.getSeuil_abs());
            pstmt.setInt(3, penalite.getCin()); // Remplacer id_absence par cin

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Pénalité ajoutée avec succès : " + penalite);

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        penalite.setId_pen(generatedKeys.getInt(1));
                    }
                }
            } else {
                System.out.println("Aucune ligne insérée !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(penalite penalite) {
        if (conn == null) {
            System.out.println("Connexion non disponible !");
            return;
        }

        // Mise à jour par CIN au lieu de l'ID_PEN
        String SQL = "UPDATE penalite SET type = ?, seuil_abs = ? WHERE cin = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, penalite.getType());  // Type reste en String
            pstmt.setInt(2, penalite.getSeuil_abs()); // Seuil reste en int

            // Ici, CIN reste un int
            pstmt.setInt(3, penalite.getCin()); // CIN reste en int sans conversion

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Pénalité mise à jour avec succès pour CIN : " + penalite.getCin());
            } else {
                System.out.println("Aucune pénalité trouvée avec le CIN : " + penalite.getCin());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    @Override
    public List<penalite> getAll() {
        if (conn == null) {
            System.out.println("Connexion non disponible !");
            return new ArrayList<>();
        }
        List<penalite> penalites = new ArrayList<>();
        String SQL = "SELECT * FROM penalite";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            while (rs.next()) {
                int id_pen = rs.getInt("id_pen");
                String type = rs.getString("type");
                int seuil_abs = rs.getInt("seuil_abs");
                int cin = rs.getInt("cin"); // Utilisation du CIN au lieu de l'id_absence

                penalite pen = new penalite(id_pen, type, seuil_abs, cin);
                penalites.add(pen);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return penalites;
    }

    @Override
    public List<penalite> getPromotionsByUserId(int id) {
        return List.of();
    }

    @Override
    public boolean authenticateUser(String username, String password) {
        return false;
    }

    @Override
    public void delete(penalite penalite) {
        if (conn == null) {
            System.out.println("Connexion non disponible !");
            return;
        }

        // Utilisation du CIN de l'objet penalite
        String SQL = "DELETE FROM penalite WHERE cin = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, penalite.getCin()); // Récupérer le CIN de l'objet penalite

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Pénalité supprimée avec succès pour CIN : " + penalite.getCin());
            } else {
                System.out.println("Aucune pénalité trouvée avec le CIN : " + penalite.getCin());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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