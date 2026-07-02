package model;

public class ToppingListInfo {
private int toppingId, toppingPrice, toppingStock, toppingDisplayFlag, toppingDeleteFlag;

public ToppingListInfo() {}

public int getToppingId() {
	return toppingId;
}
public void setToppingId(int toppingId) {
	this.toppingId = toppingId;
}
public int getToppingPrice() {
	return toppingPrice;
}
public void setToppingPrice(int toppingPrice) {
	this.toppingPrice = toppingPrice;
}
public int getToppingStock() {
	return toppingStock;
}
public void setToppingStock(int toppingStock) {
	this.toppingStock = toppingStock;
}
public int getToppingDisplayFlag() {
	return toppingDisplayFlag;
}
public void setToppingDisplayFlag(int toppingDisplayFlag) {
	this.toppingDisplayFlag = toppingDisplayFlag;
}
public int getToppingDeleteFlag() {
	return toppingDeleteFlag;
}
public void setToppingDeleteFlag(int toppingDeleteFlag) {
	this.toppingDeleteFlag = toppingDeleteFlag;
}
public String getToppingName() {
	return toppingName;
}
public void setToppingName(String toppingName) {
	this.toppingName = toppingName;
}
private String toppingName;
}
