package com.pyg.service;

import com.pyg.pojo.PageResoult;
import com.pyg.pojo.TbBrand;

import java.util.List;

public interface BrandService {

    List<TbBrand> findAll();

    PageResoult getPageBrand(Integer page,Integer rows);

    void add(TbBrand brand);

    void update(TbBrand brand);

    TbBrand findOne(Long id);
}
