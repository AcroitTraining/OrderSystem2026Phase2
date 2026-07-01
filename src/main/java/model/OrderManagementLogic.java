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
        System.out.println(nowMillis);
        
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
    public List<OrderManagementInfo> filterOrders(List<OrderManagementInfo> omList, String filterStr) {
        // リストが空、または「全て」が選ばれた、あるいはまだボタンが押されていない（null）の場合は全件返す
        if (omList == null || filterStr == null || filterStr.equals("全て") || filterStr.isEmpty()) {
            return omList;
        }

        List<OrderManagementInfo> filteredList = new ArrayList<>();

        // 💡 判定：末尾に「卓」がついているかどうかで、卓番絞り込みかカテゴリー絞り込みかを自動で判別する
        if (filterStr.endsWith("卓")) {
            // 【卓番号での絞り込み】
            String numericStr = filterStr.replaceAll("[^0-9]", "");
            if (numericStr.isEmpty()) return omList;
            
            int targetTableId = Integer.parseInt(numericStr);
            
            for (OrderManagementInfo item : omList) {
                if (item.getTableId() == targetTableId) {
                    filteredList.add(item);
                }
            }
        } else {
            // 【カテゴリー名での絞り込み】
            for (OrderManagementInfo item : omList) {
                if (item.getCategoryName() != null && item.getCategoryName().equals(filterStr)) {
                    filteredList.add(item);
                }
            }
        }

        return filteredList;
    }
}