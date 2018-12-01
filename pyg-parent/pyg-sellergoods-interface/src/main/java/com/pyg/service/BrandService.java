package com.pyg.service;

import entity.PageResult;
import com.pyg.pojo.TbBrand;

import java.util.List;

public interface BrandService {

    List<TbBrand> findAll();

    PageResult getPageBrand(Integer page, Integer rows);

    void add(TbBrand brand);

    void update(TbBrand brand);

    TbBrand findOne(Long id);

    void delete(long[] ids);

    PageResult findPage(TbBrand brand,Integer page,Integer size);
}
