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
<!-- 卓番選択エリア -->
<table-area>

<form action="ServedHistoryServlet" method="get">

	<div class="table-tabs">

		<button type="submit" name="tableFilter" value="全ての卓"
			class="table-tab-btn ${currentTable == '全ての卓' ? 'active' : ''}">
			全ての卓
		</button>

		<button type="submit" name="tableFilter" value="1卓"
			class="table-tab-btn ${currentTable == '1卓' ? 'active' : ''}">
			1卓
		</button>

		<button type="submit" name="tableFilter" value="2卓"
			class="table-tab-btn ${currentTable == '2卓' ? 'active' : ''}">
			2卓
		</button>

		<button type="submit" name="tableFilter" value="3卓"
			class="table-tab-btn ${currentTable == '3卓' ? 'active' : ''}">
			3卓
		</button>

		<button type="submit" name="tableFilter" value="4卓"
			class="table-tab-btn ${currentTable == '4卓' ? 'active' : ''}">
			4卓
		</button>

	</div>

</form>

</table-area>

	<table class="history-table">
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
						<input type="submit" name="action" value="履歴編集"> <input
							type="hidden" name="oid" value="${item.orderId}"> <input
							type="hidden" name="from" value="servedHistory">
					</form>
				</td>
				<td>
					<form action="ServedHistoryServlet" method="post">

						<input type="submit" name="action" value="戻す"> <input
							type="hidden" name="oid" value="${item.orderId}">
					</form>
				</td>
			</tr>
		</c:forEach>
	</table>

	<table class="footer-table">
		<tr>
			<td>
				<form action="OrderManagementServlet" method="get">
					<input type="submit" name="action" value="注文">
				</form>
			</td>
		</tr>
</body>
</html>