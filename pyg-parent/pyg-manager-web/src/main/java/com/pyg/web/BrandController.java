package com.pyg.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.pojo.PageResoult;
import com.pyg.pojo.Resoult;
import com.pyg.pojo.TbBrand;
import com.pyg.service.BrandService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: pyg-parent
 * @description: 品牌
 * @author: zzy
 * @create: 2018-11-27 19:46
 **/
@RestController
@RequestMapping("brand")
public class BrandController {

    @Reference
    private BrandService brandService;

    @RequestMapping("/findAll")
    public List<TbBrand> findAll() {
        return brandService.findAll();
    }


    @RequestMapping("/findPage")
    public PageResoult findPage(Integer page, Integer size) {
        return brandService.getPageBrand(page, size);
    }

    @RequestMapping("/add")
    public Resoult add(@RequestBody TbBrand brand) {
        try {
            brandService.add(brand);
            return new Resoult(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Resoult(false, "增加失败");
        }
    }

    @RequestMapping("/update")
    public Resoult update(@RequestBody TbBrand brand) {
        try {
            brandService.update(brand);
            return new Resoult(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Resoult(false, "修改失败");
        }
    }

    @RequestMapping("/findOne")
    public TbBrand findOne(Long id) {
        return brandService.findOne(id);
    }

    @RequestMapping("/delete")
    public Resoult delete(long[] ids) {
        try {
            brandService.delete(ids);
            return new Resoult(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Resoult(false, "删除失败");
        }
    }

    @RequestMapping("/search")
    public PageResoult search(@RequestBody TbBrand brand,int page, int rows){
        return brandService.findPage(brand,page,rows);
    }
}
