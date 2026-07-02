package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductEditInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private int productId;
    private String productName;
    private int productPrice;
    private String categoryName;
    private List<Integer> selectedToppingIds = new ArrayList<>(); // 現在紐づいているトッピングID

    // ゲッター・セッター
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getProductPrice() { return productPrice; }
    public void setProductPrice(int productPrice) { this.productPrice = productPrice; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public List<Integer> getSelectedToppingIds() { return selectedToppingIds; }
    public void setSelectedToppingIds(List<Integer> selectedToppingIds) { this.selectedToppingIds = selectedToppingIds; }

    // トッピングマスタデータを表すstaticインナークラス
    public static class ToppingMaster implements Serializable {
        private static final long serialVersionUID = 1L;
        private int toppingId;
        private String toppingName;

        public ToppingMaster(int toppingId, String toppingName) {
            this.toppingId = toppingId;
            this.toppingName = toppingName;
        }
        public int getToppingId() { return toppingId; }
        public String getToppingName() { return toppingName; }
    }
}