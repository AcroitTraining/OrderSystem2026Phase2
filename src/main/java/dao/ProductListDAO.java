package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.ProductListInfo;

public class ProductListDAO {
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

	/**
	 * 管理画面タブ用のカテゴリ一覧取得（削除フラグ=1のもの）
	 */
	public List<ProductListInfo> findAllCategories() throws SQLException {
		String sql = "SELECT category_id, category_name FROM category "
				+ "WHERE category_delete_flag = 1 ORDER BY category_id ASC";
		List<ProductListInfo> cList = new ArrayList<>();
		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				ProductListInfo cInfo = new ProductListInfo();
				cInfo.setCategoryId(rs.getInt("category_id"));
				cInfo.setCategoryName(rs.getString("category_name"));
				cList.add(cInfo);
			}
		}
		return cList;
	}

	/**
	 * 商品一覧の取得（p.category_id を併せて取得）
	 */
	public List<ProductListInfo> findAllProduct() throws SQLException {
		String sql = "SELECT p.product_id, p.product_name, p.category_id, c.category_name, p.product_price, "
				+ "p.product_stock, p.product_display_flag, p.product_delete_flag "
				+ "FROM product p "
				+ "LEFT JOIN category c ON p.category_id = c.category_id "
				+ "WHERE p.product_delete_flag = 1";
		List<ProductListInfo> pList = new ArrayList<>();
		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				ProductListInfo pInfo = new ProductListInfo();
				pInfo.setProductId(rs.getInt("product_id"));
				pInfo.setProductName(rs.getString("product_name"));
				pInfo.setCategoryId(rs.getInt("category_id")); // ★追加
				pInfo.setCategoryName(rs.getString("category_name"));
				pInfo.setProductPrice(rs.getInt("product_price"));
				pInfo.setProductStock(rs.getInt("product_stock"));
				pInfo.setProductDisplayFlag(rs.getInt("product_display_flag"));
				pInfo.setProductDeleteFlag(rs.getInt("product_delete_flag"));
				pList.add(pInfo);
			}
		}
		return pList;	
	}

	public void updateProductDisplayFlag(int productId) throws SQLException {
		String sql = "UPDATE product SET product_display_flag = product_display_flag ^ 1 WHERE product_id = ?";
		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, productId);
			ps.executeUpdate();
		}
	}

	public void updateProductDeleteFlag(int productId) throws SQLException {
		String sql = "UPDATE product SET product_delete_flag = 0 WHERE product_id = ?";
		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, productId);
			ps.executeUpdate();
		}
	}
}