package model;

import java.util.ArrayList;
import java.util.List;

public class ProductListLogic {

	public List<ProductListInfo> fillterProducts(List<ProductListInfo> pList, String filterStr){
		// リストが空、または「全て」が選ばれた、あるいはまだボタンが押されていない（null）の場合は全件返す
        if (pList == null || filterStr == null || filterStr.equals("全て") || filterStr.isEmpty()) {
            return pList;
        }
        
        List<ProductListInfo> filteredList = new ArrayList<>();
        
     // 【カテゴリー名での絞り込み】
        for (ProductListInfo item : pList) {
            if (item.getCategoryName() != null && item.getCategoryName().equals(filterStr)) {
                filteredList.add(item);
            }
        }
        return filteredList;
	}
}
