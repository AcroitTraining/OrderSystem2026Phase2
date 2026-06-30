<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>注文変更</title>
<link rel="stylesheet" href="css/editOrder.css">
</head>
<body>

<div class="main-container">
    <form action="EditOrderServlet" method="POST">
        <input type="hidden" name="orderId" value="${orderInfo.orderId}">
        <input type="hidden" name="oldProductQty" value="${orderInfo.orderQuantity}">
        <c:forEach var="t" items="${orderInfo.toppingList}" varStatus="status">
            <input type="hidden" name="oldToppingQty_${status.index}" value="${t.toppingQuantity}">
        </c:forEach>

        <div class="order-header">
            <span>${orderInfo.orderId}番</span>
            <span>${orderInfo.tableNumber}卓</span>
        </div>

        <div class="product-row">
            <span class="product-name">${orderInfo.productName}</span>
            <div class="qty-controller">
                <button type="submit" name="Button" value="product_minus" class="btn-minus">-</button>
                <span class="qty-val">${orderInfo.orderQuantity}</span>
                <button type="submit" name="Button" value="product_plus" class="btn-plus">+</button>
            </div>
            <button type="submit" name="mode" value="delete" class="btn-cancel" onclick="return confirm('この注文を取り消しますか？');">注文取り消し</button>
        </div>

        <div class="topping-grid">
            <c:forEach var="t" items="${orderInfo.toppingList}" varStatus="status">
                <div class="topping-item">
                    <span class="topping-name">${t.toppingName}</span>
                    <div class="qty-controller">
                        <button type="submit" name="Button" value="-${status.index}" class="btn-minus">-</button>
                        <span class="qty-val">${t.toppingQuantity}</span>
                        <button type="submit" name="Button" value="+${status.index}" class="btn-plus">+</button>
                    </div>
                </div>
            </c:forEach>
        </div>

        <div class="footer-actions">
            <button type="button" class="btn-back" onclick="location.href='OrderManagementServlet'">一覧へ戻る</button>
            <button type="submit" name="mode" value="update" class="btn-submit">変更</button>
        </div>
    </form>
</div>

</body>
</html>