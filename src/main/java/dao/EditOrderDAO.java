package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.EditOrderInfo;

public class EditOrderDAO {
    private final String JDBC_URL = "jdbc:mysql://localhost:3306/order_management";
    private final String DB_USER = "order";
    private final String DB_PASS = "1234";

    // 注文詳細と紐づくトッピング一覧をまとめて取得
    public EditOrderInfo findOrderDetails(int orderId) {
        EditOrderInfo info = null;
        String sql = 
            "SELECT od.order_id, od.product_id, p.product_name, od.product_quantity, od.session_id, od.table_number, " +
            "t.topping_id, t.topping_name, IFNULL(mt.topping_quantity, 0) AS topping_quantity " +
            "FROM order_details od " +
            "JOIN product p ON od.product_id = p.product_id " +
            "LEFT JOIN multiple_topping mt ON od.order_id = mt.order_id " +
            "LEFT JOIN topping t ON mt.topping_id = t.topping_id " +
            "WHERE od.order_id = ?";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    if (info == null) {
                        info = new EditOrderInfo();
                        info.setOrderId(rs.getInt("order_id"));
                        info.setProductId(rs.getInt("product_id"));
                        info.setProductName(rs.getString("product_name"));
                        info.setOrderQuantity(rs.getInt("product_quantity"));
                        info.setSessionId(rs.getString("session_id"));
                        info.setTableNumber(rs.getInt("table_number"));
                    }
                    
                    int toppingId = rs.getInt("topping_id");
                    if (!rs.wasNull()) {
                        EditOrderInfo.ToppingInfo t = new EditOrderInfo.ToppingInfo(
                            toppingId,
                            rs.getString("topping_name"),
                            rs.getInt("topping_quantity")
                        );
                        info.addTopping(t);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // 注文自体の数量をアップデート
    public void updateProductQuantity(int orderId, int qty) {
        String sql = "UPDATE order_details SET product_quantity = ? WHERE order_id = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 商品数量が0になった場合、または「注文取り消し」時の注文データ物理削除
    public void deleteOrder(int orderId) {
        String deleteToppings = "DELETE FROM multiple_topping WHERE order_id = ?";
        String deleteOrder = "DELETE FROM order_details WHERE order_id = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
            try (PreparedStatement ps1 = conn.prepareStatement(deleteToppings)) {
                ps1.setInt(1, orderId);
                ps1.executeUpdate();
            }
            try (PreparedStatement ps2 = conn.prepareStatement(deleteOrder)) {
                ps2.setInt(1, orderId);
                ps2.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // トッピング個別操作用
    public void insertTopping(int orderId, int toppingId, int qty) {
        String sql = "INSERT INTO multiple_topping (order_id, topping_id, topping_quantity) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, toppingId);
            ps.setInt(3, qty);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateToppingQuantity(int orderId, int toppingId, int qty) {
        String sql = "UPDATE multiple_topping SET topping_quantity = ? WHERE order_id = ? AND topping_id = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setInt(2, orderId);
            ps.setInt(3, toppingId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteTopping(int orderId, int toppingId) {
        String sql = "DELETE FROM multiple_topping WHERE order_id = ? AND topping_id = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, toppingId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}