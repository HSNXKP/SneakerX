package top.naccl.service;

import top.naccl.entity.Product;

import java.util.List;

/**
 * @Author wdd
 * @Date 2023/4/4 19:10
 * @PackageName:top.naccl.service
 * @ClassName: ProductService
 * @Version 1.0
 */
public interface ProductService {
    List<Product> getProductByProductCategoryId(Long id);

    List<Product> getProductsIsRecommend();

}
