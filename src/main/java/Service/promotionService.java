package Service;

import Model.promotion;
import Util.DBconnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class promotionService implements IService<promotion> {

    Connection conn;

    public promotionService() {this.conn = DBconnection.getInstance().getConn();}

    @Override
    public void add(promotion promotion) {
        String SQL = "INSERT INTO promotion (`type_promo`, `raison`, `poste_promo`, `date_promo`, `nouv_sal`, `avantage_supp` , `id_user` ) VALUES (?, ?, ?, ?, ?, ? , ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, promotion.getType_promo());
            pstmt.setString(2, promotion.getRaison());
            pstmt.setString(3, promotion.getPoste_promo());
            pstmt.setDate(4, promotion.getDate_prom());  // Ensure getDate_prom() returns a String
            pstmt.setDouble(5, promotion.getNouv_sal());
            pstmt.setString(6, promotion.getAvs());
            pstmt.setInt(7, promotion.getId_user());

            // Execute Query
            pstmt.executeUpdate();

            System.out.println("Promotion added successfully!");

        } catch (SQLException e) {
            System.out.println("Error adding promotion: " + e.getMessage());
        }
    }



    @Override
    public void update(promotion promotion) {
        String SQL = "UPDATE `promotion` SET `type_promo`=?,`raison`=?,`poste_promo`=?,`date_promo`=?,`nouv_sal`=?,`avantage_supp`=? WHERE id = ?";
        try{
            PreparedStatement pstmt2 = conn.prepareStatement(SQL);
            pstmt2.setString(1, promotion.getType_promo());
            pstmt2.setString(2, promotion.getRaison());
            pstmt2.setString(3, promotion.getPoste_promo());
            pstmt2.setDate(4, promotion.getDate_prom());
            pstmt2.setDouble(5, promotion.getNouv_sal());
            pstmt2.setString(6, promotion.getAvs());
            pstmt2.setInt(7, promotion.getId());
            pstmt2.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(promotion promotion) {
        String SQL = "DELETE FROM `promotion` WHERE id = ?";
        try{
            PreparedStatement pstmt = conn.prepareStatement(SQL);
            pstmt.setInt(1, promotion.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<promotion> getAll() {
        String req = "SELECT * FROM `promotion`";
        ArrayList<promotion> tab_promotion = new ArrayList<>();
        Statement stm;
        try {
            stm = this.conn.createStatement();
            ResultSet rs=  stm.executeQuery(req);
            while (rs.next()){
                promotion p = new promotion();
                p.setId(rs.getInt("id"));
                p.setType_promo(rs.getString("type_promo"));
                p.setRaison(rs.getString("raison"));
                p.setPoste_promo(rs.getString("poste_promo"));
                p.setDate_prom(rs.getDate("date_promo"));
                p.setNouv_sal(rs.getDouble("nouv_sal"));
                p.setAvs(rs.getString("avantage_supp"));
                p.setId_user(rs.getInt("id_user"));


                tab_promotion.add(p);
            }


        } catch (SQLException ex) {

            System.out.println(ex.getMessage());

        }
        return tab_promotion;
    }

}
