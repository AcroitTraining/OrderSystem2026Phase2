<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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

	<!-- 画面最上部に固定するヘッダーエリア -->
	<header class="fixed-header">
		<div class="content-area">
			<!-- ① ホームボタン -->
			<div class="home-container">
				<form action="HomeServlet" method="get">
					<button type="submit" class="home-btn">
						<img src="./image/homeButton.png" alt="ホーム" class="home-img">
					</button>
				</form>
			</div>

			<!-- ② カテゴリ新規作成ボタン（CategoryEditServletへ移動） -->
			<div class="create-container">
				<form action="CategoryEditServlet" method="get">
					<button type="submit" class="create-btn">
						<img src="./image/createNew.png" alt="新規作成" class="create-img">
					</button>
				</form>
			</div>
		</div>
	</header>

	<!-- カテゴリ一覧テーブル -->
	<table class="table-wrapper">
		<tr>
			<th class="col-name">カテゴリ名</th>
			<th class="col-edit">編集</th>
			<th class="col-toggle">表示切り替え</th>
			<th class="col-delete">削除</th>
		</tr>

		<!-- 🟢 Servletの request.setAttribute("cList", cList) からデータを取得してループ -->
		<c:forEach var="item" items="${cList}">
			<tr>
				<td class="col-name">${item.categoryName}</td>
				
				<!-- 編集ボタン (CategoryEditServletに遷移) -->
				<td class="col-edit">
					<form action="CategoryEditServlet" method="get">
						<input type="hidden" name="categoryId" value="${item.categoryId}">
						<button type="submit" name="action" value="カテゴリ編集" class="edit-img-btn">
							<img src="./image/edit_icon.png" alt="カテゴリ編集" class="edit-icon-img">
						</button>
					</form>
				</td>

				<!-- 表示切り替えスイッチ -->
				<td class="col-toggle display">
					<form action="CategoryListServlet" method="post">
						<input type="hidden" name="categoryId" value="${item.categoryId}">
						<input type="hidden" name="action" value="表示切り替え">
						<c:choose>
							<c:when test="${item.displayOrder == 1}">
								<span class="status-text text-show">表示中：</span>
							</c:when>
							<c:otherwise>
								<span class="status-text text-hide">非表示：</span>
							</c:otherwise>
						</c:choose>
						<label class="toggle-button-4">
							<input type="checkbox" name="displayStatus" value="true"
								onclick="this.form.submit()"
								${item.displayOrder == 1 ? 'checked' : ''} />
						</label>
					</form>
				</td>

				<!-- 削除ボタン -->
				<td class="col-delete">
					<form action="CategoryListServlet" method="post" class="delete-form">
						<input type="hidden" name="action" value="削除">
						<input type="hidden" name="categoryId" value="${item.categoryId}">

						<button type="submit" class="img-delete-btn">
							<img src="./image/delete_button.png" alt="削除" class="delete-icon-img">
						</button>
					</form>
				</td>
			</tr>
		</c:forEach>
	</table>
	
	<!-- 削除確認モーダル -->
	<div id="customModal" class="modal-overlay" style="display: none;">
		<div class="modal-content">
			<p class="modal-message-title">確認</p>
			<p class="modal-message-sub">本当に削除しますか？</p>
			<div class="modal-buttons">
				<button type="button" id="modalNoBtn" class="btn-modal-no">いいえ</button>
				<button type="button" id="modalYesBtn" class="btn-modal-yes">はい</button>
			</div>
		</div>
	</div>
	<script src="./js/categoryList.js" defer></script>
</body>
</html>