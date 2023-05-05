package top.naccl.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.naccl.entity.Product;
import top.naccl.entity.ProductCategory;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author wdd
 * @Date 2023/4/4 19:09
 * @PackageName:top.naccl.mapper
 * @ClassName: ProductMapper
 * @Version 1.0
 */
@Mapper
@Repository
public interface ProductMapper {
    List<Product> getProductByProductCategoryId(@Param("id") Long id);

    List<Product> getProductsIsRecommend();

    Product getProductById(Long id);


    int isCollectProductByUserIdAndProductId(@Param("userId")Long userId, @Param("productId")Long productId);

    int addCollectProduct(@Param("userId")Long userId, @Param("productId")Long productId, @Param("createTime") LocalDateTime createTime);

    int deleteCollectProduct(@Param("userId")Long userId,@Param("productId") Long productId);

    List<Product> getProductCollect(Long userId);

    int deleteAllProductCollectByUserId(Long userId);

    int checkedProductCollect(@Param("userId")Long userId,@Param("productId") Long productId, @Param("checked")Boolean checked);

    List<Product> getAllProduct(@Param("productCategoryId")Long productCategoryId, @Param("name")String name);

    int addProduct(Product product);

    int deleteProduct(Long productId);

    int updateProduct(Product product);

    int changeRecommend(Long id, Boolean checked);

    List<Product> getAllProductByCodeOrName(String query);
}
