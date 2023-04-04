package top.naccl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.naccl.entity.ProductCategory;
import top.naccl.exception.NotFoundException;
import top.naccl.mapper.ProductCategoryMapper;
import top.naccl.model.vo.Result;
import top.naccl.service.ProductCategoryService;

import java.util.List;

/**
 * @Author wdd
 * @Date 2023/4/4 19:11
 * @PackageName:top.naccl.service.impl
 * @ClassName: ProductCategoryServiceImpl
 * @Version 1.0
 */
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Override
    public Result getProductCategories() {
        // 递归调用
        List<ProductCategory> productCategories = productMenu(-1L);
        return Result.ok("获取成功",productCategories);
    }

    /**
     * 递归
     * @param parentId
     * @return
     */
    List<ProductCategory> productMenu(Long parentId) {
        // 通过父id查询
        List<ProductCategory> productCategories = productCategoryMapper.getProductCategoryByParentId(parentId);
        // 递归将子分类放入父分类的children属性中
        for (ProductCategory productCategory : productCategories) {
            List<ProductCategory> children = productMenu(productCategory.getId());
            productCategory.setChildren(children);
        }
        return productCategories;
    }


    @Override
    public ProductCategory getProductCategoryById(Long id) {
        ProductCategory productCategory = productCategoryMapper.getProductCategoryById(id);
        if (productCategory == null){
            throw new NotFoundException("查询的类目未找到");
        }
        return productCategory;
    }

    @Override
    public List<ProductCategory> getProductCategoryByParentId(Long id) {

        try {
            List<ProductCategory> productCategoryList = productCategoryMapper.getProductCategoryByParentId(id);
            if (productCategoryList == null){
                throw new NotFoundException("查询的类目未找到");
            }
            return productCategoryList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
