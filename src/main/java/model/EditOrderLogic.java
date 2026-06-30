package model;

import java.util.List;

public class EditOrderLogic {

    /**
     * 商品の数量を変更するメソッド
     */
    public int calcProductQuantity(int currentQty, String action) {
        if ("plus".equals(action)) {
            return currentQty + 1;
        } else if ("minus".equals(action) && currentQty > 0) {
            return currentQty - 1;
        }
        return currentQty;
    }

    /**
     * トッピングの数量を変更するメソッド (最大4個制限を維持する場合)
     */
    public void calcToppingQuantity(List<EditOrderInfo.ToppingInfo> toppingList, int index, String action) {
        if (toppingList == null || index < 0 || index >= toppingList.size()) return;
        
        EditOrderInfo.ToppingInfo t = toppingList.get(index);
        int qty = t.getToppingQuantity();
        
        // 全トッピングの合計選択数を計算
        int totalQty = 0;
        for (EditOrderInfo.ToppingInfo topping : toppingList) {
            totalQty += topping.getToppingQuantity();
        }
        
        if ("plus".equals(action)) {
            // 合計4個未満の場合のみ追加可能
            if (totalQty < 4) {
                t.setToppingQuantity(qty + 1);
            }
        } else if ("minus".equals(action)) {
            if (qty > 0) {
                t.setToppingQuantity(qty - 1);
            }
        }
    }
}
