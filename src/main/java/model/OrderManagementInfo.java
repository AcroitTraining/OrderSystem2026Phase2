package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderManagementInfo {
private int orderId, tableId, toppingQuantity, toppingPrice, subTotal, productPrice,orderQuantity, toppingStock, productStock, allOrderPrice, sessionId, orderPrice, orderFlag;
private String categoryName, productName;
private String orderTime;
private String timeColorClass;
private List<ToppingList>toppings = new ArrayList<>();

public static class ToppingList implements Serializable {
	private String name;
	private int quantity;
	private int price;
	public ToppingList(String name, int quantity, int price) {
		this.setName(name);
		this.setQuantity(quantity);
		this.setPrice(price);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
}

public int getTableId() {
	return tableId;
}

public int getToppingQuantity() {
	return toppingQuantity;
}

public void setToppingQuantity(int toppingQuantity) {
	this.toppingQuantity = toppingQuantity;
}

public int getToppingPrice() {
	return toppingPrice;
}

public void setToppingPrice(int toppingPrice) {
	this.toppingPrice = toppingPrice;
}

public int getSubTotal() {
	return subTotal;
}

public void setSubTotal(int subTotal) {
	this.subTotal = subTotal;
}

public int getProductPrice() {
	return productPrice;
}

public void setProductPrice(int productPrice) {
	this.productPrice = productPrice;
}

public int getToppingStock() {
	return toppingStock;
}

public void setToppingStock(int toppingStock) {
	this.toppingStock = toppingStock;
}

public int getProductStock() {
	return productStock;
}

public void setProductStock(int productStock) {
	this.productStock = productStock;
}

public int getAllOrderPrice() {
	return allOrderPrice;
}

public void setAllOrderPrice(int allOrderPrice) {
	this.allOrderPrice = allOrderPrice;
}

public int getOrderPrice() {
	return orderPrice;
}

public void setOrderPrice(int orderPrice) {
	this.orderPrice = orderPrice;
}

public int getOrderFlag() {
	return orderFlag;
}

public void setOrderFlag(int orderFlag) {
	this.orderFlag = orderFlag;
}

public void setTableId(int tableId) {
	this.tableId = tableId;
}

public int getSessionId() {
	return sessionId;
}

public void setSessionId(int sessionId) {
	this.sessionId = sessionId;
}

public String getCategoryName() {
	return categoryName;
}

public void setCategoryName(String categoryName) {
	this.categoryName = categoryName;
}

public int getOrderQuantity() {
	return orderQuantity;
}

public void setOrderQuantity(int orderQuantity) {
	this.orderQuantity = orderQuantity;
}

public String getProductName() {
	return productName;
}

public void setProductName(String productName) {
	this.productName = productName;
}

public List<ToppingList> getToppings() {
	return toppings;
}

public void setToppings(List<ToppingList> topping) {
	this.toppings = topping;
}

public void addTopping(String name, int quantity, int price) {
	if (name != null) {
		this.toppings.add(new ToppingList(name, quantity, price));
	}	
}

public OrderManagementInfo() {}

public OrderManagementInfo(int orderId) {
	this.orderId = orderId;
}

public int getOrderId() {
	return orderId;
}

public void setOrderId(int orderId) {
	this.orderId = orderId;
}

public String getOrderTime() {
	return orderTime;
}

public void setOrderTime(String orderTime) {
	this.orderTime = orderTime;
}

public String getTimeColorClass() {
	return timeColorClass;
}

public void setTimeColorClass(String timeColorClass) {
	this.timeColorClass = timeColorClass;
}
}
