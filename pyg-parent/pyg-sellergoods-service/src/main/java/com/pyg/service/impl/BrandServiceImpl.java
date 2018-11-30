package com.pyg.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pyg.mapper.TbBrandMapper;
import com.pyg.pojo.PageResoult;
import com.pyg.pojo.TbBrand;
import com.pyg.pojo.TbBrandExample;
import com.pyg.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @program: pyg-parent
 * @description: 品牌
 * @author: zzy
 * @create: 2018-11-27 19:24
 **/
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private TbBrandMapper tbBrandMapper;

    @Override
    public List<TbBrand> findAll() {
        return tbBrandMapper.selectByExample(null);
    }

    @Override
    public PageResoult getPageBrand(Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        List<TbBrand> list = tbBrandMapper.selectByExample(new TbBrandExample());
        PageInfo<TbBrand> info = new PageInfo<>(list);
        return new PageResoult(info.getTotal(), info.getList());
    }

    @Override
    public void add(TbBrand brand) {
        tbBrandMapper.insert(brand);
    }

    @Override
    public void update(TbBrand brand) {
        tbBrandMapper.updateByPrimaryKey(brand);
    }

    @Override
    public TbBrand findOne(Long id) {
        return tbBrandMapper.selectByPrimaryKey(id);
    }

    @Override
    public void delete(long[] ids) {
        for (long id : ids) {
            tbBrandMapper.deleteByPrimaryKey(id);
        }
    }

    @Override
    public PageResoult findPage(TbBrand brand, Integer page, Integer size) {
        PageHelper.startPage(page,size);
        TbBrandExample example = new TbBrandExample();
        TbBrandExample.Criteria criteria = example.createCriteria();
        if (brand!=null){
            if (brand.getName()!=null && brand.getName().length()>0){
                criteria.andNameLike("%"+brand.getName()+"%");
            }
            if (brand.getFirstChar()!=null && brand.getFirstChar().length() >0){
                criteria.andFirstCharEqualTo(brand.getFirstChar());
            }
        }
        Page<TbBrand> brandPage = (Page<TbBrand>) tbBrandMapper.selectByExample(example);
        return new PageResoult(brandPage.getTotal(),brandPage.getResult());
    }
}
