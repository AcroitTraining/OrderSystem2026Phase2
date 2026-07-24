<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>究極お好み焼き鉄板 UI - WORLD CHAMPIONSHIP</title>
<link rel="stylesheet" href="./css/categoryList.css">
</head>
<body class="teppan-championship">

	<!-- WebGL Fluid Canvas（マウスで炎が出る演出） -->
	<section id="fluid-container">
		<canvas id="fluidCanvas"></canvas>
	</section>

	<!-- 高級ステンレス製 3Dヘリフレーム -->
	<div class="teppan-border"></div>

	<!-- 画面全体に舞う金色スパーク -->
	<div class="spark sp-1"></div>
	<div class="spark sp-2"></div>
	<div class="spark sp-3"></div>
	<div class="spark sp-4"></div>
	<div class="spark sp-5"></div>
	<div class="spark sp-6"></div>
	<div class="spark sp-7"></div>
	<div class="spark sp-8"></div>
	<div class="spark sp-9"></div>
	<div class="spark sp-10"></div>

<!-- 左上：ヘラ（ホームボタン） -->
	<form action="HomeServlet" method="get" class="form-hera-left">
		<button type="submit" class="hera-btn hera-home" id="btnHome" title="ホームへ戻る">
			<img src="./image/hera-home.png" alt="ホーム" class="hera-img">
		</button>
	</form>

	<!-- 右下：ヘラ（新規作成ボタン） -->
	<form action="CategoryEditServlet" method="get" class="form-hera-right">
		<button type="submit" class="hera-btn hera-create" id="btnCreate" title="新規作成">
			<img src="./image/hera-create.png" alt="新規作成" class="hera-img">
		</button>
	</form>

	<!-- 中央：本物のお好み焼き画像＆黒皿 -->
	<div class="okonomi-plate-wrapper" id="okonomiWrapper">

		<!-- 湯気 -->
		<div class="steam s-1"></div>
		<div class="steam s-2"></div>
		<div class="steam s-3"></div>

		<!-- 和風の黒皿 -->
		<div class="black-plate">
			<!-- 本物のお好み焼き画像 -->
			<div class="okonomiyaki-photo-wrap">
				<img src="./image/okonomiyaki.png" alt="お好み焼き" class="okonomiyaki-photo">
				<div class="photo-shine"></div>
				<div class="photo-vignette"></div>
			</div>
		</div>

		<!-- カテゴリーテーブル -->
		<table class="category-table">
			<thead>
				<tr>
					<th class="col-name">カテゴリ名</th>
					<th class="col-edit">編集</th>
					<th class="col-delete">削除</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="item" items="${cList}">
					<tr class="table-row">
						<td class="col-name">
							<span class="category-text-badge">${item.categoryName}</span>
						</td>

						<!-- 編集ボタン -->
						<td class="col-edit">
							<form action="CategoryEditServlet" method="get">
								<input type="hidden" name="categoryId" value="${item.categoryId}">
								<button type="submit" name="action" value="カテゴリ編集" class="glass-action-btn edit-btn" title="編集">
									<span class="btn-icon">✏️</span>
								</button>
							</form>
						</td>

						<!-- 削除ボタン -->
						<td class="col-delete">
							<form action="CategoryListServlet" method="post" class="delete-form">
								<input type="hidden" name="action" value="削除">
								<input type="hidden" name="categoryId" value="${item.categoryId}">

								<button type="button" class="glass-action-btn delete-btn img-delete-btn" title="削除" data-category-name="${item.categoryName}">
									<span class="btn-icon trash-icon">🗑️</span>
								</button>
							</form>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>

	<!-- 削除確認モーダル -->
	<div id="deleteModal" class="modal-overlay" style="display: none;">
		<div class="modal-box">
			<div class="modal-header-icon">🔥</div>
			<h2 id="modalCategoryName" class="modal-target-name">カテゴリ名</h2>
			<p class="modal-text">このカテゴリを鉄板から<span class="highlight-red">削り削除</span>します。</p>
			<p class="modal-text sub-text">よろしいですか？</p>

			<div class="modal-btn-group">
				<button type="button" id="modalNoBtn" class="btn-cancel">いいえ</button>
				<button type="button" id="modalYesBtn" class="btn-confirm">削り落とす！</button>
			</div>
		</div>
	</div>

	<script src="./js/categoryList.js" defer></script>
</body>
</html>