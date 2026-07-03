<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="./css/ProductList.css">
<link rel="stylesheet" href="./css/common.css">
<link rel="stylesheet" href="./css/productList.css">
<title>商品一覧画面</title>
</head>
<body>
	<form action="HomeServlet" method="get">
		<input type="submit" name="action" value="home">
	</form>
	<form action="ProductEditServlet" method="get">
		<input type="submit" name="action" value="新規作成">
	</form>
	<category-area>
	<form action="ProductListServlet" method="get" style="display: inline;">
		<div class="category-tabs">
			<!-- currentCategoryと一致するボタンにだけ 'active' クラスを付与 -->
			<button type="submit" name="filter" value="全て"
				class="tab-btn ${currentCategory == '全て' ? 'active' : ''}"
				data-category="全て">全て</button>
			<button type="submit" name="filter" value="お好み焼き"
				class="tab-btn ${currentCategory == 'お好み焼き' ? 'active' : ''}"
				data-category="お好み焼き">お好み焼き</button>
			<button type="submit" name="filter" value="もんじゃ焼き"
				class="tab-btn ${currentCategory == 'もんじゃ焼き' ? 'active' : ''}"
				data-category="もんじゃ焼き">もんじゃ焼き</button>
			<button type="submit" name="filter" value="鉄板焼き"
				class="tab-btn ${currentCategory == '鉄板焼き' ? 'active' : ''}"
				data-category="鉄板焼き">鉄板焼き</button>
			<button type="submit" name="filter" value="サイドメニュー"
				class="tab-btn ${currentCategory == 'サイドメニュー' ? 'active' : ''}"
				data-category="サイドメニュー">サイドメニュー</button>
			<button type="submit" name="filter" value="ソフトドリンク"
				class="tab-btn ${currentCategory == 'ソフトドリンク' ? 'active' : ''}"
				data-category="ソフトドリンク">ソフトドリンク</button>
			<button type="submit" name="filter" value="お酒"
				class="tab-btn ${currentCategory == 'お酒' ? 'active' : ''}"
				data-category="お酒">お酒</button>
			<button type="submit" name="filter" value="ボトル"
				class="tab-btn ${currentCategory == 'ボトル' ? 'active' : ''}"
				data-category="ボトル">ボトル</button>
		</div>
	</form>
	</category-area>
	<table class="product-table">
		<tr>
			<th>商品名</th>
			<th>価格</th>
			<th>編集</th>
			<th>表示切り替え</th>
			<th>削除</th>
		</tr>
		<c:forEach var="item" items="${pList}">

			<tr>
				<td>${item.productName}</td>
				<td>${item.productPrice}</td>
				<td>
					<form action="ProductEditServlet" method="get">
						<input type="hidden" name="productId" value="${item.productId}">
						<input type="submit" name="action" value="編集">
					</form>
				</td>
				<td class="display">
					<form action="ProductListServlet" method="post">
						<input type="hidden" name="productId" value="${item.productId}">
						<input type="hidden" name="action" value="表示切り替え"> <label
							class="toggle-button-4">
							<input type="checkbox" name="displayStatus" value="true"
							onclick="this.form.submit()"
							${item.productDisplayFlag == 1 ? 'checked' : ''} />
						</label>
					</form>
				</td>

				<td>
					<form action="ProductListServlet" method="post">
						<input type="hidden" name="productId" value="${item.productId}">
						<input type="submit" name="action" value="削除">
					</form>
				</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>