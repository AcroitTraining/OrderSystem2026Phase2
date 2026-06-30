<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>注文管理画面</title>
</head>
<body>
	<category-area>
	<td><input type="submit" name="category" value="全て" class="全て"></td>
	<td><input type="submit" name="category" value="お好み焼き"
		class="お好み焼き"></td>
	<td><input type="submit" name="category" value="もんじゃ焼き"
		class="もんじゃ焼き"></td>
	<td><input type="submit" name="category" value="鉄板焼き" class="鉄板焼き"></td>
	<td><input type="submit" name="category" value="サイドメニュー"
		class="サイドメニュー"></td>
	<td><input type="submit" name="category" value="ソフトドリンク"
		class="ソフトドリンク"></td>
	<td><input type="submit" name="category" value="お酒" class="お酒"></td>
	<td><input type="submit" name="category" value="ボトル" class="ボトル"></td>
	<td><input type="submit" name="category" value="1卓" class="1卓"></td>
	<td><input type="submit" name="category" value="2卓" class="2卓"></td>
	<td><input type="submit" name="category" value="3卓" class="3卓"></td>
	<td><input type="submit" name="category" value="4卓" class="4卓"></td>
	</category-area>
	<h1>注文管理画面</h1>

	<c:forEach var="item" items="${omList}">
		<table>
		<form action="EditOrderServlet" method="get">
			<tr>
			<input type="hidden" name="oid" value="${item.orderId}">
				<td>${item.orderId}</td>
				<td>${item.orderTime}</td>
				<td>${item.orderQuantity}</td>
				<td>${item.productName}</td>
				<c:if test="${!empty item.toppings}">
					<c:forEach var="t" items="${item.toppings}">
								<td>・${t.name}✕${t.quantity}</td>	
					</c:forEach>	
				</c:if>					
				<td><input type="submit" name="category" value="編集" class="editButton"></td>
			</tr>
		</form>
		</table>
	</c:forEach>


	<footer></footer>
</body>
</html>