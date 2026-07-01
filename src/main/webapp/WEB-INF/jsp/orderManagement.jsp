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
		<td><input type="submit" name="category" value="全て" class="全て"></td>
		<td><input type="submit" name="category" value="お好み焼き" class="お好み焼き"></td>
		<td><input type="submit" name="category" value="もんじゃ焼き" class="もんじゃ焼き"></td>
		<td><input type="submit" name="category" value="鉄板焼き" class="鉄板焼き"></td>
		<td><input type="submit" name="category" value="サイドメニュー" class="サイドメニュー"></td>
		<td><input type="submit" name="category" value="ソフトドリンク" class="ソフトドリンク"></td>
		<td><input type="submit" name="category" value="お酒" class="お酒"></td>
		<td><input type="submit" name="category" value="ボトル" class="ボトル"></td>
		<td><input type="submit" name="category" value="1卓" class="1卓"></td>
		<td><input type="submit" name="category" value="2卓" class="2卓"></td>
		<td><input type="submit" name="category" value="3卓" class="3卓"></td>
		<td><input type="submit" name="category" value="4卓" class="4卓"></td>
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
						<input type="submit" name="action" value="編集"> 
						<input type="hidden" name="oid" value="${item.orderId}">
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
			<td class="bg-red" style="display: table-cell; border: solid 1px #330000;">10分～</td>
		</tr>
	</table>
</body>
</html>
