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
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/order_management?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "order";
    private static final String DB_PASS = "1234";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("JDBCドライバを読み込めませんでした", e);
        }
    }

    /** メソッド呼び出しのたびに新しい接続を取得する */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
    }

    public boolean isProductNameExists(String productName, int productId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM product WHERE product_name = ? AND product_id != ? AND product_delete_flag = 1";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
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

    public List<ProductEditInfo.ToppingMaster> findAllToppings() throws SQLException {
        String sql = "SELECT topping_id, topping_name FROM topping";
        List<ProductEditInfo.ToppingMaster> list = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
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

    public ProductEditInfo findProductDetails(int productId) throws SQLException {
        String productSql = "SELECT product_id, product_name, category_name, product_price "
                          + "FROM product WHERE product_id = ? AND product_delete_flag = 1";

        ProductEditInfo info = null;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(productSql)) {
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

            try (Connection conn = getConnection();
                 PreparedStatement ps = conn.prepareStatement(toppingSql)) {
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

    public void insertProductDetails(String productName, int productPrice, String categoryName, String[] toppingIds) throws SQLException {
        String insertProductSql = "INSERT INTO product (product_name, product_price, category_name, product_stock, product_display_flag, product_delete_flag) "
                                + "VALUES (?, ?, ?, 0, 1, 1)";
        String insertToppingSql = "INSERT INTO product_topping (product_id, topping_id) VALUES (?, ?)";

        try (Connection conn = getConnection()) {
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
            }
        }
    }
    /**
     * バリデーション＋重複チェック＋新規/更新の判定をまとめて行う保存メソッド。
     * 成功時はtrue、入力不正時はfalseを返す。
     */
    public boolean saveProduct(int productId, String productName, int productPrice,
                                String categoryName, String[] toppingIds) throws SQLException {

        if (!isValid(productName, productPrice, categoryName)
                || isProductNameExists(productName, productId)) {
            return false;
        }

        if (productId == 0) {
            insertProductDetails(productName, productPrice, categoryName, toppingIds);
        } else {
            updateProductDetails(productId, productName, productPrice, categoryName, toppingIds);
        }
        return true;
    }

    private boolean isValid(String productName, int productPrice, String categoryName) {
        return productName != null && !productName.trim().isEmpty() && productName.length() <= 18
                && productPrice >= 0 && productPrice <= 99999
                && categoryName != null && !categoryName.isEmpty();
    }
    public void updateProductDetails(int productId, String productName, int productPrice, String categoryName, String[] toppingIds) throws SQLException {
        String updateProductSql = "UPDATE product SET product_name = ?, product_price = ?, category_name = ? WHERE product_id = ?";
        String deleteToppingSql = "DELETE FROM product_topping WHERE product_id = ?";
        String insertToppingSql = "INSERT INTO product_topping (product_id, topping_id) VALUES (?, ?)";

        try (Connection conn = getConnection()) {
            try {
                conn.setAutoCommit(false);

                try (PreparedStatement ps = conn.prepareStatement(updateProductSql)) {
                    ps.setString(1, productName);
                    ps.setInt(2, productPrice);
                    ps.setString(3, categoryName);
                    ps.setInt(4, productId);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = conn.prepareStatement(deleteToppingSql)) {
                    ps.setInt(1, productId);
                    ps.executeUpdate();
                }

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
            }
        }
    }
}