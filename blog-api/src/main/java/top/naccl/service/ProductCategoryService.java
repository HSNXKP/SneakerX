package top.naccl.service;

import top.naccl.entity.ProductCategory;
import top.naccl.model.vo.Result;

import java.util.List;

public interface ProductCategoryService {
    Result getProductCategories();


    ProductCategory getProductCategoryById(Long id);

    List<ProductCategory> getProductCategoryByParentId(Long id);
}
