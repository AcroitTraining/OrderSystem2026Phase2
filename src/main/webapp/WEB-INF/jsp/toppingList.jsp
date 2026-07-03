<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>トッピング一覧画面</title>
</head>
<body>

<form action="HomeServlet" method="get">
<input type="submit" name="action" value="home">
</form>
<form action="ProductEditServlet" method="get">
<input type="submit" name="action" value="新規作成">
</form>

	<table class="topping-table">
		<tr>
			<th>トッピング名</th>
			<th>価格</th>
			<th>編集</th>
			<th>表示切り替え</th>
			<th>削除</th>
		</tr>
		<c:forEach var="item" items="${tList}">

			<tr>
				<td>${item.toppingName}</td>
				<td>${item.toppingPrice}</td>
				<td>
					<form action="ToppingEditServlet" method="get">
						<input type="hidden" name="toppingId" value="${item.toppingId}">
						<input type="submit" name="action" value="編集">
					</form>
				</td>
				<td>

					<form action="ToppingListServlet" method="post">
						<input type="hidden" name="toppingId" value="${item.toppingId}">
						<input type="submit" name="action" value="表示切り替え">
					</form>
				</td>
				<td>
					<form action="ToppingListServlet" method="post">
						<input type="hidden" name="toppingId" value="${item.toppingId}">
						<input type="submit" name="action" value="削除">
					</form>
				</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>