<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/common.css">
<link rel="stylesheet" href="./css/servedHistory.css">

<title>注文済み履歴画面</title>
</head>
<body>
	<div class="header-nav-area">

		<div class="header-nav-row">
			<!-- 卓番選択エリア -->
			<div class="tab-items-container">

				<div class="home-container">
					<form action="HomeServlet" method="get">
						<button type="submit" class="home-btn">
							<img src="./image/homeButton.png" alt="ホーム" class="home-img">
						</button>
					</form>
				</div>



				<form action="ServedHistoryServlet" method="get">

					<div class="table-tabs">

						<button type="submit" name="tableFilter" value="全ての卓"
							class="tab-item-link ${currentTable == '全ての卓' ? 'active' : ''}">
							全ての卓<span class="tab-badge">${countTableAll}</span>
						</button>
						<button type="submit" name="tableFilter" value="1卓"
							class="tab-item-link ${currentTable == '1卓' ? 'active' : ''}">
							1卓<span class="tab-badge">${countTable1}</span>
						</button>
						<button type="submit" name="tableFilter" value="2卓"
							class="tab-item-link ${currentTable == '2卓' ? 'active' : ''}">
							2卓<span class="tab-badge">${countTable2}</span>
						</button>
						<button type="submit" name="tableFilter" value="3卓"
							class="tab-item-link ${currentTable == '3卓' ? 'active' : ''}">
							3卓<span class="tab-badge">${countTable3}</span>
						</button>
						<button type="submit" name="tableFilter" value="4卓"
							class="tab-item-link ${currentTable == '4卓' ? 'active' : ''}">
							4卓<span class="tab-badge">${countTable4}</span>
						</button>

					</div>

				</form>

			</div>
		</div>
	</div>

	<table class="table-wrapper">
		<tr>
			<th>卓番</th>
			<th>注文時間</th>
			<th>個数</th>
			<th>商品</th>
			<th>トッピング</th>
			<th>編集</th>
			<th>戻す</th>
		</tr>
		<c:forEach var="item" items="${shList}">
			<tr>
				<td>${item.tableId}</td>
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
							<img src="./image/edit_icon.png" alt="注文編集" class="edit-icon-img">
						</button>
						<input type="hidden" name="oid" value="${item.orderId}"> <input
							type="hidden" name="from" value="servedHistory">
					</form>
				</td>
				<td>
					<form action="ServedHistoryServlet" method="post">

						<input type="submit" name="action" value="戻す" class="return-btn">
						<input type="hidden" name="oid" value="${item.orderId}">
					</form>
				</td>
			</tr>
		</c:forEach>
	</table>

	<footer class="footer-legend">
		<form action="OrderManagementServlet" method="get"
			class="history-form">
			<input type="submit" name="action" value="注文">
		</form>
	</footer>
	<script src="./js/orderManagement.js" defer></script>
</body>
</html>