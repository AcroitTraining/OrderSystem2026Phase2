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

    // カテゴリ一覧の取得 (delete_flag = 1 の未削除データのみ取得)
    public List<CategoryListInfo> findAllCategory() throws SQLException {
        String sql = "SELECT category_id, category_name, category_display_flag, category_delete_flag "
                   + "FROM category "
                   + "WHERE category_delete_flag = 1 "
                   + "ORDER BY category_id";
        
        List<CategoryListInfo> cList = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                CategoryListInfo cInfo = new CategoryListInfo();
                cInfo.setCategoryId(rs.getInt("category_id"));
                cInfo.setCategoryName(rs.getString("category_name"));
                cInfo.setDisplayOrder(rs.getInt("category_display_flag"));
                cInfo.setDeleteFlag(rs.getInt("category_delete_flag"));
                cList.add(cInfo);
            }
        }
        return cList;
    }

    // 論理削除（delete_flagを 0 に変更して非表示化する）
    public void updateCategoryDeleteFlag(int categoryId) throws SQLException {
        String sql = "UPDATE category SET category_delete_flag = 0 WHERE category_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ps.executeUpdate();
        }
    }
}