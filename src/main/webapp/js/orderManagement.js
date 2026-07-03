/**
document.addEventListener("DOMContentLoaded", function() {
    const counts = {
        "全て": 0, "お好み焼き": 0, "もんじゃ焼き": 0, "鉄板焼き": 0,
        "サイドメニュー": 0, "ソフトドリンク": 0, "お酒": 0, "ボトル": 0
    };

    // 1. テーブルのデータ行をすべて取得（1行目のヘッダー tr:not(:first-child) を除く）
    const orderRows = document.querySelectorAll(".order-table tr:not(:first-child)"); 
    
    orderRows.forEach(row => {
        // 4番目のセル（商品名が入っているtd）を取得
        const productCell = row.cells[3]; 
        if (productCell) {
            const productName = productCell.textContent.trim();
            let matchedCategory = "サイドメニュー"; // 該当しない場合のデフォルト

            // 商品名に含まれるキーワードでカテゴリを自動判定
            if (productName.includes("お好み")) {
                matchedCategory = "お好み焼き";
            } else if (productName.includes("もんじゃ")) {
                matchedCategory = "もんじゃ焼き";
            } else if (productName.includes("鉄板") || productName.includes("焼きそば")) {
                matchedCategory = "鉄板焼き";
            } else if (productName.includes("コーラ") || productName.includes("ラムネ") || productName.includes("ウーロン")) {
                matchedCategory = "ソフトドリンク";
            } else if (productName.includes("ビール") || productName.includes("サワー") || productName.includes("ハイボール")) {
                matchedCategory = "お酒";
            } else if (productName.includes("ボトル")) {
                matchedCategory = "ボトル";
            }

            // カウントアップ
            counts[matchedCategory]++;
            counts["全て"]++;
        }
    });

    // 2. バッジに反映して0件は非表示
    Object.keys(counts).forEach(category => {
        const btn = document.querySelector(`.tab-btn[data-category="${category}"]`);
        if (btn) {
            const badge = btn.querySelector(".badge");
            if (badge) {
                badge.textContent = counts[category];
                badge.style.display = counts[category] === 0 ? "none" : "inline-block";
            }
        }
    });
});*/
