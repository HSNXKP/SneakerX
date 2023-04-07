package top.naccl.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.naccl.entity.Order;
import top.naccl.entity.ProductSize;
import top.naccl.model.vo.Result;

import java.util.List;

/**
 * @Author wdd
 * @Date 2023/4/6 15:07
 * @PackageName:top.naccl.mapper
 * @ClassName: ProductSizeMapper
 * @Version 1.0
 */
@Mapper
@Repository
public interface ProductSizeMapper {
    List<ProductSize> getProductSizeWithPriceByProductId(@Param("productId") Long productId);

    ProductSize getProductSizeById(@Param("id") Long id);

    boolean reduceProductCount(@Param("id") Long id);
}
