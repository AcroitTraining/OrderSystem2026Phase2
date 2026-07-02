<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/productList.css">
<title>商品一覧画面</title>
</head>
<body>
<form action="HomeServlet" method="get">
<input type="submit" name="action" value="home">
</form>
	<category-area>
	<form action="ProductListServlet" method="get" style="display: inline;">
		<input type="submit" name="filter" value="全て" class="全て"> <input
			type="submit" name="filter" value="お好み焼き" class="お好み焼き"> <input
			type="submit" name="filter" value="もんじゃ焼き" class="もんじゃ焼き"> <input
			type="submit" name="filter" value="鉄板焼き" class="鉄板焼き"> <input
			type="submit" name="filter" value="サイドメニュー" class="サイドメニュー">
		<input type="submit" name="filter" value="ソフトドリンク" class="ソフトドリンク">
		<input type="submit" name="filter" value="お酒" class="お酒"> <input
			type="submit" name="filter" value="ボトル" class="ボトル">
	</form>
	</category-area>
	<table class="product-table">
		<tr>
			<th>商品名</th>
			<th>価格</th>
			<th>編集</th>
			<th>表示切り替え</th>
			<th>削除</th>
		</tr>
		<c:forEach var="item" items="${pList}">
			
			<tr>
				<td>${item.productName}</td>
				<td>${item.productPrice}</td>
				<td>
					<form action="ProductEditServlet" method="get">
					<input type="hidden" name="productId" value="${item.productId}">
						<input type="submit" name="action" value="編集">
					</form>
				</td>
				<td>
				
					<form action="ProductListServlet" method="post">
					<input type="hidden" name="productId" value="${item.productId}">
						<input type="submit" name="action" value="表示切り替え">
					</form>
				</td>
				<td>
					<form action="ProductListServlet" method="post">
					<input type="hidden" name="productId" value="${item.productId}">
						<input type="submit" name="action" value="削除">
					</form>
				</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>