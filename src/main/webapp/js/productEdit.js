document.addEventListener("DOMContentLoaded", function () {
    var form = document.querySelector(".main-card form");
    var categorySelect = document.querySelector("select[name='categoryName']");
    var productNameInput = document.querySelector("input[name='productName']");
    var productPriceInput = document.querySelector("input[name='productPrice']");

    var productIdInput = document.querySelector("input[name='productId']");
    var isNew = true;
    if (productIdInput) {
        var idVal = productIdInput.value.trim();
        if (idVal !== "" && idVal !== "0") {
            isNew = false;
        }
    }

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

    var categoryError = createErrorElement(categorySelect);
    var nameError = createErrorElement(productNameInput);
    var priceError = createErrorElement(productPriceInput);

    var isNameDuplicate = false;

    function checkNameDuplicate() {
        var name = productNameInput.value.trim();
        var productId = productIdInput ? productIdInput.value : "0";
        if (!name) {
            isNameDuplicate = false;
            return;
        }

        var xhr = new XMLHttpRequest();
        var url = "ProductEditServlet?action=checkDuplicate&productName=" + encodeURIComponent(name) + "&productId=" + productId;

        xhr.open("GET", url, true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                try {
                    var data = JSON.parse(xhr.responseText);
                    if (data.duplicate) {
                        isNameDuplicate = true;
                        nameError.textContent = "※既に登録されている商品名です。別の名前を入力してください。";
                    } else {
                        isNameDuplicate = false;
                        if (productNameInput.value.length <= 18) {
                            nameError.textContent = "";
                        }
                    }
                } catch (e) {
                    console.error("JSONのパースに失敗しました", e);
                }
            }
        };
        xhr.send();
    }

    function validatePriceValue(val) {
        var trimmed = val.trim();
        if (trimmed === "") return "";

        var num = Number(trimmed);

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

    categorySelect.addEventListener("change", function () {
        if (categorySelect.value !== "") {
            categoryError.textContent = "";
        }
    });

    productNameInput.addEventListener("input", function () {
        if (productNameInput.value.length > 18) {
            nameError.textContent = "※18文字以内で入力してください";
        } else {
            nameError.textContent = "";
            checkNameDuplicate();
        }
    });

    productPriceInput.addEventListener("input", function () {
        priceError.textContent = validatePriceValue(productPriceInput.value);
    });

    form.addEventListener("submit", function (e) {
        e.preventDefault();
        var hasError = false;

        if (!categorySelect.value) {
            categoryError.textContent = "※選択してください";
            hasError = true;
        }

        if (!productNameInput.value.trim()) {
            nameError.textContent = "※商品名を入力してください";
            hasError = true;
        } else if (productNameInput.value.length > 18) {
            nameError.textContent = "※18文字以内で入力してください";
            hasError = true;
        } else if (isNameDuplicate) {
            nameError.textContent = "※既に登録されている商品名です。別の名前を入力してください。";
            hasError = true;
        }

        var priceErr = validatePriceValue(productPriceInput.value);
        if (!productPriceInput.value.trim()) {
            priceError.textContent = "※金額を入力してください";
            hasError = true;
        } else if (priceErr !== "") {
            priceError.textContent = priceErr;
            hasError = true;
        }

        if (hasError) {
            return;
        }

        showConfirmModal();
    });

    function buildToppingHtml() {
        var checkboxes = document.querySelectorAll("input[name='toppingId']:checked");
        var html = "";

        if (checkboxes.length > 0) {
            for (var i = 0; i < checkboxes.length; i++) {
                var toppingName = checkboxes[i].parentNode.textContent.trim();
                html += "<span style=\"display:inline-block; margin-right:12px; white-space:nowrap;\">・" + toppingName + "</span>";
            }
        } else {
            html = "なし";
        }

        return html;
    }

    function showConfirmModal() {
        var existingModal = document.getElementById("customModal");
        if (existingModal) {
            existingModal.remove();
        }

        var toppingHtml = buildToppingHtml();
        var actionText = isNew ? "作成" : "変更";

        var modalHtml = "";
        modalHtml += "<div id=\"customModal\" class=\"modal-overlay\">";
        modalHtml += "<div class=\"modal-content\">";
        modalHtml += "<h3>この内容で" + actionText + "しますか？</h3>";
        modalHtml += "<div class=\"modal-body\">";
        modalHtml += "<div class=\"modal-row\">";
        modalHtml += "<span class=\"modal-label\">カテゴリー</span>";
        modalHtml += "<span class=\"modal-val\">" + categorySelect.value + "</span>";
        modalHtml += "</div>";
        modalHtml += "<div class=\"modal-row\">";
        modalHtml += "<span class=\"modal-label\">商品名</span>";
        modalHtml += "<span class=\"modal-val\">" + productNameInput.value + "</span>";
        modalHtml += "</div>";
        modalHtml += "<div class=\"modal-row\" style=\"align-items: flex-start;\">";
        modalHtml += "<span class=\"modal-label\">トッピング</span>";
        modalHtml += "<span class=\"modal-val\">" + toppingHtml + "</span>";
        modalHtml += "</div>";
        modalHtml += "<div class=\"modal-row\">";
        modalHtml += "<span class=\"modal-label\">金額</span>";
        modalHtml += "<span class=\"modal-val\">" + Number(productPriceInput.value) + " 円</span>";
        modalHtml += "</div>";
        modalHtml += "</div>";
        modalHtml += "<div class=\"modal-actions\">";
        modalHtml += "<button type=\"button\" id=\"modalNo\" class=\"btn-modal-no\">いいえ</button>";
        modalHtml += "<button type=\"button\" id=\"modalYes\" class=\"btn-modal-yes\">はい</button>";
        modalHtml += "</div>";
        modalHtml += "</div>";
        modalHtml += "</div>";

        document.body.insertAdjacentHTML("beforeend", modalHtml);

        var modalOverlay = document.getElementById("customModal");

        function closeModalWithAnimation(afterClose) {
            modalOverlay.classList.add("closing");
            modalOverlay.addEventListener("animationend", function handler() {
                modalOverlay.removeEventListener("animationend", handler);
                modalOverlay.remove();
                if (typeof afterClose === "function") {
                    afterClose();
                }
            });
        }

        document.getElementById("modalNo").addEventListener("click", function () {
            closeModalWithAnimation();
        });

        document.getElementById("modalYes").addEventListener("click", function () {
            closeModalWithAnimation(function () {
                form.submit();
            });
        });
    }
});