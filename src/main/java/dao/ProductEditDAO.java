package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.ProductEditInfo;

public class ProductEditDAO {
    private final String JDBC_URL = "jdbc:mysql://localhost:3306/order_management";
    private final String DB_USER = "order";
    private final String DB_PASS = "1234";

    private Connection connection;
    public ProductEditDAO() throws SQLException {
        this.connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
    }

    public ProductEditDAO(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return this.connection;
    }

    // 商品詳細と選択中のトッピングIDを取得（変更用）
    public ProductEditInfo findProductDetails(int productId) throws SQLException {
        ProductEditInfo info = null;
        
        String productSql = "SELECT product_name, product_price, category_name FROM product WHERE product_id = ?";
        try (PreparedStatement ps = this.connection.prepareStatement(productSql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    info = new ProductEditInfo();
                    info.setProductId(productId);
                    info.setProductName(rs.getString("product_name"));
                    info.setProductPrice(rs.getInt("product_price"));
                    info.setCategoryName(rs.getString("category_name"));
                }
            }
        }

        if (info != null) {
            List<Integer> selectedToppings = new ArrayList<>();
            String toppingSql = "SELECT topping_id FROM product_topping WHERE product_id = ?";
            try (PreparedStatement ps = this.connection.prepareStatement(toppingSql)) {
                ps.setInt(1, productId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        selectedToppings.add(rs.getInt("topping_id"));
                    }
                }
            }
            info.setSelectedToppingIds(selectedToppings);
        }
        
        return info;
    }

    // 全トッピングマスタのリストを取得（チェックボックス一覧用）
    public List<ProductEditInfo.ToppingMaster> findAllToppings() throws SQLException {
        List<ProductEditInfo.ToppingMaster> list = new ArrayList<>();
        String sql = "SELECT topping_id, topping_name FROM topping";
        
        try (PreparedStatement ps = this.connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new ProductEditInfo.ToppingMaster(
                    rs.getInt("topping_id"),
                    rs.getString("topping_name")
                ));
            }
        }
        return list;
    }

    // 商品の新規登録処理
    public void insertProductDetails(String productName, int productPrice, String categoryName, String[] toppingIds) throws SQLException {
        // ★修正箇所: product_delete_flag の値を「0」に修正しました
        String insertProductSql = "INSERT INTO product (product_name, product_price,"
        		+ " category_name, product_stock, product_display_flag, product_delete_flag)"
        		+ " VALUES (?, ?, ?, 1, 1, 1)";
        
        int newProductId = 0;
        
        try (PreparedStatement ps = this.connection.prepareStatement(insertProductSql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, productName);
            ps.setInt(2, productPrice);
            ps.setString(3, categoryName);
            ps.executeUpdate();
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newProductId = generatedKeys.getInt(1);
                }
            }
        }

        // 新しいproduct_idに対して、選択されたトッピングを登録
        if (newProductId > 0 && toppingIds != null && toppingIds.length > 0) {
            String insertToppingSql = "INSERT INTO product_topping (product_id, topping_id) VALUES (?, ?)";
            try (PreparedStatement ps = this.connection.prepareStatement(insertToppingSql)) {
                for (String tIdStr : toppingIds) {
                    ps.setInt(1, newProductId);
                    ps.setInt(2, Integer.parseInt(tIdStr));
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        }
    }

    // 既存商品の更新処理
    public void updateProductDetails(int productId, String productName, int productPrice, String categoryName, String[] toppingIds) throws SQLException {
        String updateProductSql = "UPDATE product SET product_name = ?, product_price = ?, category_name = ? WHERE product_id = ?";
        try (PreparedStatement ps = this.connection.prepareStatement(updateProductSql)) {
            ps.setString(1, productName);
            ps.setInt(2, productPrice);
            ps.setString(3, categoryName);
            ps.setInt(4, productId);
            ps.executeUpdate();
        }

        String deleteToppingsSql = "DELETE FROM product_topping WHERE product_id = ?";
        try (PreparedStatement ps = this.connection.prepareStatement(deleteToppingsSql)) {
            ps.setInt(1, productId);
            ps.executeUpdate();
        }

        if (toppingIds != null && toppingIds.length > 0) {
            String insertToppingSql = "INSERT INTO product_topping (product_id, topping_id) VALUES (?, ?)";
            try (PreparedStatement ps = this.connection.prepareStatement(insertToppingSql)) {
                for (String tIdStr : toppingIds) {
                    ps.setInt(1, productId);
                    ps.setInt(2, Integer.parseInt(tIdStr));
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        }
    }
}