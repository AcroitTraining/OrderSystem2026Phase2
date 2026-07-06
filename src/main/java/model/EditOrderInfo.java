package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EditOrderInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private int orderId;
    private int productId;
    private String productName;
    private int productPrice;     // DAOで必要となる商品の単価
    private int productQuantity;
    private String sessionId;
    private int tableId;          // 【今回追加】JSPのヘッダーに表示する卓番号
    private int subTotal;
    private List<ToppingList> toppings = new ArrayList<>();

    public EditOrderInfo() {}

    public static class ToppingList implements Serializable {
        private static final long serialVersionUID = 1L;
        private int toppingId;
        private String name;
        private int quantity;
        private int price;

        public ToppingList(int toppingId, String name, int quantity, int price) {
            this.toppingId = toppingId;
            this.name = name;
            this.quantity = quantity;
            this.price = price;
        }

        public int getToppingId() { return toppingId; }
        public void setToppingId(int toppingId) { this.toppingId = toppingId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public int getPrice() { return price; }
        public void setPrice(int price) { this.price = price; }
    }

    // ==================================================
    // ゲッター・セッター群
    // ==================================================
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getProductPrice() { return productPrice; }
    public void setProductPrice(int productPrice) { this.productPrice = productPrice; }

    public int getProductQuantity() { return productQuantity; }
    public void setProductQuantity(int productQuantity) { this.productQuantity = productQuantity; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public int getTableId() { return tableId; }
    public void setTableId(int tableId) { this.tableId = tableId; }

    public int getSubTotal() { return subTotal; }
    public void setSubTotal(int subTotal) { this.subTotal = subTotal; }

    public List<ToppingList> getToppings() { return toppings; }
    public void setToppings(List<ToppingList> toppings) { this.toppings = toppings; }

    public void addTopping(int id, String name, int qty, int price) {
        this.toppings.add(new ToppingList(id, name, qty, price));
    }
}