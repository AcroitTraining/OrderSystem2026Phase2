<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/servedHistory.css">
<title>Insert title here</title>
</head>
<body>
<body>
	<category-area>
	<form action="ServedHistoryServlet" method="get" style="display: inline;">
	<input type="submit" name="tableFilter" value="全て">
	<input type="submit" name="tableFilter" value="1卓" class="1卓">
	<input type="submit" name="tableFilter" value="2卓" class="2卓">
	<input type="submit" name="tableFilter" value="3卓" class="3卓">
	<input type="submit" name="tableFilter" value="4卓" class="4卓">
	</form>
	</category-area>


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
							type="hidden" name="oid" value="${item.orderId}">
					</form>
				</td>
				<td>
					<form action="ServedHistoryServlet" method="post">

						<input type="submit" name="action" value="戻す">
						<input type="hidden" name="oid" value="${item.orderId}">
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