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

	private static final String JDBC_URL =	"jdbc:mysql://localhost:3306/order_management"
					+ "?useSSL=false&allowPublicKeyRetrieval=true";
	private static final String DB_USER = "order";
	private static final String DB_PASS = "1234";

	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(
					"JDBCドライバを読み込めませんでした", e);
		}
	}

	/** メソッド呼び出しのたびに新しい接続を取得する */
	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(
				JDBC_URL, DB_USER, DB_PASS);
	}

	/**
	 * 商品名が既に存在するか確認する。
	 *
	 * @param productName 商品名
	 * @param productId 自分自身を除外するための商品ID
	 * @return 重複している場合はtrue
	 */
	public boolean isProductNameExists(
			String productName, int productId) throws SQLException {

		String sql =
				"SELECT COUNT(*) FROM product WHERE product_name = ? "
						+ "AND product_id != ? "
						+ "AND product_delete_flag = 1";

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
	/**
	 * トッピング一覧を取得する。
	 *
	 * @return トッピング一覧
	 */
	public List<ProductEditInfo.ToppingMaster> findAllToppings()
			throws SQLException {

		String sql =
				"SELECT topping_id, topping_name "
						+ "FROM topping";

		List<ProductEditInfo.ToppingMaster> list = new ArrayList<>();

		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				ProductEditInfo.ToppingMaster tm =
						new ProductEditInfo.ToppingMaster();

				tm.setToppingId(rs.getInt("topping_id"));
				tm.setToppingName(rs.getString("topping_name"));

				list.add(tm);
			}
		}

		return list;
	}

	/**
	 * カテゴリー一覧を取得する。
	 *
	 * ※現在はToppingMasterを流用。
	 *
	 * @return カテゴリー一覧
	 */
	public List<ProductEditInfo.ToppingMaster> findAllCategories()
			throws SQLException {

		String sql =
				"SELECT category_id, category_name "
						+ "FROM category";

		List<ProductEditInfo.ToppingMaster> list = new ArrayList<>();

		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				ProductEditInfo.ToppingMaster cm =
						new ProductEditInfo.ToppingMaster();

				cm.setToppingId(rs.getInt("category_id"));
				cm.setToppingName(rs.getString("category_name"));

				list.add(cm);
			}
		}

		return list;
	}

	/**
	 * 商品情報と選択されているトッピングを取得する。
	 *
	 * @param productId 商品ID
	 * @return 商品情報
	 */
	public ProductEditInfo findProductDetails(int productId)
			throws SQLException {

		String productSql =
				"SELECT p.product_id, "
						+ "p.product_name, "
						+ "p.category_id, "
						+ "c.category_name, "
						+ "p.product_price, "
						+ "p.product_stock "
						+ "FROM product p "
						+ "LEFT JOIN category c "
						+ "ON p.category_id = c.category_id "
						+ "WHERE p.product_id = ? "
						+ "AND p.product_delete_flag = 1";

		ProductEditInfo info = null;

		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement(productSql)) {

			ps.setInt(1, productId);

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {
					info = new ProductEditInfo();

					info.setProductId(
							rs.getInt("product_id"));

					info.setProductName(
							rs.getString("product_name"));

					info.setCategoryId(
							rs.getInt("category_id"));

					info.setCategoryName(
							rs.getString("category_name"));

					info.setProductPrice(
							rs.getInt("product_price"));

					info.setProductStock(
							rs.getInt("product_stock"));
					System.out.println(
					        "取得した在庫数: "
					        + rs.getInt("product_stock"));
				}
			}
		}

		if (info != null) {

			String toppingSql =
					"SELECT topping_id "
							+ "FROM product_topping "
							+ "WHERE product_id = ?";

			List<Integer> selectedToppings = new ArrayList<>();

			try (Connection conn = getConnection();
					PreparedStatement ps =
							conn.prepareStatement(toppingSql)) {

				ps.setInt(1, productId);

				try (ResultSet rs = ps.executeQuery()) {

					while (rs.next()) {
						selectedToppings.add(
								rs.getInt("topping_id"));
					}
				}
			}

			info.setSelectedToppingIds(selectedToppings);
		}

		return info;
	}

	/**
	 * 商品を新規登録する。
	 *
	 * @param productName 商品名
	 * @param productPrice 商品価格
	 * @param categoryId カテゴリーID
	 * @param productStock 在庫数
	 * @param toppingIds トッピングID一覧
	 */
	public void insertProductDetails(
			String productName,
			int productPrice,
			int categoryId,
			int productStock,
			String[] toppingIds) throws SQLException {

		String insertProductSql =
				"INSERT INTO product "
						+ "(product_name, product_price, category_id, "
						+ "product_stock, product_display_flag, "
						+ "product_delete_flag) "
						+ "VALUES (?, ?, ?, ?, 1, 1)";

		String insertToppingSql =
				"INSERT INTO product_topping "
						+ "(product_id, topping_id) "
						+ "VALUES (?, ?)";

		try (Connection conn = getConnection()) {

			try {
				conn.setAutoCommit(false);

				int generatedProductId = 0;

				try (PreparedStatement ps =
						conn.prepareStatement(
								insertProductSql,
								PreparedStatement.RETURN_GENERATED_KEYS)) {

					ps.setString(1, productName);
					ps.setInt(2, productPrice);
					ps.setInt(3, categoryId);
					ps.setInt(4, productStock);

					ps.executeUpdate();

					try (ResultSet rs =
							ps.getGeneratedKeys()) {

						if (rs.next()) {
							generatedProductId =
									rs.getInt(1);
						}
					}
				}

				if (toppingIds != null
						&& generatedProductId > 0) {

					try (PreparedStatement ps =
							conn.prepareStatement(insertToppingSql)) {

						for (String toppingIdStr : toppingIds) {

							ps.setInt(
									1, generatedProductId);

							ps.setInt(
									2,
									Integer.parseInt(
											toppingIdStr));

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
	 * 商品を新規登録または更新する。
	 *
	 * @param productId 商品ID
	 * @param productName 商品名
	 * @param productPrice 商品価格
	 * @param categoryId カテゴリーID
	 * @param productStock 在庫数
	 * @param toppingIds トッピングID一覧
	 * @return 保存成功時はtrue
	 */
	public boolean saveProduct(
			int productId,
			String productName,
			int productPrice,
			int categoryId,
			int productStock,
			String[] toppingIds) throws SQLException {

		if (!isValid(
				productName,
				productPrice,
				categoryId,
				productStock)
				|| isProductNameExists(
						productName,
						productId)) {

			return false;
		}

		if (productId == 0) {

			insertProductDetails(
					productName,
					productPrice,
					categoryId,
					productStock,
					toppingIds);

		} else {

			updateProductDetails(
					productId,
					productName,
					productPrice,
					categoryId,
					productStock,
					toppingIds);
		}

		return true;
	}

	/**
	 * 入力値のバリデーション。
	 *
	 * @param productName 商品名
	 * @param productPrice 商品価格
	 * @param categoryId カテゴリーID
	 * @param productStock 在庫数
	 * @return 入力値が正しい場合はtrue
	 */
	private boolean isValid(
			String productName,
			int productPrice,
			int categoryId,
			int productStock) {

		return productName != null
				&& !productName.trim().isEmpty()
				&& productName.length() <= 18
				&& productPrice >= 0
				&& productPrice <= 99999
				&& categoryId > 0
				&& productStock >= 0
				&& productStock <= 99999;
	}

	/**
	 * 商品情報を更新する。
	 *
	 * @param productId 商品ID
	 * @param productName 商品名
	 * @param productPrice 商品価格
	 * @param categoryId カテゴリーID
	 * @param productStock 在庫数
	 * @param toppingIds トッピングID一覧
	 */
	public void updateProductDetails(
			int productId,
			String productName,
			int productPrice,
			int categoryId,
			int productStock,
			String[] toppingIds) throws SQLException {

		String updateProductSql =
				"UPDATE product "
						+ "SET product_name = ?, "
						+ "product_price = ?, "
						+ "category_id = ?, "
						+ "product_stock = ? "
						+ "WHERE product_id = ?";

		String deleteToppingSql =
				"DELETE FROM product_topping "
						+ "WHERE product_id = ?";

		String insertToppingSql =
				"INSERT INTO product_topping "
						+ "(product_id, topping_id) "
						+ "VALUES (?, ?)";

		try (Connection conn = getConnection()) {

			try {
				conn.setAutoCommit(false);

				try (PreparedStatement ps =
						conn.prepareStatement(
								updateProductSql)) {

					ps.setString(1, productName);
					ps.setInt(2, productPrice);
					ps.setInt(3, categoryId);
					ps.setInt(4, productStock);
					ps.setInt(5, productId);

					ps.executeUpdate();
				}

				try (PreparedStatement ps =
						conn.prepareStatement(
								deleteToppingSql)) {

					ps.setInt(1, productId);
					ps.executeUpdate();
				}

				if (toppingIds != null) {

					try (PreparedStatement ps =
							conn.prepareStatement(
									insertToppingSql)) {

						for (String toppingIdStr : toppingIds) {

							ps.setInt(
									1, productId);

							ps.setInt(
									2,
									Integer.parseInt(
											toppingIdStr));
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