package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.ProductListInfo;

public class ProductListDAO {
	private Connection conn;

	public ProductListDAO(Connection conn) {
		this.conn = conn;
	}

	public List<ProductListInfo> findAllProduct() throws SQLException {
		//JDBCドライバを読み込む
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e){
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}

		String sql = "SELECT product_id, product_name, category_name, product_price, "
				+ "product_stock, product_display_flag, product_delete_flag "
				+ "FROM product "
				+ "WHERE product_delete_flag = 1";

		List<ProductListInfo> pList = new ArrayList<>();

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ProductListInfo pInfo = new ProductListInfo();

					pInfo.setProductId(rs.getInt("product_id"));
					pInfo.setProductName(rs.getString("product_name"));
					pInfo.setCategoryName(rs.getString("category_name"));
					pInfo.setProductPrice(rs.getInt("product_price"));
					pInfo.setProductStock(rs.getInt("product_stock"));
					pInfo.setProductDisplayFlag(rs.getInt("product_display_flag"));
					pInfo.setProductDeleteFlag(rs.getInt("product_delete_flag"));

					// リストに追加
					pList.add(pInfo);

				}
			}catch(SQLException e){
				throw e;
			}

		}
		return pList;	
	}

	public void updateProductDisplayFlag(int productId) throws SQLException{
		//JDBCドライバを読み込む
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e){
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}
		
		String sql = "UPDATE product SET product_display_flag = product_display_flag ^ 1 WHERE product_id = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, productId);
			int rs = ps.executeUpdate();
		}catch(SQLException e) {
			throw e;
		}
	}
	
	public void updateProductDeleteFlag(int productId) throws SQLException{
		//JDBCドライバを読み込む
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e){
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}
		
		String sql = "UPDATE product SET product_delete_flag = 0 WHERE product_id = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, productId);
			int rs = ps.executeUpdate();
		}catch(SQLException e) {
			throw e;
		}
	}
}