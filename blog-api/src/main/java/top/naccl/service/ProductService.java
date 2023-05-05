package top.naccl.service;

import org.springframework.web.multipart.MultipartFile;
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

    Result getAllProduct(Long productCategoryId, String name, Integer pageNum, Integer pageSize);

    Result addProduct(Product product);

    Result uploadProductImage(MultipartFile file);

    Result deleteProduct(Long productId);

    Result updateProduct(Product product);

    Result changeRecommend(Long id, Boolean checked);

    Result getAllProductByCodeOrName(String code, Integer pageNum, Integer pageSize);

}
