package model;

import java.util.ArrayList;
import java.util.List;

public class EditOrderInfo {
    private int orderId;
    private int productId;
    private String productName;
    private int orderQuantity;
    private String sessionId;
    private int tableNumber;
    // トッピングを複数保持するためのリスト
    private List<ToppingInfo> toppingList = new ArrayList<>();

    // 空のコンストラクタ
    public EditOrderInfo() {}

    /**
     * トッピング情報を保持するための内部クラス（インナークラス）
     */
    public static class ToppingInfo {
        private int toppingId;
        private String toppingName;
        private int toppingQuantity;

        // コンストラクタ
        public ToppingInfo(int toppingId, String toppingName, int toppingQuantity) {
            this.toppingId = toppingId;
            this.toppingName = toppingName;
            this.toppingQuantity = toppingQuantity;
        }

        // トッピング用のゲッター・セッター
        public int getToppingId() { return toppingId; }
        public void setToppingId(int toppingId) { this.toppingId = toppingId; }
        public String getToppingName() { return toppingName; }
        public void setToppingName(String toppingName) { this.toppingName = toppingName; }
        public int getToppingQuantity() { return toppingQuantity; }
        public void setToppingQuantity(int toppingQuantity) { this.toppingQuantity = toppingQuantity; }
    }

    // メイン注文情報のゲッター・セッター
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getOrderQuantity() { return orderQuantity; }
    public void setOrderQuantity(int orderQuantity) { this.orderQuantity = orderQuantity; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public int getTableNumber() { return tableNumber; }
    public void setTableNumber(int tableNumber) { this.tableNumber = tableNumber; }

    public List<ToppingInfo> getToppingList() { return toppingList; }
    public void setToppingList(List<ToppingInfo> toppingList) { this.toppingList = toppingList; }

    /**
     * DAOなどで取得したトッピングをリストに1件ずつ追加するための便利メソッド
     */
    public void addTopping(ToppingInfo topping) {
        this.toppingList.add(topping);
    }
}