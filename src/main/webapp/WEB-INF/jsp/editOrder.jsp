<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.EditOrderInfo" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>注文変更</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/editOrder.css">
</head>
<body>

    <%
        // 正しい名前（orderManagement / servedHistory）で取得
        String from = (String) request.getAttribute("from");
        if (from == null) { from = "orderManagement"; }

        // 新しい名前に合わせた戻り先URLの判定
        String backUrl = "ServedHistoryServlet";
        if ("orderManagement".equals(from)) {
            backUrl = "OrderManagementServlet";
        }
    %>

    <div class="home-container">
        <a href="<%= backUrl %>" class="home-btn">
            <svg viewBox="0 0 24 24" width="28" height="28" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"></path>
                <polyline points="9 22 9 12 15 12 15 22"></polyline>
            </svg>
        </a>
    </div>

    <%
        EditOrderInfo info = (EditOrderInfo) request.getAttribute("editOrderInfo");
        if (info != null) {
    %>
    <div class="order-box">
        <div class="box-header">
            <%= info.getSessionId() %>番 4卓
        </div>

        <form action="EditOrderServlet" method="post" id="editOrderForm">
            <input type="hidden" name="oid" value="<%= info.getOrderId() %>">
            <input type="hidden" name="mode" id="submitMode" value="">
            <input type="hidden" name="oldProductQty" value="<%= info.getProductQuantity() %>">
            
            <input type="hidden" name="from" value="<%= from %>">

            <div class="main-product-row">
                <span class="product-name"><%= info.getProductName() %></span>
                <div class="quantity-counter">
                    <button type="submit" name="Button" value="main_minus" class="btn-minus">−</button>
                    <span class="qty-num"><%= info.getProductQuantity() %></span>
                    <button type="submit" name="Button" value="main_plus" class="btn-plus">+</button>
                </div>
                <button type="submit" name="Button" value="delete_order" class="btn-cancel-order">注文取り消し</button>
            </div>

            <div class="toppings-container">
                <% 
                    if (info.getToppings() != null) {
                        for (int i = 0; i < info.getToppings().size(); i++) {
                            EditOrderInfo.ToppingList topping = info.getToppings().get(i);
                %>
                <div class="topping-cell">
                    <span class="topping-title"><%= topping.getName() %></span>
                    <div class="quantity-counter">
                        <input type="hidden" name="oldQty_<%= i %>" value="<%= topping.getQuantity() %>">
                        <button type="submit" name="Button" value="-<%= i %>" class="btn-minus">−</button>
                        <span class="qty-num"><%= topping.getQuantity() %></span>
                        <button type="submit" name="Button" value="+<%= i %>" class="btn-plus">+</button>
                    </div>
                </div>
                <% 
                        }
                    }
                %>
            </div>

            <div class="box-footer">
                <a href="<%= backUrl %>" class="btn-go-back">一覧へ戻る</a>
                <button type="button" class="btn-save-changes" id="triggerUpdate">変更</button>
            </div>
        </form>
    </div>

    <div id="customModal" class="modal-overlay">
        <div class="modal-content">
            <p class="modal-message-title">この内容で変更します</p>
            <p class="modal-message-sub">よろしいですか？</p>
            <div class="modal-buttons">
                <button type="button" id="modalNo" class="btn-modal-no">いいえ</button>
                <button type="button" id="modalYes" class="btn-modal-yes">はい</button>
            </div>
        </div>
    </div>
    <% } %>

    <script src="${pageContext.request.contextPath}/js/editOrder.js"></script>
</body>
</html>