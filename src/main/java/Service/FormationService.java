package Service;
import Model.Formation;
import Util.DBconnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FormationService implements IService<Formation>  {
    Connection conn;

    public FormationService() {
        this.conn = DBconnection.getInstance().getConn();
    }

    @Override
    public void add(Formation formation) {
        String SQL = "insert into formation (Titre, Description, Date_D, Date_F, Duree, Image, id_Formateur) values ('" +
                formation.getTitre()
                + "','" + formation.getDescription() + "','" +
                formation.getDate_D() +
                "','" + formation.getDate_F() +
                "','" + formation.getDuree() +
                "','" + formation.getImage() +
                "','" + formation.getId_Formateur() + "')";
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(SQL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void update(Formation formation) {
        String SQL = "UPDATE formation SET Titre = ?, Description = ?, Date_D = ?, Date_F = ?, Duree = ?, Image = ?, id_Formateur = ? WHERE id_form = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, formation.getTitre());
            pstmt.setString(2, formation.getDescription());
            pstmt.setDate(3, formation.getDate_D());
            pstmt.setDate(4, formation.getDate_F());
            pstmt.setInt(5, formation.getDuree());
            pstmt.setString(6, formation.getImage());
            pstmt.setInt(7, formation.getId_Formateur());
            pstmt.setInt(8, formation.getId_form());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void delete(Formation formation) {
        String SQL = "DELETE FROM formation WHERE id_form = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, formation.getId_form());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Formation> getAll() {
        String req = "SELECT * FROM `formation`";
        ArrayList<Formation> formations = new ArrayList<>();
        Statement stm;
        try {
            stm = this.conn.createStatement();
            ResultSet rs = stm.executeQuery(req);
            while (rs.next()) {
                Formation f = new Formation();
                f.setId_form(rs.getInt("id_form"));
                f.setTitre(rs.getString("Titre"));
                f.setDescription(rs.getString("Description"));
                f.setDate_D(rs.getDate("Date_D"));
                f.setDate_F(rs.getDate("Date_F"));
                f.setDuree(rs.getInt("Duree")); // Correctement liée à la durée
                f.setImage(rs.getString("Image"));
                f.setId_Formateur(rs.getInt("id_Formateur"));
                formations.add(f);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return formations;
    }
    public List<Formation> searchByTitle(String title) {
        String req = "SELECT * FROM formation WHERE Titre LIKE ?";
        List<Formation> formations = new ArrayList<>();
        try (PreparedStatement pst = conn.prepareStatement(req)) {
            pst.setString(1, "%" + title + "%");  // Requête insensible à la casse
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Formation f = new Formation();
                f.setId_form(rs.getInt("id_form"));
                f.setTitre(rs.getString("Titre"));
                f.setDescription(rs.getString("Description"));
                f.setDate_D(rs.getDate("Date_D"));
                f.setDate_F(rs.getDate("Date_F"));
                f.setDuree(rs.getInt("Duree"));
                f.setImage(rs.getString("Image"));
                f.setId_Formateur(rs.getInt("id_Formateur"));
                formations.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return formations;
    }


}
