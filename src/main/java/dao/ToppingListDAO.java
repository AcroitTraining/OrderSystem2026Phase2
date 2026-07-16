package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.ToppingListInfo;
public class ToppingListDAO {
	private final Connection conn;

	public ToppingListDAO(Connection conn) {
		this.conn = conn;
	}

	public List<ToppingListInfo> findAllTopping() throws SQLException {
		String sql = "SELECT topping_id, topping_name, topping_price, topping_stock, "
				+ "topping_display_flag, topping_delete_flag "
				+ "FROM topping "
				+ "WHERE topping_delete_flag = 1";
		List<ToppingListInfo> tList = new ArrayList<>();
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ToppingListInfo tInfo = new ToppingListInfo();
					tInfo.setToppingId(rs.getInt("topping_id"));
					tInfo.setToppingName(rs.getString("topping_name"));
					tInfo.setToppingPrice(rs.getInt("topping_price"));
					tInfo.setToppingStock(rs.getInt("topping_stock"));
					tInfo.setToppingDeleteFlag(rs.getInt("topping_delete_flag"));
					tInfo.setToppingDisplayFlag(rs.getInt("topping_display_flag"));
					tList.add(tInfo);
				}
			}
		}
		return tList;
	}

	public void updateToppingDisplayFlag(int toppingId) throws SQLException {
		String sql = "UPDATE topping SET topping_display_flag = 1 - topping_display_flag WHERE topping_id = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, toppingId);
			ps.executeUpdate();
		}
	}

	public void updateToppingDeleteFlag(int toppingId) throws SQLException {
		String sql = "UPDATE topping SET topping_delete_flag = 0 WHERE topping_id = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, toppingId);
			ps.executeUpdate();
		}
	}
}