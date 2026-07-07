<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/orderManagement.css">
<link rel="stylesheet" href="./css/common.css">
<title>注文管理画面</title>
</head>
<body>
	<div class="home-container">
		<form action="HomeServlet" method="get">
			<button type="submit" class="home-btn">
				<img src="./image/homeButton.png" alt="注文編集" class="home-img">
			</button>
		</form>
	</div>

	<!-- カテゴリ選択エリア -->
	<category-area>
	<form action="OrderManagementServlet" method="get"
		style="display: inline;">
		<!-- 裏で「現在選択されている卓番」を一緒に送信する -->
		<input type="hidden" name="tableFilter" value="${currentTable}">

		<div class="category-tabs">
			<!-- currentCategoryと一致するボタンにだけ 'active' クラスを付与 -->
			<button type="submit" name="categoryFilter" value="全て"
				class="tab-btn ${currentCategory == '全て' ? 'active' : ''}"
				data-category="全て">
				全て<span class="badge">${countAll}</span>
			</button>
			<button type="submit" name="categoryFilter" value="お好み焼き"
				class="tab-btn ${currentCategory == 'お好み焼き' ? 'active' : ''}"
				data-category="お好み焼き">
				お好み焼き<span class="badge">${countOkonomi}</span>
			</button>
			<button type="submit" name="categoryFilter" value="もんじゃ焼き"
				class="tab-btn ${currentCategory == 'もんじゃ焼き' ? 'active' : ''}"
				data-category="もんじゃ焼き">
				もんじゃ焼き<span class="badge">${countMonja}</span>
			</button>
			<button type="submit" name="categoryFilter" value="鉄板焼き"
				class="tab-btn ${currentCategory == '鉄板焼き' ? 'active' : ''}"
				data-category="鉄板焼き">
				鉄板焼き<span class="badge">${countTeppan}</span>
			</button>
			<button type="submit" name="categoryFilter" value="サイドメニュー"
				class="tab-btn ${currentCategory == 'サイドメニュー' ? 'active' : ''}"
				data-category="サイドメニュー">
				サイドメニュー<span class="badge">${countSide}</span>
			</button>
			<button type="submit" name="categoryFilter" value="ソフトドリンク"
				class="tab-btn ${currentCategory == 'ソフトドリンク' ? 'active' : ''}"
				data-category="ソフトドリンク">
				ソフトドリンク<span class="badge">${countSoft}</span>
			</button>
			<button type="submit" name="categoryFilter" value="お酒"
				class="tab-btn ${currentCategory == 'お酒' ? 'active' : ''}"
				data-category="お酒">
				お酒<span class="badge">${countSake}</span>
			</button>
			<button type="submit" name="categoryFilter" value="ボトル"
				class="tab-btn ${currentCategory == 'ボトル' ? 'active' : ''}"
				data-category="ボトル">
				ボトル<span class="badge">${countBottle}</span>
			</button>
		</div>
	</form>
	</category-area>

	<!--  卓番選択エリア -->
	<table-area>
	<form action="OrderManagementServlet" method="get"
		style="display: inline;">
		<!-- 現在選択されているカテゴリを一緒に送信する -->
		<input type="hidden" name="categoryFilter" value="${currentCategory}">

		<div class="category-tabs" style="margin-top: 10px;">
			<!-- スタイル流用 -->
			<!-- currentTableと一致するボタンにだけ 'active' クラスを付与 -->
			<button type="submit" name="tableFilter" value="全ての卓"
				class="tab-btn ${currentTable == '全ての卓' ? 'active' : ''}"
				data-table="全ての卓">
				全ての卓<span class="badge">${countTableAll}</span>
			</button>
			<button type="submit" name="tableFilter" value="1卓"
				class="tab-btn ${currentTable == '1卓' ? 'active' : ''}"
				data-table="1卓">
				1卓<span class="badge">${countTable1}</span>
			</button>
			<button type="submit" name="tableFilter" value="2卓"
				class="tab-btn ${currentTable == '2卓' ? 'active' : ''}"
				data-table="2卓">
				2卓<span class="badge">${countTable2}</span>
			</button>
			<button type="submit" name="tableFilter" value="3卓"
				class="tab-btn ${currentTable == '3卓' ? 'active' : ''}"
				data-table="3卓">
				3卓<span class="badge">${countTable3}</span>
			</button>
			<button type="submit" name="tableFilter" value="4卓"
				class="tab-btn ${currentTable == '4卓' ? 'active' : ''}"
				data-table="4卓">
				4卓<span class="badge">${countTable4}</span>
			</button>
		</div>
	</form>
	</table-area>


		<table class="order-table">
			<tr class="order-row" data-category="${item.categoryName}">
				<th>No.</th>
				<th>注文時間</th>
				<th>個数</th>
				<th>商品</th>
				<th>トッピング</th>
				<th>編集</th>
				<th>卓番/提供</th>
			</tr>
			<c:forEach var="item" items="${omList}">
				<tr class="order-row" data-category="${item.categoryName}">
					<td>${item.orderId}</td>
					<td>${item.orderTime}</td>
					<td>${item.orderQuantity}</td>
					<td>${item.productName}</td>
					<td><c:if test="${!empty item.toppings}">
							<c:forEach var="t" items="${item.toppings}">
                    ・${t.name}✕${t.quantity}<br>
							</c:forEach>
						</c:if></td>
					<td>
						<form action="EditOrderServlet" method="get">
							<button type="submit" name="action" value="注文編集"
								class="edit-img-btn">
								<img src="./image/edit_icon.png" alt="注文編集"
									class="edit-icon-img">
							</button>

							<input type="hidden" name="oid" value="${item.orderId}">
							<input type="hidden" name="from" value="orderManagement">
						</form>
					</td>
					<td>
						<form action="OrderManagementServlet" method="post"
							class="served-form">
							<input type="submit" name="action" value="${item.tableId}卓
提供"
								class="${item.timeColorClass}"> <input type="hidden"
								name="oid" value="${item.orderId}"> <input type="hidden"
								name="action" value="提供">
						</form>
					</td>
				</tr>
			</c:forEach>
		</table>

		<table class="footer-table">
			<tr>
				<td class="bg-green"
					style="display: table-cell; border: solid 1px #330000;">～5分</td>
				<td class="bg-yellow"
					style="display: table-cell; border: solid 1px #330000; color: #333333;">5分～10分</td>
				<td class="bg-red"
					style="display: table-cell; border: solid 1px #330000;">10分～
					<form action="ServedHistoryServlet" method="get">
						<input type="submit" name="action" value="履歴">
					</form>
				</td>
			</tr>
		</table>


		<div id="customModal" class="modal-overlay" style="display: none;">
			<div class="modal-content">
				<p class="modal-message-title">確認</p>
				<p class="modal-message-sub">本当に提供しますか？</p>
				<div class="modal-buttons">
					<button type="button" id="modalNoBtn" class="btn-modal-no">いいえ</button>
					<button type="button" id="modalYesBtn" class="btn-modal-yes">はい</button>
				</div>
			</div>
		</div>


		<script src="./js/orderManagement.js" defer></script>
</body>
</html>
