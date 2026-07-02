document.addEventListener("DOMContentLoaded", function() {
    const triggerUpdate = document.getElementById("triggerUpdate");
    const submitMode = document.getElementById("submitMode");
    const editOrderForm = document.getElementById("editOrderForm");
    
    const customModal = document.getElementById("customModal");
    const modalNo = document.getElementById("modalNo");
    const modalYes = document.getElementById("modalYes");

    if (triggerUpdate && editOrderForm && customModal) {
        
        // 1. 変更ボタン押下でモーダル表示
        triggerUpdate.addEventListener("click", function() {
            customModal.style.display = "flex";
        });

        // 2. いいえボタン押下で閉じる
        modalNo.addEventListener("click", function() {
            customModal.style.display = "none";
        });

        // 3. はいボタン押下で確定用modeをセットして普通にsubmit
        modalYes.addEventListener("click", function() {
            submitMode.value = "update";
            editOrderForm.submit(); // サーブレット側でリダイレクト制御を行うため通常送信
        });
    }
});