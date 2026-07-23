package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.ToppingEditInfo;

public class ToppingEditDAO {

    // トッピング詳細の取得
    public ToppingEditInfo findToppingDetails(
            Connection conn, int toppingId) throws SQLException {

        ToppingEditInfo info = null;

        String sql =
                "SELECT topping_name, topping_price, topping_stock "
                + "FROM topping "
                + "WHERE topping_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, toppingId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    info = new ToppingEditInfo();

                    info.setToppingId(toppingId);
                    info.setToppingName(
                            rs.getString("topping_name"));
                    info.setToppingPrice(
                            rs.getInt("topping_price"));
                    info.setToppingStock(
                            rs.getInt("topping_stock"));
                }
            }
        }

        return info;
    }


    // 新規登録処理
    public void insertToppingDetails(
            Connection conn,
            String toppingName,
            int toppingPrice,
            int toppingStock) throws SQLException {

        String sql =
                "INSERT INTO topping "
                + "(topping_name, topping_price, topping_stock, "
                + "topping_display_flag, topping_delete_flag) "
                + "VALUES (?, ?, ?, 1, 1)";

        try (PreparedStatement ps =
                conn.prepareStatement(sql)) {

            ps.setString(1, toppingName);
            ps.setInt(2, toppingPrice);
            ps.setInt(3, toppingStock);

            ps.executeUpdate();
        }
    }


    // 更新処理
    public void updateToppingDetails(
            Connection conn,
            int toppingId,
            String toppingName,
            int toppingPrice,
            int toppingStock) throws SQLException {

        String sql =
                "UPDATE topping "
                + "SET topping_name = ?, "
                + "topping_price = ?, "
                + "topping_stock = ? "
                + "WHERE topping_id = ?";

        try (PreparedStatement ps =
                conn.prepareStatement(sql)) {

            ps.setString(1, toppingName);
            ps.setInt(2, toppingPrice);
            ps.setInt(3, toppingStock);
            ps.setInt(4, toppingId);

            ps.executeUpdate();
        }
    }
}