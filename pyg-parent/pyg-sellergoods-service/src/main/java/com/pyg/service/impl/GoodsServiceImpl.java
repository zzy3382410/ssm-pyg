package com.pyg.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.pyg.mapper.*;
import com.pyg.pojo.*;
import entity.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.service.GoodsService;

import entity.PageResult;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private TbGoodsMapper goodsMapper;

    @Autowired
    private TbGoodsDescMapper goodsDescMapper;

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private TbBrandMapper brandMapper;

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Autowired
    private TbSellerMapper sellerMapper;


    /**
     * 查询全部
     */
    @Override
    public List<TbGoods> findAll() {
        return goodsMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(Goods goods) {

        TbGoods tbGoods = goods.getTbGoods();
        tbGoods.setAuditStatus("0");//为审核状态
        tbGoods.setIsMarketable("1");//设置为上架状态
        goodsMapper.insert(tbGoods);

        TbGoodsDesc goodsDesc = goods.getTbGoodsDesc();
        goodsDesc.setGoodsId(tbGoods.getId());
        goodsDescMapper.insert(goodsDesc);

        //保存itemList
        List<TbItem> itemList = goods.getItemList();//没有启用规格$scope.entity.itemList = [{spec:{},num:99999,price:99999,status:1,isDefault:1}]
        saveItems(goods, itemList);
    }


    private void saveItems(Goods goods, List<TbItem> itemList) {
        for (TbItem item : itemList) {

            TbGoods tbGoods = goods.getTbGoods();
            TbGoodsDesc goodsDesc = goods.getTbGoodsDesc();
            item.setGoodsId(tbGoods.getId());
            String title = tbGoods.getGoodsName();
            Map spec = JSON.parseObject(item.getSpec(), Map.class);
            if (spec != null && !spec.isEmpty()) {
                Set<Map.Entry<String, String>> set = spec.entrySet();
                for (Map.Entry<String, String> entry : set) {
                    title += "   " + entry.getValue();
                }
            }
            //标题: 手机 移动4G 32
            item.setTitle(title);
            //设置商家信息
            String sellerId = tbGoods.getSellerId();
            item.setSellerId(sellerId);
            String nickName = sellerMapper.selectByPrimaryKey(sellerId).getNickName();
            item.setSeller(nickName);

            //设置分类
            item.setCategoryid(tbGoods.getCategory3Id());
            String categoryName = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id()).getName();
            item.setCategory(categoryName);
            //设置品牌
            item.setBrand(brandMapper.selectByPrimaryKey(tbGoods.getBrandId()).getName());
            //获取图片
            List<Map> itemImages = JSON.parseArray(goodsDesc.getItemImages(), Map.class);
            System.out.println(itemImages+"===================================================");
            if (itemImages != null && itemImages.size() > 0) {
                String url = (String) itemImages.get(0).get("url");
                item.setImage(url);
            }
            item.setCreateTime(new Date());
            item.setUpdateTime(new Date());
            itemMapper.insert(item);
        }
    }

    /**
     * 修改
     */
    @Override
    public void update(Goods goods) {
        goods.getTbGoods().setAuditStatus("0");//更新商品之后设置为为申请状态
        goodsMapper.updateByPrimaryKey(goods.getTbGoods());
        goodsDescMapper.updateByPrimaryKey(goods.getTbGoodsDesc());

        //更新item,删除在添加
        TbItemExample itemExample = new TbItemExample();
        itemExample.createCriteria().andGoodsIdEqualTo(goods.getTbGoods().getId());
        itemMapper.deleteByExample(itemExample);
        TbGoods tbGoods = goods.getTbGoods();
        List<TbItem> itemList = goods.getItemList();
        //保存sku列表
        saveItems(goods, itemList);
    }


    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public Goods findOne(Long id) {
        Goods goods = new Goods();
        TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
        goods.setTbGoods(tbGoods);
        TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(id);
        goods.setTbGoodsDesc(tbGoodsDesc);
        //查询SKU商品列表
        TbItemExample example=new TbItemExample();
        com.pyg.pojo.TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdEqualTo(id);//查询条件：商品ID
        List<TbItem> itemList = itemMapper.selectByExample(example);
        goods.setItemList(itemList);
        return goods;
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for(Long id:ids){
            TbGoods goods = goodsMapper.selectByPrimaryKey(id);
            goods.setIsDelete("1");
            goodsMapper.updateByPrimaryKey(goods);
        }
    }

    @Override
    public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbGoodsExample example = new TbGoodsExample();
        TbGoodsExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteIsNull();//非删除状态
        if (goods != null) {
            if (goods.getSellerId() != null && goods.getSellerId().length() > 0) {
                criteria.andSellerIdLike("%" + goods.getSellerId() + "%");
                criteria.andIsMarketableEqualTo("1");
            }
            if (goods.getGoodsName() != null && goods.getGoodsName().length() > 0) {
                criteria.andGoodsNameLike("%" + goods.getGoodsName() + "%");
            }
            if (goods.getAuditStatus() != null && goods.getAuditStatus().length() > 0) {
                criteria.andAuditStatusLike("%" + goods.getAuditStatus() + "%");
            }
            if (goods.getIsMarketable() != null && goods.getIsMarketable().length() > 0) {
                criteria.andIsMarketableLike("%" + goods.getIsMarketable() + "%");
            }
            if (goods.getCaption() != null && goods.getCaption().length() > 0) {
                criteria.andCaptionLike("%" + goods.getCaption() + "%");
            }
            if (goods.getSmallPic() != null && goods.getSmallPic().length() > 0) {
                criteria.andSmallPicLike("%" + goods.getSmallPic() + "%");
            }
            if (goods.getIsEnableSpec() != null && goods.getIsEnableSpec().length() > 0) {
                criteria.andIsEnableSpecLike("%" + goods.getIsEnableSpec() + "%");
            }
            if (goods.getIsDelete() != null && goods.getIsDelete().length() > 0) {
                criteria.andIsDeleteLike("%" + goods.getIsDelete() + "%");
            }
            if (goods.getSellerId()!=null && goods.getSellerId().length()>0){
                criteria.andSellerIdEqualTo(goods.getSellerId());
            }
        }

        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void updateStatus(Long[] ids, String status) {
        for(Long id:ids){
            TbGoods goods = goodsMapper.selectByPrimaryKey(id);
            goods.setAuditStatus(status);
            goodsMapper.updateByPrimaryKey(goods);
        }
    }

    /**
     * 下架商品
     * @param id
     */
    @Override
    public void updateIsMarketable(Long id) {
        TbGoods goods = goodsMapper.selectByPrimaryKey(id);
        goods.setIsMarketable("0");
        goodsMapper.updateByPrimaryKey(goods);
    }
}
