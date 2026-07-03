<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.ToppingEditInfo" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title><%= ((ToppingEditInfo)request.getAttribute("toppingEditInfo")).getToppingId() == 0 ? "新規トッピング作成" : "トッピング変更" %></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/toppingEdit.css">
</head>
<body>

    <div class="home-container">
        <a href="HomeServlet" class="home-btn">
            <img src="${pageContext.request.contextPath}/image/homeButton.png" class="home-icon" alt="ホーム">
        </a>
    </div>

    <%
        ToppingEditInfo info = (ToppingEditInfo) request.getAttribute("toppingEditInfo");
        if (info != null) {
            boolean isNew = (info.getToppingId() == 0); // 新規追加モードかどうかのフラグ
    %>
    
    <div class="page-title"><%= isNew ? "新規トッピング作成" : "トッピング変更" %></div>

    <div class="main-card">
        <form action="ToppingEditServlet" method="post">
            <input type="hidden" name="toppingId" value="<%= info.getToppingId() %>">

            <div class="form-group">
                <label class="label-title">トッピング名 <span class="label-sub">※最大18文字</span></label>
                <input type="text" name="toppingName" class="input-topping-name" maxlength="18" value="<%= info.getToppingName() %>" required>
            </div>

            <div class="form-group">
                <label class="label-title">金額 <span class="label-sub">※最大5桁</span></label>
                <input type="number" name="toppingPrice" class="input-price" min="0" max="99999" value="<%= isNew ? "" : info.getToppingPrice() %>" required>
                <span class="currency-unit">円</span>
            </div>

            <div class="footer-actions">
                <a href="ToppingListServlet" class="btn-back">トッピング一覧へ戻る</a>
                <button type="submit" class="btn-submit"><%= isNew ? "作成" : "変更" %></button>
            </div>
        </form>
    </div>
    <% } else { %>
        <p>画面情報の初期化に失敗しました。</p>
    <% } %>

</body>
</html>