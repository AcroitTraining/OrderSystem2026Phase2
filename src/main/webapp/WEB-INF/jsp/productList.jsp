<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="./css/common.css">
<link rel="stylesheet" href="./css/productList.css">

<title>商品一覧画面</title>
</head>
<body>
	<div class="home-container">
		<form action="HomeServlet" method="get">
			<button type="submit" class="home-btn">
				<img src="./image/homeButton.png" alt="ホーム" class="home-img">
			</button>
		</form>
	</div>
	<!-- 🟢 カテゴリ・新規作成エリア -->
	<category-area class="category-wrapper"> 

	<form action="ProductEditServlet" method="get"
		class="create-form-sticky">
		<button type="submit" class="create-btn">
			<img src="./image/create_button-removebg-preview.png" alt="新規作成" class="create-img">
		</button>
	</form>

	<form action="ProductListServlet" method="get"
		style="display: inline; width: 100%;">
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
			<!-- 🟢 各ヘッダーにカラム識別用クラスを追加 -->
			<th class="col-name">商品名</th>
			<th class="col-price">価格</th>
			<th class="col-edit">編集</th>
			<th class="col-toggle">表示切り替え</th>
			<th class="col-delete">削除</th>
		</tr>
		<c:forEach var="item" items="${pList}">
			<tr>
				<!-- 🟢 各データセルにヘッダーと共通のクラスを追加 -->
				<td class="col-name">${item.productName}</td>
				<td class="col-price">${item.productPrice}</td>
				<td class="col-edit">
					<form action="ProductEditServlet" method="get">
						<input type="hidden" name="productId" value="${item.productId}">
						<input type="submit" name="action" value="編集">
					</form>
				</td>
				<td class="col-toggle display">
					<form action="ProductListServlet" method="post">
						<input type="hidden" name="productId" value="${item.productId}">
						<input type="hidden" name="action" value="表示切り替え">
						<c:choose>
							<c:when test="${item.productDisplayFlag == 1}">
								<span class="status-text text-show">表示中：</span>
							</c:when>
							<c:otherwise>
								<span class="status-text text-hide">非表示：</span>
							</c:otherwise>
						</c:choose>
						<label class="toggle-button-4"> <input type="checkbox"
							name="displayStatus" value="true" onclick="this.form.submit()"
							${item.productDisplayFlag == 1 ? 'checked' : ''} />
						</label>
					</form>
				</td>
				<td class="col-delete">
					<form action="ProductListServlet" method="post">
						<!-- 🟢 サーブレットへ「削除」のアクション名とIDを確実に送信 -->
						<input type="hidden" name="action" value="削除"> <input
							type="hidden" name="productId" value="${item.productId}">

						<!-- 🟢 画像を内包したボタン（押すとformがPOST送信されます） -->
						<button type="submit" class="img-delete-btn">
							<img src="./image/delete_button.png" alt="削除"
								class="delete-icon-img">
						</button>
					</form>
				</td>

			</tr>
		</c:forEach>
	</table>
</body>
</html>
