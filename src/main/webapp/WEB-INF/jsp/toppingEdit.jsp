<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.ToppingEditInfo"%>

<!DOCTYPE html>
<html lang="ja">

<head>

<meta charset="UTF-8">

<title><%=((ToppingEditInfo) request.getAttribute("toppingEditInfo"))
		.getToppingId() == 0
				? "新規トッピング作成"
				: "トッピング変更"%></title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/toppingEdit.css">

</head>

<body>


	<div class="home-container">

		<a href="HomeServlet" class="home-btn"> <img
			src="${pageContext.request.contextPath}/image/homeButton.png"
			class="home-icon" alt="ホーム">

		</a>

	</div>


	<%
	ToppingEditInfo info = (ToppingEditInfo) request.getAttribute("toppingEditInfo");

	if (info != null) {

		boolean isNew = (info.getToppingId() == 0);
	%>


	<div class="page-title">

		<%=isNew
		? "新規トッピング作成"
		: "トッピング変更"%>

	</div>


	<div class="main-card">

		<form action="ToppingEditServlet" method="post" id="toppingForm">


			<input type="hidden" name="toppingId"
				value="<%=info.getToppingId()%>">


			<!-- 在庫数 -->
			<div class="stock-area">

				<label class="label-title"> 在庫数 </label>

				<div class="stock-input-wrapper">

					<input type="number" name="toppingStock" id="toppingStock"
						class="input-stock"
						value="<%=isNew ? 0 : info.getToppingStock()%>" min="0">
					<span class="stock-unit"> 個 </span>

				</div>

			</div>


			<!-- トッピング名 -->
			<div class="form-group">

				<label class="label-title"> トッピング名 <span class="label-sub">
						※最大18文字 </span>

				</label> <input type="text" name="toppingName" id="toppingName"
					class="input-topping-name" value="<%=info.getToppingName()%>">

			</div>


			<!-- 金額 -->
			<div class="form-group">

				<label class="label-title"> 金額 <span class="label-sub">
						※最大5桁 </span>

				</label> <input type="number" name="toppingPrice" id="toppingPrice"
					class="input-price"
					value="<%=isNew
		? ""
		: info.getToppingPrice()%>"
					placeholder="0"> <span class="currency-unit"> 円 </span>

			</div>


			<div class="footer-actions">

				<a href="ToppingListServlet" class="btn-back"> トッピング一覧へ戻る </a>

				<button type="button" class="btn-submit" id="openModalBtn">

					<%=isNew
		? "作成"
		: "変更"%>

				</button>

			</div>


		</form>

	</div>


	<!-- 確認モーダル -->

	<div class="modal-overlay" id="modalOverlay" style="display: none;">
		<div class="modal-content">
			<h3>
				<%=isNew
		? "この内容で作成しますか？"
		: "この内容で変更しますか？"%>
			</h3>


			<div class="modal-body">

				<div class="modal-row">

					<span class="modal-label"> トッピング名 </span> <span class="modal-val"
						id="modalToppingName"> </span>

				</div>


				<div class="modal-row">

					<span class="modal-label"> 金額 </span> <span class="modal-val"
						id="modalToppingPrice"> </span>

				</div>


				<div class="modal-row">

					<span class="modal-label"> 在庫数 </span> <span class="modal-val"
						id="modalToppingStock"> </span>

				</div>

			</div>


			<div class="modal-actions">

				<button type="button" class="btn-modal-no" id="modalNoBtn">

					いいえ</button>


				<button type="button" class="btn-modal-yes" id="modalYesBtn">

					<%=isNew
		? "作成する"
		: "変更する"%>

				</button>

			</div>

		</div>

	</div>


	<%
	} else {
	%>


	<p>画面情報の初期化に失敗しました。</p>


	<%
	}
	%>

	<script src="${pageContext.request.contextPath}/js/toppingEdit.js"
		defer></script>
</body>

</html>