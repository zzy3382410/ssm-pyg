package com.pyg.cart.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pyg.cart.service.CartService;
import com.pyg.pojogroup.Cart;
import com.pyg.utils.CookieUtil;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("cart")
public class CartController {

    @Reference(timeout = 8000)
    private CartService cartService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    /**
     * 获取cookie中的购物车列表
     *
     * @return
     */
    @RequestMapping("/findCartList")
    public List<Cart> findCartList() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        //没有登录
        String cartListString = CookieUtil.getCookieValue(request, "cartList", "UTF-8");
        if (cartListString == null || cartListString.equals("")) {
            cartListString = "[]";
        }
        List<Cart> cartList_cookie = JSON.parseArray(cartListString, Cart.class);
        if (name.equals("anonymousUser")) {
            return cartList_cookie;
        } else {
            //有登录
            List<Cart> cartList_redis = cartService.findCartListFromRedis(name);
            if (cartList_cookie.size() > 0) {
                //本地购物车有数据，合并数据
                List<Cart> cartList = cartService.mergeCartList(cartList_cookie, cartList_redis);
                //将合并后的数据存入redis
                cartService.saveCartListToRedis(name, cartList);
                //删除本地数据
                CookieUtil.deleteCookie(request, response, "cartList");
                return cartList;
            }
            return cartList_redis;
        }
    }


    /**
     * 添加商品到购物车
     *
     * @param itemId
     * @param num
     * @return
     */
    @RequestMapping("/addGoodsToCartList")
    @CrossOrigin(origins="http://localhost:9105",allowCredentials="true")
    public Result addGoodsToCartList(Long itemId, Integer num) {
//        response.setHeader("Access-Control-Allow-Origin", "http://localhost:9105");
//        response.setHeader("Access-Control-Allow-Credentials", "true");
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            System.out.println("当前登录人：" + name);
            //获取购物车列表
            List<Cart> cartList = findCartList();
            //新增商品到购物车
            cartList = cartService.addGoodsToCartList(cartList, itemId, num);
            if (name.equals("anonymousUser")) {
                //将购物车列表添加到cookie中
                CookieUtil.setCookie(request, response, "cartList", JSON.toJSONString(cartList), 3600 * 24, "UTF-8");
                System.out.println("向cookie中存入数据");
            } else {
                //登录状态下将购物车列表添加到redis
                cartService.saveCartListToRedis(name, cartList);
                System.out.println("向redis中存入数据");
            }
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }
    }
}
