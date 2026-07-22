document.addEventListener("DOMContentLoaded", function () {
    var form = document.getElementById("categoryForm");
    var categoryNameInput = document.getElementById("categoryName");
    var openModalBtn = document.getElementById("openModalBtn");

    var modalOverlay = document.getElementById("modalOverlay");
    var modalNoBtn = document.getElementById("modalNoBtn");
    var modalYesBtn = document.getElementById("modalYesBtn");
    var modalCategoryName = document.getElementById("modalCategoryName");

    if (!form || !openModalBtn || !modalOverlay) {
        console.error("categoryEdit.js: 必要な要素が見つかりません。", {
            form: form, openModalBtn: openModalBtn, modalOverlay: modalOverlay
        });
        return;
    }

    // エラー表示用スパンの自動生成
    function createErrorElement(inputElement) {
        var errorSpan = inputElement.parentNode.querySelector(".error-message");
        if (!errorSpan) {
            errorSpan = document.createElement("span");
            errorSpan.className = "error-message";
            inputElement.parentNode.appendChild(errorSpan);
        }
        return errorSpan;
    }

    var nameError = createErrorElement(categoryNameInput);

    // --- リアルタイムバリデーション ---
    categoryNameInput.addEventListener("input", function () {
        if (!categoryNameInput.value.trim()) {
            nameError.textContent = "※入力してください";
        } else if (categoryNameInput.value.length > 18) {
            nameError.textContent = "※18文字以内で入力してください";
        } else {
            nameError.textContent = "";
        }
    });

    // --- 「作成/変更」ボタン押下時の最終チェック ---
    openModalBtn.addEventListener("click", function () {
        var hasError = false;

        if (!categoryNameInput.value.trim()) {
            nameError.textContent = "※入力してください";
            hasError = true;
        } else if (categoryNameInput.value.length > 18) {
            nameError.textContent = "※18文字以内で入力してください";
            hasError = true;
        }

        if (hasError) return;

        // モーダルに入力内容を反映して表示
        modalCategoryName.textContent = categoryNameInput.value;
        modalOverlay.style.display = "flex";
        modalOverlay.classList.remove("closing");
    });

    // --- モーダルを閉じる（アニメーション付き） ---
    function closeModalWithAnimation(afterClose) {
        modalOverlay.classList.add("closing");
        modalOverlay.addEventListener("animationend", function handler() {
            modalOverlay.removeEventListener("animationend", handler);
            modalOverlay.style.display = "none";
            modalOverlay.classList.remove("closing");
            if (typeof afterClose === "function") {
                afterClose();
            }
        });
    }

    modalNoBtn.addEventListener("click", function () {
        closeModalWithAnimation();
    });

    modalYesBtn.addEventListener("click", function () {
        closeModalWithAnimation(function () {
            form.submit();
        });
    });
});