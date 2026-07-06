package model;

import java.io.Serializable;

public class EditOrderLogic implements Serializable {
    private static final long serialVersionUID = 1L;

    public void calcQuantity(EditOrderInfo info, String button) {
        if (info == null || button == null) return;

        if ("main_plus".equals(button)) {
            int currentQty = info.getProductQuantity();
            if (currentQty < 4) {
                info.setProductQuantity(currentQty + 1);
            }
        } else if ("main_minus".equals(button)) {
            int currentQty = info.getProductQuantity();
            if (currentQty > 1) {
                info.setProductQuantity(currentQty - 1);
            }
        }

        // 2. トッピングの数量変更（全体で最大4個まで）
        if (button.startsWith("+") || button.startsWith("-")) {
            int index = Integer.parseInt(button.substring(1));
            if (index >= 0 && index < info.getToppings().size()) {
                EditOrderInfo.ToppingList topping = info.getToppings().get(index);
                int currentTQty = topping.getQuantity();
                if (button.startsWith("+")) {
                    int totalToppingQty = 0;
                    for (EditOrderInfo.ToppingList t : info.getToppings()) {
                        totalToppingQty += t.getQuantity();
                    }
                    if (totalToppingQty < 4 && currentTQty < 4) {
                        topping.setQuantity(currentTQty + 1);
                    }
                } else if (button.startsWith("-")) {
                    if (currentTQty > 0) {
                        topping.setQuantity(currentTQty - 1);
                    }
                }
            }
        }
    }
}