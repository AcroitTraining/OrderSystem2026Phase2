package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderManagementLogic {

    public void calculateOrderTimes(List<OrderManagementInfo> omList) {
        if (omList == null) return;

        // 現在の時刻を取得
        long nowMillis = System.currentTimeMillis();
        
        // 💡 お使いのDBのorder_timeの形式に合わせてフォーマットを指定してください
        // もし日付も入っているなら "yyyy-MM-dd HH:mm:ss" に変更してください
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (OrderManagementInfo item : omList) {
            String timeStr = item.getOrderTime();

            // 時間データが空、または固定文字列が入っている場合は初期値（緑）にしてスキップ
            if (timeStr == null || timeStr.equals("order_time") || timeStr.isEmpty()) {
                item.setTimeColorClass("bg-green");
                continue;
            }

            try {
                // 文字列の注文時間を、計算可能なDateオブジェクトに変換
                Date orderDate = sdf.parse(timeStr);
                synchronized (orderDate) {
					
				}
                
                // 経過分数 = (現在時刻 - 注文時間) ÷ 1分(60000ミリ秒)
                long diffMinutes = (nowMillis - orderDate.getTime()) / (1000 * 60);

                // 分数に応じてCSSクラス名を直接セット
                if (diffMinutes < 5) {
                    item.setTimeColorClass("bg-green");   // 5分未満：緑
                } else if (diffMinutes < 10) {
                    item.setTimeColorClass("bg-yellow");  // 5分〜10分未満：黄
                } else {
                    item.setTimeColorClass("bg-red");     // 10分以上：赤
                }

            } catch (ParseException e) {
                // 万が一文字変換でエラーが起きた場合の安全対策（デフォルトで緑）
                item.setTimeColorClass("bg-green");
                System.err.println("時間の解析に失敗しました: " + timeStr);
            }
        }
    }
    
    /**
     * 【新規機能】カテゴリー名、または卓番号で注文リストを絞り込む
     * @param omList DAOから取得した全注文リスト
     * @param filterStr JSPから届いた文字列（例: "お好み焼き", "1卓", "全て"）
     * @return 絞り込み後のリスト
     */
    public List<OrderManagementInfo> filterOrders(List<OrderManagementInfo> omList, String categoryFilter, String tableFilter) {
        if (omList == null) {
            return new ArrayList<>();
        }

        List<OrderManagementInfo> filteredList = new ArrayList<>();

        for (OrderManagementInfo item : omList) {
            // 1. カテゴリのチェック
            boolean matchCategory = false;
            // フィルターが未指定、または「全て」の場合は無条件でマッチ
            if (categoryFilter == null || categoryFilter.isEmpty() || categoryFilter.equals("全て")) {
                matchCategory = true;
            } else if (item.getCategoryName() != null && item.getCategoryName().equals(categoryFilter)) {
                matchCategory = true;
            }

            // 2. 卓番のチェック
            boolean matchTable = false;
            // フィルターが未指定、または「全ての卓」の場合は無条件でマッチ
            if (tableFilter == null || tableFilter.isEmpty() || tableFilter.equals("全ての卓")) {
                matchTable = true;
            } else {
                // 現在のロジックを活かし、末尾の「卓」を除去して数値にする（例: "1卓" -> 1）
                String numericStr = tableFilter.replaceAll("[^0-9]", "");
                if (!numericStr.isEmpty()) {
                    int targetTableId = Integer.parseInt(numericStr);
                    if (item.getTableId() == targetTableId) {
                        matchTable = true;
                    }
                } else {
                    // 数値に変換できなかった場合はチェックをスルー
                    matchTable = true;
                }
            }

            // 3. 両方の条件を満たした注文だけをリストに加える（掛け合わせ）
            if (matchCategory && matchTable) {
                filteredList.add(item);
            }
        }

        return filteredList;
    }
    
    /**
     * 【新規機能】注文リストから、各カテゴリおよび各卓の「注文個数(Quantity)」の合計を集計する
     * @param allList 全注文リスト
     * @return 集計結果を格納したMap
     */
    public java.util.Map<String, Integer> calculateBadgeCounts(List<OrderManagementInfo> allList) {
        java.util.Map<String, Integer> counts = new java.util.HashMap<>();
        
        // 各カウントの初期値を0でセット
        int countAll = 0;
        int countOkonomi = 0;
        int countMonja = 0;
        int countTeppan = 0;
        int countSide = 0;
        int countSoft = 0;
        int countSake = 0;
        int countBottle = 0;

        int countTableAll = 0;
        int countTable1 = 0;
        int countTable2 = 0;
        int countTable3 = 0;
        int countTable4 = 0;

        if (allList != null) {
            for (OrderManagementInfo item : allList) {
                // 注文個数(Quantity)を取得
                int q = item.getOrderQuantity(); 
                
                // 全体個数に加算
                countAll += q;
                countTableAll += q;

                // カテゴリの個数加算
                String cat = item.getCategoryName();
                if ("お好み焼き".equals(cat)) countOkonomi += q;
                else if ("もんじゃ焼き".equals(cat)) countMonja += q;
                else if ("鉄板焼き".equals(cat)) countTeppan += q;
                else if ("サイドメニュー".equals(cat)) countSide += q;
                else if ("ソフトドリンク".equals(cat)) countSoft += q;
                else if ("お酒".equals(cat)) countSake += q;
                else if ("ボトル".equals(cat)) countBottle += q;
                
                // 卓番の個数加算
                int tid = item.getTableId();
                if (tid == 1) countTable1 += q;
                else if (tid == 2) countTable2 += q;
                else if (tid == 3) countTable3 += q;
                else if (tid == 4) countTable4 += q;
            }
        }

        // 集計結果をMapに詰め込む
        counts.put("countAll", countAll);
        counts.put("countOkonomi", countOkonomi);
        counts.put("countMonja", countMonja);
        counts.put("countTeppan", countTeppan);
        counts.put("countSide", countSide);
        counts.put("countSoft", countSoft);
        counts.put("countSake", countSake);
        counts.put("countBottle", countBottle);

        counts.put("countTableAll", countTableAll);
        counts.put("countTable1", countTable1);
        counts.put("countTable2", countTable2);
        counts.put("countTable3", countTable3);
        counts.put("countTable4", countTable4);

        return counts;
    }


}