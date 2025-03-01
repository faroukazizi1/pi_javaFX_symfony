    package Services;

    import Util.DBconnection;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import models.DemandeConge;
    import models.StatutDemande;
    import models.TypeConge;

    import java.sql.*;


    public class GestionConge {


        public static void soumettreDemandeConge(int employeId, String dateDebut, String dateFin, String typeConge) {
            String query = "INSERT INTO DemandeConge (employe_id, date_debut, date_fin, type_conge) VALUES (?, ?, ?, ?)";
            Connection conn = null;

            try {
                conn = DBconnection.getInstance().getConn();
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setInt(1, employeId);
                    stmt.setString(2, dateDebut);
                    stmt.setString(3, dateFin);
                    stmt.setString(4, typeConge);
                    stmt.executeUpdate();
                    System.out.println("Demande de congé soumise avec succès.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (conn != null && !conn.isClosed()) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        public static boolean ajouterConge(TypeConge type, String dateDebut, String dateFin, int employeId) {
            String query = "INSERT INTO DemandeConge (employe_id, date_debut, date_fin, type_conge) " +
                    "VALUES (?, ?, ?, ?)";

            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/gestion_conges", "root", "");
                 PreparedStatement stmt = connection.prepareStatement(query)) {

                stmt.setInt(1, employeId);
                stmt.setString(2, dateDebut);
                stmt.setString(3, dateFin);
                stmt.setString(4, type.name());

                int rowsAffected = stmt.executeUpdate();

                return rowsAffected > 0;

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }


        public static void traiterDemandeConge(Connection conn, int demandeId, StatutDemande nouveauStatut) {
            String query = "UPDATE DemandeConge SET statut = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, nouveauStatut.name());
                stmt.setInt(2, demandeId);
                stmt.executeUpdate();
                System.out.println("La demande a été mise à jour avec le statut : " + nouveauStatut);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }



        public boolean updateDemandeConge(Connection conn, int id, String debut, String fin, TypeConge type) {
            String query = "UPDATE demandeConge SET date_debut = ?, date_fin = ?, type_conge = ? WHERE id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, debut);
                stmt.setString(2, fin);
                stmt.setString(3, type.name());  // Set enum as String (type_conge column expects a string)
                stmt.setInt(4, id);

                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }


        public static void supprimerDemandeConge(int id) {
            String query = "DELETE FROM DemandeConge WHERE id = ?";

            try (Connection conn = DBconnection.getInstance().getConn();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, id);
                int rowsDeleted = stmt.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("La demande de congé a été supprimée.");
                } else {
                    System.out.println("Aucune demande trouvée pour cet ID.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public static ObservableList<DemandeConge> getDemandesCongeList(int userId) {
            ObservableList<DemandeConge> demandes = FXCollections.observableArrayList();

            String query = "SELECT * FROM demandeConge WHERE employe_id = ?"; // Corrected table name
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/gestion_conges", "root", "");
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, userId);

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String type = rs.getString("type_conge");
                    String datedebut = rs.getString("date_debut");
                    String datefin = rs.getString("date_fin");
                    String statut = rs.getString("statut");

                    DemandeConge demandeConge = new DemandeConge(id, userId, type, datedebut, datefin, statut, statut);
                    demandes.add(demandeConge);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return demandes;
        }







    }