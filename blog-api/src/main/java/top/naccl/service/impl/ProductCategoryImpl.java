package top.naccl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.naccl.entity.ProductCategory;
import top.naccl.mapper.ProductCategoryMapper;
import top.naccl.model.vo.Result;
import top.naccl.service.ProductCategoryService;

import java.util.List;

/**
 * @author: wdd
 * @date: 2023/4/3 20:04
 */
@Service
public class ProductCategoryImpl implements ProductCategoryService {


    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Override
    public Result getProductCategories() {

        List<ProductCategory> productCategories = productMenu(-1L);
        return Result.ok("获取成功",productCategories);
    }

     List<ProductCategory> productMenu(Long parentId) {
        List<ProductCategory> productCategories = productCategoryMapper.getProductCategories(parentId);
        // 递归将子分类放入父分类的children属性中
        for (ProductCategory productCategory : productCategories) {
            List<ProductCategory> children = productMenu(productCategory.getId());
            productCategory.setChildren(children);
        }
        return productCategories;
    }
}
