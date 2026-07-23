<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="./css/orderManagement.css">
<link rel="stylesheet" href="./css/common.css">

<title>注文管理画面</title>
</head>
<body>

	<div class="header-nav-area">

		<div class="header-nav-row">
			<div class="header-left-box">
				<form action="HomeServlet" method="get">
					<button type="submit" class="home-btn">
						<img src="./image/homeButton.png" alt="ホーム" class="home-icon">
					</button>
				</form>
			</div>
			<div class="category-filter">
				<div class="scrollable-tabs-wrapper">
					<form action="OrderManagementServlet" method="get"
						class="nav-tab-form">

						<input type="hidden" name="tableFilter" value="${currentTable}">

						<div class="tab-items-container">

							<button type="submit" name="categoryFilter" value="全て"
								class="tab-item-link ${currentCategory == '全て' ? 'active' : ''}">
								全て<span class="tab-badge">${countAll}</span>
							</button>

							<c:forEach var="category" items="${categoryCounts}">
								<button type="submit" name="categoryFilter"
									value="${category.key}"
									class="tab-item-link ${currentCategory == category.key ? 'active' : ''}">
									${category.key}<span class="tab-badge">${category.value}</span>
								</button>
							</c:forEach>
						</div>
					</form>
				</div>
			</div>
			<div class="table-filter">
				<div class="scrollable-tabs-wrapper">
					<form action="OrderManagementServlet" method="get"
						class="nav-tab-form">
						<input type="hidden" name="categoryFilter"
							value="${currentCategory}">
						<div class="tab-items-container">
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
	</div>

	<table class="table-wrapper">
		<thead>
			<tr class="order-row">
				<th>No.</th>
				<th>注文時間</th>
				<th>個数</th>
				<th>商品</th>
				<th>トッピング</th>
				<th>編集</th>
				<th>卓番/ 提供</th>
			</tr>
		</thead>
		<tbody id="orderTableBody">
			<c:forEach var="item" items="${omList}">
				<tr class="order-row" data-category="${item.categoryName}">
					<td>No.${item.orderId}</td>
					<td>${item.orderTime}</td>
					<td>${item.orderQuantity}個</td>
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
								class="served-btn ${item.timeColorClass}"> <input
								type="hidden" name="oid" value="${item.orderId}"> <input
								type="hidden" name="action" value="提供">
						</form>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<div class="footer-legend">
		<div class="legend-group">
			<span class="legend-title">※</span>
			<div class="legend-item">
				<span class="legend-box bg-green"></span> <span>～5分</span>
			</div>
			<div class="legend-item">
				<span class="legend-box bg-yellow"></span> <span>5分～10分</span>
			</div>
			<div class="legend-item">
				<span class="legend-box bg-red"></span> <span>10分以上</span>
			</div>
		</div>
		<form action="ServedHistoryServlet" method="get" class="history-form">
			<input type="submit" name="action" value="履歴">
		</form>
	</div>

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