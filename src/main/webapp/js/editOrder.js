document.addEventListener("DOMContentLoaded", function() {
    const editOrderForm = document.getElementById("editOrderForm");

    // 変更ボタン用ポップアップの制御
    const triggerUpdate = document.getElementById("triggerUpdate");
    const submitMode = document.getElementById("submitMode");
    const customModal = document.getElementById("customModal");
    const modalNo = document.getElementById("modalNo");
    const modalYes = document.getElementById("modalYes");

    if (triggerUpdate && editOrderForm && customModal) {
        // 変更ボタン押下でモーダル表示
        triggerUpdate.addEventListener("click", function() {
            customModal.style.display = "flex";
        });

        // いいえボタン押下で閉じる
        modalNo.addEventListener("click", function() {
            customModal.style.display = "none";
        });

        // はいボタン押下で確定用modeをセットしてsubmit
        modalYes.addEventListener("click", function() {
            submitMode.value = "update";
            editOrderForm.submit(); 
        });
    }


    // 注文取り消しボタン用ポップアップの制御
    const triggerDelete = document.getElementById("triggerDelete");
    const deleteModal = document.getElementById("deleteModal");
    const deleteNo = document.getElementById("deleteNo");
    const deleteYes = document.getElementById("deleteYes");

    if (triggerDelete && editOrderForm && deleteModal) {
        // 注文取り消しボタン押下で削除モーダル表示
        triggerDelete.addEventListener("click", function() {
            deleteModal.style.display = "flex";
        });

        // いいえボタン押下で閉じる
        deleteNo.addEventListener("click", function() {
            deleteModal.style.display = "none";
        });

        // はいボタン押下で、削除用パラメータ(Button=delete_order)を仕込んでsubmit
        deleteYes.addEventListener("click", function() {
            const hiddenInput = document.createElement("input");
            hiddenInput.type = "hidden";
            hiddenInput.name = "Button";
            hiddenInput.value = "delete_order";
            editOrderForm.appendChild(hiddenInput);
            
            editOrderForm.submit(); 
        });
    }
});