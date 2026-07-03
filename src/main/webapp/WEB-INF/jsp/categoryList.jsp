<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>カテゴリ一覧画面</title>

</head>
<body>
	<form action="HomeServlet" method="get">
		<input type="submit" name="action" value="home">
	</form>

	<table class="category-table">
		<tr>
			<th>カテゴリ名</th>
		</tr>
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
	</table>
</body>
</html>