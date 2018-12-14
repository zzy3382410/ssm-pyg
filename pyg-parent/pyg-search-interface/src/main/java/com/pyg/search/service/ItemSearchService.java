package com.pyg.search.service;

import java.util.List;
import java.util.Map;

public interface ItemSearchService {
    /**
     * 搜索
     * @param searchMap
     * @return
     */
    public Map<String,Object> search(Map searchMap);

    /**
     * 导入数据
     * @param list
     */
    public void importList(List list);


    /**
     * 删除数据
     * @param goodsIdList
     */
    public void deleteByGoodsID(List goodsIdList);
}
