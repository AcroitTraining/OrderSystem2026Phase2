<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.ProductEditInfo"%>
<%@ page import="java.util.List"%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title><%=((ProductEditInfo) request.getAttribute("productEditInfo")).getProductId() == 0
		? "商品作成"
		: "商品変更"%></title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/productEdit.css">

<script src="${pageContext.request.contextPath}/js/productEdit.js" defer></script>
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

	List<ProductEditInfo.ToppingMaster> allToppings = (List<ProductEditInfo.ToppingMaster>) request
			.getAttribute("allToppings");

	if (info != null) {
		boolean isNew = (info.getProductId() == 0);
	%>

	<div class="page-title">
		<%=isNew ? "新規商品作成" : "商品変更"%>
	</div>

	<div class="main-card">

		<form action="ProductEditServlet" method="post">

			<input type="hidden" name="productId"
				value="<%=info.getProductId()%>">


			<!-- 在庫数 -->
			<div class="stock-area">

				<label class="label-title"> 在庫数 </label>

				<div class="stock-input-wrapper">

					<input type="number" name="productStock" id="productStock"
						class="input-stock"
						value="<%=isNew ? 0 : info.getProductStock()%>" min="0">
					<span class="stock-unit"> 個 </span>
				</div>

			</div>


			<!-- カテゴリー -->
			<div class="form-group">

				<label class="label-title"> カテゴリー </label> <select name="categoryId">

					<option value="0"
						<%=info.getCategoryId() == 0
		? "selected"
		: ""%>>
						-選択してください-</option>

					<option value="1"
						<%=info.getCategoryId() == 1
		? "selected"
		: ""%>>
						お好み焼き</option>

					<option value="2"
						<%=info.getCategoryId() == 2
		? "selected"
		: ""%>>
						もんじゃ焼き</option>

					<option value="3"
						<%=info.getCategoryId() == 3
		? "selected"
		: ""%>>
						鉄板焼</option>

					<option value="4"
						<%=info.getCategoryId() == 4
		? "selected"
		: ""%>>
						サイドメニュー</option>

					<option value="5"
						<%=info.getCategoryId() == 5
		? "selected"
		: ""%>>
						ソフトドリンク</option>

					<option value="6"
						<%=info.getCategoryId() == 6
		? "selected"
		: ""%>>
						お酒</option>

					<option value="7"
						<%=info.getCategoryId() == 7
		? "selected"
		: ""%>>
						ボトル</option>

				</select>

			</div>


			<!-- 商品名 -->
			<div class="form-group">

				<label class="label-title"> 商品名 <span class="label-sub">
						※最大18文字 </span>
				</label> <input type="text" name="productName" class="input-product-name"
					value="<%=info.getProductName() == null
		? ""
		: info.getProductName()%>"
					placeholder="例: 納豆お好み焼き">

			</div>


			<!-- トッピング -->
			<div class="form-group">

				<div class="topping-fieldset">

					<span class="topping-legend"> トッピング </span>

					<div class="topping-grid">

						<%
						if (allToppings != null) {

							for (ProductEditInfo.ToppingMaster tm : allToppings) {

								boolean isChecked = !isNew
								&& info.getSelectedToppingIds()
										.contains(tm.getToppingId());
						%>

						<label class="topping-checkbox-label"> <input
							type="checkbox" name="toppingId" value="<%=tm.getToppingId()%>"
							<%=isChecked ? "checked" : ""%>> <%=tm.getToppingName()%>

						</label>

						<%
						}
						}
						%>

					</div>

				</div>

			</div>


			<!-- 金額 -->
			<div class="form-group">

				<label class="label-title"> 金額 <span class="label-sub">
						※最大5桁 </span>

				</label> <input type="number" name="productPrice" class="input-price"
					value="<%=isNew ? "" : info.getProductPrice()%>" placeholder="0">

				<span class="currency-unit"> 円 </span>

			</div>


			<!-- ボタン -->
			<div class="footer-actions">

				<a href="ProductListServlet" class="btn-back"> 商品一覧へ戻る </a>

				<button type="submit" class="btn-submit">
					<%=isNew ? "作成" : "変更"%>
				</button>

			</div>

		</form>

	</div>

	<%
	} else {
	%>

	<p>画面情報の初期化に失敗しました。</p>

	<%
	}
	%>

</body>
</html>