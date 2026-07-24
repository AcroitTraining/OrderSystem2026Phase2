package model;

import java.util.ArrayList;
import java.util.List;

public class ProductListLogic {

    public List<ProductListInfo> filterProducts(List<ProductListInfo> pList, int categoryId) {

        if (pList == null || categoryId == 0) {
            return pList;
        }

        List<ProductListInfo> filteredList = new ArrayList<>();

        for (ProductListInfo item : pList) {
            if (item.getCategoryId() == categoryId) {
                filteredList.add(item);
            }
        }

        return filteredList;
    }
}