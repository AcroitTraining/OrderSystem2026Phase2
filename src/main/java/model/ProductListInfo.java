package model;

public class ProductListInfo {
	private int productPrice, productStock, productId, productDisplayFlag, productDeleteFlag;
	private int categoryId; // ★追加
	private String categoryName, productName;

	public ProductListInfo() {}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getCategoryId() { // ★追加
		return categoryId;
	}

	public void setCategoryId(int categoryId) { // ★追加
		this.categoryId = categoryId;
	}

	public int getProductDisplayFlag() {
		return productDisplayFlag;
	}

	public void setProductDisplayFlag(int productDisplayFlag) {
		this.productDisplayFlag = productDisplayFlag;
	}

	public int getProductDeleteFlag() {
		return productDeleteFlag;
	}

	public void setProductDeleteFlag(int productDeleteFlag) {
		this.productDeleteFlag = productDeleteFlag;
	}

	public int getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}

	public int getProductStock() {
		return productStock;
	}

	public void setProductStock(int productStock) {
		this.productStock = productStock;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
}