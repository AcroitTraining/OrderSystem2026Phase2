<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>カテゴリ一覧画面</title>
<link rel="stylesheet" href="./css/common.css">
<link rel="stylesheet" href="./css/categoryList.css"> 
</head>
<body>

	<!-- 🟢 変更：最上部に固定するヘッダーでホームボタンを囲みます -->
	<header class="fixed-header">
		<div class="home-container">
			<form action="HomeServlet" method="get">
				<button type="submit" name="action" value="home" class="home-btn">
					<img src="./image/homeButton.png" alt="ホーム" class="home-img">
				</button>
			</form>
		</div>
	</header>
	
	<table class="category-table">
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
