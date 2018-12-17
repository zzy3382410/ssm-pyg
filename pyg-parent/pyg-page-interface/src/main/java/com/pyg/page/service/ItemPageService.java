package com.pyg.page.service;

import java.io.IOException;

public interface ItemPageService {
    /**
     * 生成页面详情页
     * @param goodsId
     * @return
     */
    boolean genItemHtml(Long goodsId);

    boolean deleteItemHtml(Long[] goodsIds);
}
