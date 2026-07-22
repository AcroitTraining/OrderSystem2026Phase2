package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.CategoryEdit;

public class CategoryEditDAO {

    // カテゴリ詳細の取得
    public CategoryEdit findCategoryDetails(Connection conn, int categoryId) throws SQLException {
        CategoryEdit info = null;
        String sql = "SELECT category_name FROM category WHERE category_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    info = new CategoryEdit();
                    info.setCategoryId(categoryId);
                    info.setCategoryName(rs.getString("category_name"));
                }
            }
        }
        return info;
    }

    // 新規登録処理（表示フラグ・削除フラグ等を1に指定）
    public void insertCategoryDetails(Connection conn, String categoryName) throws SQLException {
        String sql = "INSERT INTO category (category_name, category_display_flag, category_delete_flag) VALUES (?, 1, 1)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, categoryName);
            ps.executeUpdate();
        }
    }

    // 更新処理
    public void updateCategoryDetails(Connection conn, int categoryId, String categoryName) throws SQLException {
        String sql = "UPDATE category SET category_name = ? WHERE category_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, categoryName);
            ps.setInt(2, categoryId);
            ps.executeUpdate();
        }
    }
}