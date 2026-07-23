package model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class ProductEditInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private int productId;
    private String productName;
    private int productPrice;
    private int productStock;
    private int categoryId;
    private String categoryName;
    private List<Integer> selectedToppingIds = new ArrayList<>(); // 現在紐づいているトッピングID
    // --- ProductEditInfo のゲッター・セッター ---
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public int getProductPrice() { return productPrice; }
    public void setProductPrice(int productPrice) { this.productPrice = productPrice; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public List<Integer> getSelectedToppingIds() { return selectedToppingIds; }
    public void setSelectedToppingIds(List<Integer> selectedToppingIds) { this.selectedToppingIds = selectedToppingIds; }
    public int getProductStock() {
		return productStock;
	}
	public void setProductStock(int productStock) {
		this.productStock = productStock;
	}
	// --- トッピングマスタデータを表すstaticインナークラス ---
    public static class ToppingMaster implements Serializable {
        private static final long serialVersionUID = 1L;
        private int toppingId;
        private String toppingName;
        // 引数なしのコンストラクタ（DAOの68行目のエラーを解決）
        public ToppingMaster() {
        }
        // 引数ありのコンストラクタ
        public ToppingMaster(int toppingId, String toppingName) {
            this.toppingId = toppingId;
            this.toppingName = toppingName;
        }
        // ゲッター・セッター（DAOの69行目・70行目のエラーを解決）
        public int getToppingId() { return toppingId; }
        public void setToppingId(int toppingId) { this.toppingId = toppingId; }
        public String getToppingName() { return toppingName; }
        public void setToppingName(String toppingName) { this.toppingName = toppingName; }
    }
}