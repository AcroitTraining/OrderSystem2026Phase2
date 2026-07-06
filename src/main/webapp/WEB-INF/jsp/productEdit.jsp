<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.ProductEditInfo" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title><%= ((ProductEditInfo)request.getAttribute("productEditInfo")).getProductId() == 0 ? "商品追加" : "商品変更" %></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/productEdit.css">
</head>
<body>

<div class="home-container">
    <form action="HomeServlet" method="get">
        <button type="submit" class="home-btn">
            <img src="./image/homeButton.png" alt="ホーム" class="home-img">
        </button>
    </form>
</div>

    <%
        ProductEditInfo info = (ProductEditInfo) request.getAttribute("productEditInfo");
        List<ProductEditInfo.ToppingMaster> allToppings = (List<ProductEditInfo.ToppingMaster>) request.getAttribute("allToppings");
        if (info != null) {
            boolean isNew = (info.getProductId() == 0); // 新規追加モードかどうかのフラグ
    %>
    
    <div class="page-title"><%= isNew ? "商品追加" : "商品変更" %></div> 

    <div class="main-card">
        <form action="ProductEditServlet" method="post">
            <input type="hidden" name="productId" value="<%= info.getProductId() %>">

            <div class="form-group">
                <label class="label-title">カテゴリー</label>
                <select name="categoryName">
                    <% if(isNew) { %>
                        <option value="" disabled selected style="display:none;">カテゴリーを選択してください</option>
                    <% } %>
                    <option value="お好み焼き" <%= "お好み焼き".equals(info.getCategoryName()) ? "selected" : "" %>>お好み焼き</option>
                    <option value="もんじゃ焼き" <%= "もんじゃ焼き".equals(info.getCategoryName()) ? "selected" : "" %>>もんじゃ焼き</option>
                    <option value="鉄板焼" <%= "鉄板焼".equals(info.getCategoryName()) ? "selected" : "" %>>鉄板焼</option>
                    <option value="サイドメニュー" <%= "サイドメニュー".equals(info.getCategoryName()) ? "selected" : "" %>>サイドメニュー</option>
                    <option value="ソフトドリンク" <%= "ソフトドリンク".equals(info.getCategoryName()) ? "selected" : "" %>>ソフトドリンク</option>
                    <option value="お酒" <%= "お酒".equals(info.getCategoryName()) ? "selected" : "" %>>お酒</option>
                    <option value="ボトル" <%= "ボトル".equals(info.getCategoryName()) ? "selected" : "" %>>ボトル</option>
                </select>
            </div>

            <div class="form-group">
                <label class="label-title">商品名 <span class="label-sub">※最大18文字</span></label>
                <input type="text" name="productName" class="input-product-name" maxlength="18" value="<%= info.getProductName() %>" placeholder="例: 納豆お込み焼き " required>
            </div>

            <div class="form-group">
                <div class="topping-fieldset">
                    <span class="topping-legend">トッピング</span>
                    <div class="topping-grid">
                        <%
                            if (allToppings != null) {
                                for (ProductEditInfo.ToppingMaster tm : allToppings) {
                                    boolean isChecked = !isNew && info.getSelectedToppingIds().contains(tm.getToppingId());
                        %>
                        <label class="topping-checkbox-label">
                            <input type="checkbox" name="toppingId" value="<%= tm.getToppingId() %>" <%= isChecked ? "checked" : "" %>>
                            <%= tm.getToppingName() %>
                        </label>
                        <%
                                }
                            }
                        %>
                    </div>
                </div>
            </div>

            <div class="form-group">
                <label class="label-title">金額 <span class="label-sub">※最大5桁</span></label>
                <input type="number" name="productPrice" class="input-price" min="0" max="99999" value="<%= isNew ? "" : info.getProductPrice() %>" placeholder="0" required>
                <span class="currency-unit">円</span>
            </div>

            <div class="footer-actions">
                <a href="ProductListServlet" class="btn-back">商品一覧へ戻る</a>
                <button type="submit" class="btn-submit"><%= isNew ? "追加" : "変更" %></button>
            </div>
        </form>
    </div>
    <% } else { %>
        <p>画面情報の初期化に失敗しました。</p>
    <% } %>

</body>
</html>