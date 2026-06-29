package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderManagementInfo {
private int orderId;
private int tableId;
private int sessionId;
private String categoryName;
private int orderQuantity;
private String productName;
private LocalDateTime time;
private List<ToppingList>topping = new ArrayList<>();

public static class ToppingList implements Serializable {
	private String name;
	private int quantity;
	private int price;
	public ToppingList(String name, int quantity, int price) {
		this.name = name;
		this.quantity = quantity;
		this.price = price;
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
}
