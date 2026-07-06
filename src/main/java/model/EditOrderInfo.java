package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EditOrderInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private int orderId;
    private int productId;
    private String productName;
    private int productQuantity;
    private String sessionId;
    private List<ToppingList> toppings = new ArrayList<>();

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getProductQuantity() { return productQuantity; }
    public void setProductQuantity(int productQuantity) { this.productQuantity = productQuantity; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public List<ToppingList> getToppings() { return toppings; }

    public void addTopping(int toppingId, String name, int quantity, int price) {
        this.toppings.add(new ToppingList(toppingId, name, quantity, price));
    }


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
        public String getName() { return name; }
        public int getQuantity() { return quantity; }
        public int getPrice() { return price; }

        public void setToppingId(int toppingId) { this.toppingId = toppingId; }
        public void setName(String name) { this.name = name; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public void setPrice(int price) { this.price = price; }
    }
}