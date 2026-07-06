package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.EditOrderInfo;

public class EditOrderDAO {

    private final String JDBC_URL = "jdbc:mysql://localhost:3306/order_management";
    private final String DB_USER = "order";
    private final String DB_PASS = "1234";

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("JDBCドライバエラー", e);
        }
        return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
    }


    public EditOrderInfo findOrderDetails(int orderId) throws SQLException {
        EditOrderInfo info = null;
        String sql = "SELECT od.order_id, od.product_quantity, od.session_id, pd.product_id, p.product_name, p.product_price, ts.table_id "
                   + "FROM order_details od "
                   + "LEFT JOIN product_details pd ON od.order_id = pd.order_id "
                   + "LEFT JOIN product p ON pd.product_id = p.product_id "
                   + "LEFT JOIN table_sessions ts ON od.session_id = ts.session_id "
                   + "WHERE od.order_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    info = new EditOrderInfo();
                    info.setOrderId(rs.getInt("order_id"));
                    info.setProductId(rs.getInt("product_id")); 
                    info.setProductName(rs.getString("product_name"));

                    info.setProductQuantity(rs.getInt("product_quantity"));
                    info.setSessionId(String.valueOf(rs.getInt("session_id")));
                    
                    // table_sessionsから取得したtable_idをモデルへセット
                    info.setTableId(rs.getInt("table_id")); 
                }
            }
        }
        return info;
    }

    /*マスタと現在選択されているトッピング情報を結合してリスト取得
     */
    public List<EditOrderInfo.ToppingList> findToppingListByProductId(int productId, int orderId) {
        List<EditOrderInfo.ToppingList> list = new ArrayList<>();
        String sql = "SELECT t.topping_id, t.topping_name, t.topping_price, " 
                   + "IFNULL(mt.topping_quantity, 0) AS topping_quantity " 
                   + "FROM product_topping pt " 
                   + "JOIN topping t ON pt.topping_id = t.topping_id " 
                   + "LEFT JOIN multiple_toppings mt ON t.topping_id = mt.topping_id AND mt.order_id = ? " 
                   + "WHERE pt.product_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    EditOrderInfo.ToppingList t = new EditOrderInfo.ToppingList(
                        rs.getInt("topping_id"),
                        rs.getString("topping_name"),
                        rs.getInt("topping_quantity"),
                        rs.getInt("topping_price")
                    );
                    list.add(t);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
    商品注文数量の更新
     */
    public void updateProductQuantity(int orderId, int productQty) throws SQLException {
        String sql = "UPDATE order_details SET product_quantity = ? WHERE order_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productQty);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        }
    }

    /**
    トッピングの新規挿入
     */
    public void insertTopping(int orderId, int toppingId, int quantity) throws SQLException {
        String sql = "INSERT INTO multiple_toppings (order_id, topping_id, topping_quantity) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, toppingId);
            ps.setInt(3, quantity);
            ps.executeUpdate();
        }
    }

    /**
     トッピングの単体削除
     */
    public void deleteToppingSingle(int orderId, int toppingId) throws SQLException {
        String sql = "DELETE FROM multiple_toppings WHERE order_id = ? AND topping_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, toppingId);
            ps.executeUpdate();
        }
    }

    /*トッピングの数量更新  */
    public void updateToppingQuantity(int orderId, int toppingId, int quantity) throws SQLException {
        String sql = "UPDATE multiple_toppings SET topping_quantity = ? WHERE order_id = ? AND topping_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, orderId);
            ps.setInt(3, toppingId);
            ps.executeUpdate();
        }
    }

    /* 商品マスタの在庫（product_stock）を更新 */
    public void updateProductStock(int productId, int diff) throws SQLException {
        String sql = "UPDATE product SET product_stock = product_stock + ? WHERE product_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, diff);
            ps.setInt(2, productId);
            ps.executeUpdate();
        }
    }

    /**
     トッピングマスタの在庫（topping_stock）を更新
     */
    public void updateToppingStock(int toppingId, int diff) throws SQLException {
        String sql = "UPDATE topping SET topping_stock = topping_stock + ? WHERE topping_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, diff);
            ps.setInt(2, toppingId);
            ps.executeUpdate();
        }
    }

    public void deleteOrderComplete(int orderId) throws SQLException {
        String deleteToppings = "DELETE FROM multiple_toppings WHERE order_id = ?";
        String deleteOrderDetails = "DELETE FROM order_details WHERE order_id = ?";

        try (Connection conn = getConnection()) {
            try (PreparedStatement ps1 = conn.prepareStatement(deleteToppings)) {
                ps1.setInt(1, orderId);
                ps1.executeUpdate();
            }
            try (PreparedStatement ps2 = conn.prepareStatement(deleteOrderDetails)) {
                ps2.setInt(1, orderId);
                ps2.executeUpdate();
            }
        }
    }
}