package top.naccl.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.naccl.entity.Product;

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
}
