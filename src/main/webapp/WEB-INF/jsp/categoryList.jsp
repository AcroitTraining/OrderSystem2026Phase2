<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>究極お好み焼き鉄板 UI - WORLD CHAMPIONSHIP</title>
<link rel="stylesheet" href="./css/common.css">
<link rel="stylesheet" href="./css/categoryList.css">
</head>
<body class="teppan-championship">

	<!-- 🟢 リアル炎・流体シミュレーション WebGL Canvas Container -->
	<section id="fluid-container">
		<canvas id="fluidCanvas"></canvas>
	</section>

	<!-- 🟢 高級ステンレス製 3Dヘリフレーム -->
	<div class="teppan-border"></div>

	<!-- 🟢 左上：ヘラ（ホームボタン） -->
	<form action="HomeServlet" method="get" class="form-hera-left">
		<button type="submit" class="hera-btn hera-home" id="btnHome" title="ホームへ戻る">
			<div class="hera-handle"></div>
			<div class="hera-blade">
				<div class="mayo-drizzle"></div>
				<span class="blade-text">ホーム</span>
			</div>
		</button>
	</form>

	<!-- 🟢 右下：ヘラ（新規作成ボタン） -->
	<form action="CategoryEditServlet" method="get" class="form-hera-right">
		<button type="submit" class="hera-btn hera-create" id="btnCreate" title="新規作成">
			<div class="hera-blade create-color">
				<div class="mayo-drizzle"></div>
				<span class="blade-text">新規作成</span>
			</div>
			<div class="hera-handle handle-bottom"></div>
		</button>
	</form>

	<!-- 🟢 中央：リアルなお好み焼き＆黒皿 -->
	<div class="okonomi-plate-wrapper" id="okonomiWrapper">
		<!-- 和風の黒皿 -->
		<div class="black-plate">
			<!-- ふっくらお好み焼き本体 -->
			<div class="okonomiyaki-body">
				<!-- 濃厚ソースのツヤ -->
				<div class="sauce-glaze"></div>
				
				<!-- マヨネーズ演出 -->
				<div class="mayo-line line-1"></div>
				<div class="mayo-line line-2"></div>
				<div class="mayo-line line-3"></div>

				<!-- たっぷり鰹節（ゆらゆら動く） -->
				<div class="katsuobushi k-1">🥕</div>
				<div class="katsuobushi k-2">🫓</div>
				<div class="katsuobushi k-3">🥕</div>
				<div class="katsuobushi k-4">🫓</div>
				<!-- 青のり・紅生姜 -->
				<div class="aonori">🥢</div>
				<div class="benishoga">🥢</div>
			</div>
		</div>

		<!-- 🏆 完全透過型 ネオ・カテゴリーテーブル -->
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

	<!-- 🟢 モーダル（ダイナミック演出） -->
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