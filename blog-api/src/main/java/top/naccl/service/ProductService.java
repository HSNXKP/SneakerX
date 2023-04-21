package top.naccl.service;

import top.naccl.entity.Product;
import top.naccl.model.vo.Result;

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


    Product getProductById(Long id);

    Result collectProduct(Long userId, Long productId);

    Result isCollectProduct(Long userId, Long productId);

    Result cancelCollectProduct(Long userId, Long productId);

    Result getProductCollect(Long userId);

    Result deleteProductCollectByProductId(Long productId, Long userId);

    Result deleteAllProductCollectByUserId(Long userId);

    Result checkedProductCollect(Long userId, Long productId, Boolean checked,String type);

}
