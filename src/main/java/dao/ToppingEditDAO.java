package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.ToppingEditInfo;

public class ToppingEditDAO {
    private final String JDBC_URL = "jdbc:mysql://localhost:3306/order_management";
    private final String DB_USER = "order";
    private final String DB_PASS = "1234";

    private Connection connection;

    public ToppingEditDAO() throws SQLException {
        this.connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
    }

    public ToppingEditDAO(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return this.connection;
    }

    // トッピング詳細の取得
    public ToppingEditInfo findToppingDetails(int toppingId) throws SQLException {
        ToppingEditInfo info = null;
        String sql = "SELECT topping_name, topping_price FROM topping WHERE topping_id = ?";
        try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
            ps.setInt(1, toppingId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    info = new ToppingEditInfo();
                    info.setToppingId(toppingId);
                    info.setToppingName(rs.getString("topping_name"));
                    info.setToppingPrice(rs.getInt("topping_price"));
                }
            }
        }
        return info;
    }

    // 新規登録処理（各フラグを一律1にする）
    public void insertToppingDetails(String toppingName, int toppingPrice) throws SQLException {
        String sql = "INSERT INTO topping (topping_name, topping_price, topping_stock, topping_display_flag, topping_delete_flag) VALUES (?, ?, 1, 1, 1)";
        try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
            ps.setString(1, toppingName);
            ps.setInt(2, toppingPrice);
            ps.executeUpdate();
        }
    }

    // 更新処理
    public void updateToppingDetails(int toppingId, String toppingName, int toppingPrice) throws SQLException {
        String sql = "UPDATE topping SET topping_name = ?, topping_price = ? WHERE topping_id = ?";
        try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
            ps.setString(1, toppingName);
            ps.setInt(2, toppingPrice);
            ps.setInt(3, toppingId);
            ps.executeUpdate();
        }
    }
}