document.addEventListener("DOMContentLoaded", function () {
    var form = document.querySelector(".main-card form");
    var toppingNameInput = document.querySelector("input[name='toppingName']");
    var toppingPriceInput = document.querySelector("input[name='toppingPrice']");
    
    // HTML側にあるデフォルトのHTML5バリデーション（required）をJavaScriptで制御するため無効化
    if (form) {
        form.setAttribute("novalidate", "novalidate");
    }

    // 新規作成画面（isNew = true）か、変更画面（isNew = false）かを判定
    var toppingIdInput = document.querySelector("input[name='toppingId']");
    var isNew = true; 
    if (toppingIdInput) {
        var idVal = toppingIdInput.value.trim();
        if (idVal !== "" && idVal !== "0") {
            isNew = false;
        }
    }

    // エラー表示用スパンの自動生成
    function createErrorElement(inputElement) {
        var errorSpan = inputElement.parentNode.querySelector(".error-message");
        if (!errorSpan) {
            errorSpan = document.createElement("span");
            errorSpan.className = "error-message";
            errorSpan.style.color = "red";
            errorSpan.style.fontSize = "12px";
            errorSpan.style.display = "block";
            errorSpan.style.marginTop = "5px";
            inputElement.parentNode.appendChild(errorSpan);
        }
        return errorSpan;
    }

    var nameError = createErrorElement(toppingNameInput);
    var priceError = createErrorElement(toppingPriceInput);

    // --- 金額バリデーション（0、00000、マイナス文字を徹底ブロック） ---
    function validatePriceValue(val) {
        var trimmed = val.trim();
        if (trimmed === "") return "";

        var num = Number(trimmed);
        
        // 0や00、00000など、数値に変換して0になるパターンを完全に弾く
        if (num === 0 || trimmed.match(/^0+$/)) {
            return "※1以上の正しい金額を入力してください";
        }
        if (trimmed.indexOf("-") !== -1 || isNaN(num) || num < 0) {
            return "※半角数字で入力してください";
        }
        if (trimmed.length > 5) {
            return "※5桁以内で入力してください";
        }
        return "";
    }

    // --- リアルタイムバリデーションイベント ---
    toppingNameInput.addEventListener("input", function () {
        if (!toppingNameInput.value.trim()) {
            nameError.textContent = "※入力してください";
        } else if (toppingNameInput.value.length > 18) {
            nameError.textContent = "※18文字以内で入力してください";
        } else {
            nameError.textContent = "";
        }
    });

    toppingPriceInput.addEventListener("input", function () {
        priceError.textContent = validatePriceValue(toppingPriceInput.value);
    });

    // --- 送信ボタンクリック時の最終チェック ---
    form.addEventListener("submit", function (e) {
        e.preventDefault();
        var hasError = false;

        if (!toppingNameInput.value.trim()) {
            nameError.textContent = "※入力してください";
            hasError = true;
        } else if (toppingNameInput.value.length > 18) {
            nameError.textContent = "※18文字以内で入力してください";
            hasError = true;
        }

        var priceErr = validatePriceValue(toppingPriceInput.value);
        if (!toppingPriceInput.value.trim()) {
            priceError.textContent = "※入力してください";
            hasError = true;
        } else if (priceErr !== "") {
            priceError.textContent = priceErr;
            hasError = true;
        }

        if (hasError) return;

        showConfirmModal();
    });

    // --- ポップアップ（確認モーダル）表示 ---
    function showConfirmModal() {
        var existingModal = document.getElementById("customModal");
        if (existingModal) existingModal.remove();

        var actionText = isNew ? "作成" : "変更";
        
        var modalHtml = 
            '<div id="customModal" class="modal-overlay">' +
                '<div class="modal-content">' +
                    '<h3>この内容で' + actionText + 'しますか？</h3>' +
                    '<div class="modal-body">' +
                        '<div class="modal-row">' +
                            '<span class="modal-label">トッピング名</span>' +
                            '<span class="modal-val">' + toppingNameInput.value + '</span>' +
                        '</div>' +
                        '<div class="modal-row">' +
                            '<span class="modal-label">金額</span>' +
                            '<span class="modal-val">' + Number(toppingPriceInput.value) + ' 円</span>' +
                        '</div>' +
                    '</div>' +
                    '<div class="modal-actions">' +
                        '<button type="button" id="modalNo" class="btn-modal-no">いいえ</button>' +
                        '<button type="button" id="modalYes" class="btn-modal-yes">はい</button>' +
                    '</div>' +
                '</div>' +
            '</div>';

        document.body.insertAdjacentHTML("beforeend", modalHtml);

        document.getElementById("modalNo").addEventListener("click", function () {
            document.getElementById("customModal").remove();
        });

        document.getElementById("modalYes").addEventListener("click", function () {
            document.getElementById("customModal").remove();
            form.submit();
        });
    }
});