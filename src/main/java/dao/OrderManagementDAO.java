package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import model.OrderManagementInfo;

public class OrderManagementDAO {
private Connection conn;

public OrderManagementDAO(Connection conn) {
	this.conn = conn;
}

public List<OrderManagementInfo> findorderDetails() throws SQLException {

	Map<Integer, OrderManagementInfo> map = new LinkedHashMap<>();
	//JDBCドライバを読み込む
	try {
		Class.forName("com.mysql.cj.jdbc.Driver");
	}catch(ClassNotFoundException e){
		throw new IllegalStateException("JDBCドライバを読み込めませんでしたあ♡");
	}

		String sql = 
                "SELECT od.order_id, od.product_quantity, od.session_id, od.order_flag, "
                + "p.product_name, p.product_price, od.order_price, p.category_name, od.order_time, "
                + "t.topping_name, t.topping_price, t.topping_stock, p.product_stock, "
                + "mt.topping_quantity, (od.product_quantity * od.order_price) AS sub_total "
                + "FROM order_details AS od "
                + "LEFT JOIN product_details AS pd "
                + "ON od.order_id = pd.order_id "
                + "LEFT JOIN product AS p "
                + "ON pd.product_id = p.product_id "
                + "LEFT JOIN multiple_toppings AS mt "
                + "ON od.order_id = mt.order_id "
                + "LEFT JOIN topping AS t "
                + "ON mt.topping_id = t.topping_id "
                + "WHERE od.order_flag = 1 "
                + "AND od.served_flag = 0 "
                + "AND od.accounting_flag = 0 "
                + "ORDER BY od.order_id DESC";
		
		 try (PreparedStatement ps = conn.prepareStatement(sql)) {
	            try (ResultSet rs = ps.executeQuery()) {
	            	while (rs.next()) {
	                    int orderId = rs.getInt("order_id");
	                    
	                    OrderManagementInfo info = map.get(orderId);
	                    if (info == null) {
	                        info = new OrderManagementInfo();
	                        info.setOrderId(orderId);
	                        info.setOrderTime(rs.getString("order_time")); 
	                        info.setCategoryName(rs.getString("category_name"));
	                        info.setProductName(rs.getString("product_name"));
	                        info.setOrderQuantity(rs.getInt("product_quantity"));
	                        info.setOrderFlag(rs.getInt("order_flag"));
	                        info.setOrderPrice(rs.getInt("order_price"));
	                        info.setProductPrice(rs.getInt("product_price"));
	                        info.setProductStock(rs.getInt("product_stock"));
	                        info.setToppingStock(rs.getInt("topping_stock"));
	                        info.setToppingQuantity(rs.getInt("topping_quantity"));
	                        info.setToppingPrice(rs.getInt("topping_price"));
	                        // 初期金額（商品単価 × 数量）
	                        info.setSubTotal(rs.getInt("product_price") * rs.getInt("product_quantity"));
	                        map.put(orderId, info);
	                    }
	                    
	                    String toppingName = rs.getString("topping_name");
	                    if (toppingName != null) {
	                        int tQty = rs.getInt("topping_quantity");
	                        int tPrice = rs.getInt("topping_price");
	                        info.addTopping(toppingName, tQty, tPrice);
	                        // トッピング金額を加算 (トッピング単価 × 個数 × 商品の数量)
	                        int currentSubTotal = info.getSubTotal();
	                        info.setSubTotal(currentSubTotal + (tPrice * tQty * info.getOrderQuantity()));
	                    }
	                    int cname = info.getToppingStock();
	                }
	            }
	            
	        } catch (SQLException e) {
	            // ✅ ここでエラーハンドリング（例外処理）を行う
	            System.err.println("SQLの実行に失敗しました: " + e.getMessage());
	            // 必要に応じて、サーブレット側にエラーを知らせるために再スローする
	            throw new RuntimeException(e); 		
	        }
		return new ArrayList<>(map.values());
}

public void updateServedFlag(int orderId) {
	//JDBCドライバを読み込む
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e){
			throw new IllegalStateException("JDBCドライバを読み込めませんでしたあ♡");
		}
		
		String sql = "UPDATE order_details SET served_flag = 1 WHERE order_id = ?";
		
		 try (PreparedStatement ps = conn.prepareStatement(sql)) {
			 ps.setInt(1, orderId);
	            int rs = ps.executeUpdate();
		 }catch(SQLException e) {
			 
		 }
}
}
