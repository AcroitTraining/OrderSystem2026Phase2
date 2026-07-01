<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/orderManagement.css">
<title>注文管理画面</title>
</head>
<body>
	<category-area>
	<form action="OrderManagementServlet" method="get" style="display: inline;">
		<input type="submit" name="tableFilter" value="全て" class="全て">
		<input type="submit" name="tableFilter" value="お好み焼き" class="お好み焼き">
		<input type="submit" name="tableFilter" value="もんじゃ焼き" class="もんじゃ焼き">
		<input type="submit" name="tableFilter" value="鉄板焼き" class="鉄板焼き">
		<input type="submit" name="tableFilter" value="サイドメニュー" class="サイドメニュー">
		<input type="submit" name="tableFilter" value="ソフトドリンク" class="ソフトドリンク">
		<input type="submit" name="tableFilter" value="お酒" class="お酒">
		<input type="submit" name="tableFilter" value="ボトル" class="ボトル">
		<input type="submit" name="tableFilter" value="1卓" class="1卓">
		<input type="submit" name="tableFilter" value="2卓" class="2卓">
		<input type="submit" name="tableFilter" value="3卓" class="3卓">
		<input type="submit" name="tableFilter" value="4卓" class="4卓">
	</form>
	</category-area>

	<h1>注文管理画面</h1>
	<table class="order-table">
		<tr>
			<th>No.</th>
			<th>注文時間</th>
			<th>個数</th>
			<th>商品</th>
			<th>トッピング</th>
			<th>編集</th>
			<th>卓番/提供</th>
		</tr>
		<c:forEach var="item" items="${omList}">
			<tr>
				<td>${item.orderId}</td>
				<td>${item.orderTime}</td>
				<td>${item.orderQuantity}</td>
				<td>${item.productName}</td>
				<td>
					<c:if test="${!empty item.toppings}">
						<c:forEach var="t" items="${item.toppings}">
							・${t.name}✕${t.quantity}<br>
						</c:forEach>
					</c:if>
				</td>
				<td>
					<form action="EditOrderServlet" method="get">
						<input type="submit" name="action" value="注文編集"> 
						<input type="hidden" name="oid" value="${item.orderId}">
						<input type="hidden" name="from" value="orderManagement">
					</form>
				</td>
				<td>
					<form action="OrderManagementServlet" method="post">

						<input type="submit" name="action" value="提供" class="${item.timeColorClass}"> 
						<input type="hidden" name="oid" value="${item.orderId}">
					</form>
				</td>
			</tr>
		</c:forEach>
	</table>

	<table class="footer-table">
		<tr>
			<td class="bg-green" style="display: table-cell; border: solid 1px #330000;">～5分</td>
			<td class="bg-yellow" style="display: table-cell; border: solid 1px #330000; color: #333333;">5分～10分</td>
			<td class="bg-red" style="display: table-cell; border: solid 1px #330000;">10分～
			<form action="ServedHistoryServlet" method="get">
			<input type="submit" name="action" value="履歴">
			</form>
			</td>
		</tr>
	</table>
</body>
</html>
