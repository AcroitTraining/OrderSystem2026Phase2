package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.ProductEditInfo;

public class ProductEditDAO {
    // いつもの接続情報
    private final String JDBC_URL = "jdbc:mysql://localhost:3306/order_management?useSSL=false&allowPublicKeyRetrieval=true";
    private final String DB_USER = "order";
    private final String DB_PASS = "1234";

    private Connection conn;

    /**
     * コンストラクタ
     */
    public ProductEditDAO() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("JDBCドライバを読み込めませんでした", e);
        }
    }

    /**
     * 接続管理用
     */
    public Connection getConnection() {
        return this.conn;
    }

    /**
     * 1. 商品名の重複を確認するメソッド
     */
    public boolean isProductNameExists(String productName, int productId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM product WHERE product_name = ? AND product_id != ? AND product_delete_flag = 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productName);
            ps.setInt(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /**
     * 2. 全てのトッピング情報を取得するメソッド1
     */
    public List<ProductEditInfo.ToppingMaster> findAllToppings() throws SQLException {
        // 画像を基に、テーブル名を topping_master から topping へ修正
        String sql = "SELECT topping_id, topping_name FROM topping";
        List<ProductEditInfo.ToppingMaster> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ProductEditInfo.ToppingMaster tm = new ProductEditInfo.ToppingMaster();
                tm.setToppingId(rs.getInt("topping_id"));
                tm.setToppingName(rs.getString("topping_name"));
                list.add(tm);
            }
        }
        return list;
    }

    /**
     * 3. 特定の商品の詳細情報（選択中トッピングID含む）を取得するメソッド
     */
    public ProductEditInfo findProductDetails(int productId) throws SQLException {
        String productSql = "SELECT product_id, product_name, category_name, product_price "
                          + "FROM product WHERE product_id = ? AND product_delete_flag = 1";
        
        ProductEditInfo info = null;

        try (PreparedStatement ps = conn.prepareStatement(productSql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    info = new ProductEditInfo();
                    info.setProductId(rs.getInt("product_id"));
                    info.setProductName(rs.getString("product_name"));
                    info.setCategoryName(rs.getString("category_name"));
                    info.setProductPrice(rs.getInt("product_price"));
                }
            }
        }

        if (info != null) {
            String toppingSql = "SELECT topping_id FROM product_topping WHERE product_id = ?";
            List<Integer> selectedToppings = new ArrayList<>();
            
            try (PreparedStatement ps = conn.prepareStatement(toppingSql)) {
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

    /**
     * 4. 新規商品を登録するメソッド（トランザクション制御）
     */
    public void insertProductDetails(String productName, int productPrice, String categoryName, String[] toppingIds) throws SQLException {
        String insertProductSql = "INSERT INTO product (product_name, product_price, category_name, product_stock, product_display_flag, product_delete_flag) "
                                + "VALUES (?, ?, ?, 0, 1, 1)";
        String insertToppingSql = "INSERT INTO product_topping (product_id, topping_id) VALUES (?, ?)";

        try {
            conn.setAutoCommit(false);

            int generatedProductId = 0;
            try (PreparedStatement ps = conn.prepareStatement(insertProductSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, productName);
                ps.setInt(2, productPrice);
                ps.setString(3, categoryName);
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedProductId = rs.getInt(1);
                    }
                }
            }

            if (toppingIds != null && generatedProductId > 0) {
                try (PreparedStatement ps = conn.prepareStatement(insertToppingSql)) {
                    for (String toppingIdStr : toppingIds) {
                        ps.setInt(1, generatedProductId);
                        ps.setInt(2, Integer.parseInt(toppingIdStr));
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    /**
     * 5. 既存の商品情報を更新するメソッド（トランザクション制御）
     */
    public void updateProductDetails(int productId, String productName, int productPrice, String categoryName, String[] toppingIds) throws SQLException {
        String updateProductSql = "UPDATE product SET product_name = ?, product_price = ?, category_name = ? WHERE product_id = ?";
        String deleteToppingSql = "DELETE FROM product_topping WHERE product_id = ?";
        String insertToppingSql = "INSERT INTO product_topping (product_id, topping_id) VALUES (?, ?)";

        try {
            conn.setAutoCommit(false);

            // 1. 商品基本情報の更新
            try (PreparedStatement ps = conn.prepareStatement(updateProductSql)) {
                ps.setString(1, productName);
                ps.setInt(2, productPrice);
                ps.setString(3, categoryName);
                ps.setInt(4, productId);
                ps.executeUpdate();
            }

            // 2. 既存のトッピング紐づけを一旦すべて削除
            try (PreparedStatement ps = conn.prepareStatement(deleteToppingSql)) {
                ps.setInt(1, productId);
                ps.executeUpdate();
            }

            // 3. 新しく選択されたトッピングがあれば中間テーブルに再登録
            if (toppingIds != null) {
                try (PreparedStatement ps = conn.prepareStatement(insertToppingSql)) {
                    for (String toppingIdStr : toppingIds) {
                        ps.setInt(1, productId);
                        ps.setInt(2, Integer.parseInt(toppingIdStr));
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }
}