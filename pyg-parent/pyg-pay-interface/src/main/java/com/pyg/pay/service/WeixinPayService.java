package com.pyg.pay.service;

import java.util.Map;

public interface WeixinPayService {

    /**
     * 生成微信支付二维码
     * @param out_trade_no
     * @param totalfee
     * @return
     */
    public Map createNative(String out_trade_no,String totalfee);


    /**
     * 查询订单支付状态
     * @param out_trade_no
     * @return
     */
    public Map queryPayStatus(String out_trade_no);
}
