package Service;

import Model.Formateur;
import Util.DBconnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FormateurService implements IService<Formateur> {
    private final Connection conn = DBconnection.getInstance().getConn();

    @Override
    public void add(Formateur formateur) {
        String SQL = "INSERT INTO formateur (Numero, Nom_F, Prenom_F, Email, Specialite) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, formateur.getNumero());
            pstmt.setString(2, formateur.getNom_F());
            pstmt.setString(3, formateur.getPrenom_F());
            pstmt.setString(4, formateur.getEmail());
            pstmt.setString(5, formateur.getSpecialite());

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Formateur ajouté avec succès !");
            } else {
                System.out.println("L'ajout a échoué.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Formateur formateur) {
        String SQL = "UPDATE formateur SET Numero = ?, Nom_F = ?, Prenom_F = ?, Email = ?, Specialite = ? WHERE id_Formateur = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, formateur.getNumero());
            pstmt.setString(2, formateur.getNom_F());
            pstmt.setString(3, formateur.getPrenom_F());
            pstmt.setString(4, formateur.getEmail());
            pstmt.setString(5, formateur.getSpecialite());
            pstmt.setInt(6, formateur.getId_Formateur());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Formateur formateur) {
        String SQL = "DELETE FROM formateur WHERE id_Formateur = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, formateur.getId_Formateur());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Formateur> getAll() {
        String req = "SELECT * FROM formateur";
        List<Formateur> formateurs = new ArrayList<>();

        try (Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery(req)) {
            while (rs.next()) {
                Formateur fo = new Formateur();
                fo.setId_Formateur(rs.getInt("id_Formateur"));
                fo.setNumero(rs.getInt("Numero"));
                fo.setNom_F(rs.getString("Nom_F"));
                fo.setPrenom_F(rs.getString("Prenom_F"));
                fo.setEmail(rs.getString("Email"));
                fo.setSpecialite(rs.getString("Specialite"));

                formateurs.add(fo);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return formateurs;
    }
}
