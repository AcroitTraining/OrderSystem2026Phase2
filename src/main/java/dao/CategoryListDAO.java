package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.CategoryListInfo;

public class CategoryListDAO {
    private final Connection conn;

    public CategoryListDAO(Connection conn) {
        this.conn = conn;
    }

    // 🟢 カテゴリ一覧の取得 (delete_flag = 0 の未削除データのみ)
    public List<CategoryListInfo> findAllCategory() throws SQLException {
        String sql = "SELECT category_id, category_name, display_order, delete_flag "
                   + "FROM category "
                   + "WHERE delete_flag = 1 "
                   + "ORDER BY category_id";
        
        List<CategoryListInfo> cList = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                CategoryListInfo cInfo = new CategoryListInfo();
                cInfo.setCategoryId(rs.getInt("category_id"));
                cInfo.setCategoryName(rs.getString("category_name"));
                cInfo.setDisplayOrder(rs.getInt("display_order"));
                cInfo.setDeleteFlag(rs.getInt("delete_flag"));
                cList.add(cInfo);
            }
        }
        return cList;
    }

    // 表示切り替え（1なら0に、0なら1に反転）
    public void updateCategoryDisplayOrder(int categoryId) throws SQLException {
        String sql = "UPDATE category SET display_order = 1 - display_order WHERE category_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ps.executeUpdate();
        }
    }

    // 論理削除（delete_flagを1に変更）
    public void updateCategoryDeleteFlag(int categoryId) throws SQLException {
        String sql = "UPDATE category SET delete_flag = 1 WHERE category_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ps.executeUpdate();
        }
    }
}