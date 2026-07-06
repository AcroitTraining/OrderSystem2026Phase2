<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>カテゴリ一覧画面</title>
<link rel="stylesheet" href="css/common.css">
<link rel="stylesheet" href="css/productList.css">
</head>
<body>

	<div class="home-container">
		<form action="HomeServlet" method="get">
			<button type="submit" name="action" value="home" class="home-btn">
				<img src="./image/homeButton.png" alt="ホーム" class="home-img">
			</button>
		</form>
	</div>

	<table class="product-table">
		<thead>
			<tr>
				<th>カテゴリ名</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>お好み焼き</td>
			</tr>
			<tr>
				<td>もんじゃ焼き</td>
			</tr>
			<tr>
				<td>鉄板焼き</td>
			</tr>
			<tr>
				<td>サイドメニュー</td>
			</tr>
			<tr>
				<td>ソフトドリンク</td>
			</tr>
			<tr>
				<td>お酒</td>
			</tr>
			<tr>
				<td>ボトル</td>
			</tr>
		</tbody>
	</table>

</body>
</html>