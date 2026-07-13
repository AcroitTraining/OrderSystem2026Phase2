package model;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import dao.EditOrderDAO;
import jakarta.servlet.http.HttpServletRequest;

public class EditOrderLogic implements Serializable {
	private static final long serialVersionUID = 1L;

	private EditOrderDAO dao;

	public EditOrderLogic(EditOrderDAO dao) {
		this.dao = dao;
	}

	public EditOrderInfo loadOrder(int orderId) throws SQLException {

		EditOrderInfo info = dao.findOrderDetails(orderId);

		if (info == null) {
			return null;
		}

		List<EditOrderInfo.ToppingList> toppings =
				dao.findToppingListByProductId(info.getProductId(), orderId);

		if (toppings != null) {
			for (EditOrderInfo.ToppingList t : toppings) {
				info.addTopping(
						t.getToppingId(),
						t.getName(),
						t.getQuantity(),
						t.getPrice());
			}
		}

		return info;
	}

	public void deleteOrder(int orderId) throws SQLException {

		EditOrderInfo dbOriginal = dao.findOrderDetails(orderId);

		if (dbOriginal == null) {
			return;
		}

		int dbProductQty = dbOriginal.getProductQuantity();

		dao.updateProductStock(dbOriginal.getProductId(), dbProductQty);

		List<EditOrderInfo.ToppingList> toppings =
				dao.findToppingListByProductId(dbOriginal.getProductId(), orderId);

		if (toppings != null) {

			for (EditOrderInfo.ToppingList topping : toppings) {

				int qty = topping.getQuantity();

				if (qty > 0) {

					int total = qty * dbProductQty;

					dao.updateToppingStock(topping.getToppingId(), total);

				}

			}

		}

		dao.deleteOrderComplete(orderId);

	}

	public void updateOrder(EditOrderInfo info, int orderId)
			throws SQLException {

		EditOrderInfo dbOriginal = dao.findOrderDetails(orderId);

		List<EditOrderInfo.ToppingList> dbToppings =
				dao.findToppingListByProductId(info.getProductId(), orderId);

		updateProduct(info, dbOriginal, orderId);

		updateToppings(info, dbOriginal, dbToppings, orderId);

	}

	private void updateProduct(EditOrderInfo screen,
			EditOrderInfo db,
			int orderId)
					throws SQLException {

		int screenQty = screen.getProductQuantity();

		int dbQty = db.getProductQuantity();

		if (screenQty != dbQty) {

			dao.updateProductQuantity(orderId, screenQty);

			int diff = screenQty - dbQty;

			dao.updateProductStock(screen.getProductId(), -diff);

		}

	}

	private void updateToppings(
			EditOrderInfo screen,
			EditOrderInfo dbOriginal,
			List<EditOrderInfo.ToppingList> dbToppings,
			int orderId) throws SQLException {

		int screenProductQty = screen.getProductQuantity();
		int dbProductQty = dbOriginal.getProductQuantity();

		for (int i = 0; i < screen.getToppings().size(); i++) {

			EditOrderInfo.ToppingList screenTopping =
					screen.getToppings().get(i);

			EditOrderInfo.ToppingList dbTopping =
					dbToppings.get(i);

			int screenQty = screenTopping.getQuantity();
			int dbQty = dbTopping.getQuantity();

			if (dbQty != screenQty) {

				if (dbQty == 0 && screenQty > 0) {

					dao.insertTopping(
							orderId,
							screenTopping.getToppingId(),
							screenQty);

				} else if (dbQty > 0 && screenQty == 0) {

					dao.deleteToppingSingle(
							orderId,
							screenTopping.getToppingId());

				} else {

					dao.updateToppingQuantity(
							orderId,
							screenTopping.getToppingId(),
							screenQty);

				}
			}

			int newTotal = screenQty * screenProductQty;
			int oldTotal = dbQty * dbProductQty;

			int diff = newTotal - oldTotal;

			if (diff != 0) {

				dao.updateToppingStock(
						screenTopping.getToppingId(),
						-diff);
			}
		}
	}

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
	
	public void updateInfoFromRequest(EditOrderInfo info,
	        HttpServletRequest request) {

	    String oldProductQty = request.getParameter("oldProductQty");

	    if (oldProductQty != null && !oldProductQty.isEmpty()) {
	        info.setProductQuantity(Integer.parseInt(oldProductQty));
	    }

	    for (int i = 0; i < info.getToppings().size(); i++) {

	        String qty = request.getParameter("oldQty_" + i);

	        if (qty != null && !qty.isEmpty()) {

	            info.getToppings().get(i)
	                    .setQuantity(Integer.parseInt(qty));

	        }

	    }
	}
}