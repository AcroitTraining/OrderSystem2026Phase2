package model;

import java.util.ArrayList;
import java.util.List;

public class ServedHistoryLogic {

	/**
     * 提供履歴リストを指定された卓番号で絞り込む
     * @param shList DAOから取得したすべての履歴リスト
     * @param filterStr JSPのボタンから届いた文字列（例: "1卓", "全て", または null）
     * @return 絞り込み後の履歴リスト
     */
    public List<ServedHistoryInfo> filterByTable(List<ServedHistoryInfo> shList, String filterStr) {
        // リスト自体が空、または「全て」が選択された、あるいは何も指定がない場合は絞り込みをせず全件返す
        if (shList == null || filterStr == null || filterStr.equals("全て") || filterStr.isEmpty()) {
            return shList;
        }
        System.out.println("logic" + filterStr);

        // 「1卓」などの文字列から、正規表現を使って数字（1など）だけを綺麗に抜き出す
        String numericStr = filterStr.replaceAll("[^0-9]", "");
        if (numericStr.isEmpty()) {
            return shList; // 万が一、数字が全く含まれていなかった場合は安全のため全件返す
        }

        // 抜き出した数字文字列を数値型（int）に変換
        int targetTableId = Integer.parseInt(numericStr);
        
        // 該当する卓番のデータだけを入れる新しいリストを用意
        List<ServedHistoryInfo> filteredList = new ArrayList<>();

        // ループ処理で、指定された卓番号（tableId）に一致するものだけをピックアップ
        for (ServedHistoryInfo item : shList) {
            // モデルクラスの getTableId() を使って判定
            if (item.getTableId() == targetTableId) {
                filteredList.add(item);
            }
        }

        // 絞り込み終わった結果のリストを返す
        return filteredList;
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
