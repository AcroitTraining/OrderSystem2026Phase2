<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>トッピング一覧画面</title>
<link rel="stylesheet" href="./css/common.css">
<link rel="stylesheet" href="./css/toppingList.css">
</head>
<body>

	<!-- 🟢 画面最上部に固定するヘッダーエリア（1行化） -->
	<header class="fixed-header">
		<!-- ① ホームボタン -->
		<div class="home-container">
			<form action="HomeServlet" method="get">
				<button type="submit" class="home-btn">
					<img src="./image/homeButton.png" alt="ホーム" class="home-img">
				</button>
			</form>
		</div>
		
		<!-- ② トッピング新規作成ボタン（隣に綺麗に並べます） -->
		<div class="create-container">
			<form action="ToppingEditServlet" method="get">
				<button type="submit" class="create-btn">
					<img src="./image/createNew.png" alt="新規作成" class="create-img">
				</button>
			</form>
		</div>
	</header>

	<!-- 🟢 トッピング一覧テーブル -->
	<table class="table-wrapper">
		<tr>
			<!-- 各ヘッダーにカラム識別用クラスを追加 -->
			<th class="col-name">トッピング名</th>
			<th class="col-price">価格</th>
			<th class="col-edit">編集</th>
			<th class="col-toggle">表示切り替え</th>
			<th class="col-delete">削除</th>
		</tr>
		<c:forEach var="item" items="${tList}">
			<tr>
				<!-- 各データセルに共通のクラスを追加 -->
				<td class="col-name">${item.toppingName}</td>
				<td class="col-price">${item.toppingPrice}</td>
				<td class="col-edit">
					<form action="ToppingEditServlet" method="get">
						<input type="hidden" name="toppingId" value="${item.toppingId}">
						<button type="submit" name="action" value="トッピング編集"
								class="edit-img-btn">
								<img src="./image/edit_icon.png" alt="トッピング編集"
									class="edit-icon-img">
							</button>
					</form>
				</td>
				<td class="col-toggle display">
					<form action="ToppingListServlet" method="post">
						<!-- 🟢 バグ修正: productId になっていた部分を toppingId に修正 -->
						<input type="hidden" name="toppingId" value="${item.toppingId}">
						<input type="hidden" name="action" value="表示切り替え">
						<c:choose>
							<c:when test="${item.toppingDisplayFlag == 1}">
								<span class="status-text text-show">表示中：</span>
							</c:when>
							<c:otherwise>
								<span class="status-text text-hide">非表示：</span>
							</c:otherwise>
						</c:choose>
						<label class="toggle-button-4"> 
							<input type="checkbox" name="displayStatus" value="true" onclick="this.form.submit()"
							${item.toppingDisplayFlag == 1 ? 'checked' : ''} />
						</label>
					</form>
				</td>
				<td class="col-delete">
					<form action="ToppingListServlet" method="post">
						<input type="hidden" name="action" value="削除"> 
						<input type="hidden" name="toppingId" value="${item.toppingId}">
						
						<!-- 🟢 削除も画像ボタンに変更 -->
						<button type="submit" class="img-delete-btn">
							<img src="./image/delete_button.png" alt="削除" class="delete-icon-img">
						</button>
					</form>
				</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>
