package top.naccl.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import top.naccl.entity.Order;
import top.naccl.entity.ProductSize;
import top.naccl.model.vo.ProductInventory;
import top.naccl.model.vo.ProductInventoryInfo;
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

    ProductSize getProductSizeById(@Param("id") Long id);

    boolean reduceProductCount(@Param("id") Long id,@Param("quantity")Long quantity);

    List<ProductInventory> getAllProductSize(@Param("query") String query);

    List<ProductSize> getProductInventoryInfoByProductId(@Param("productId")Long productId);

    int addProductInventoryInfo(ProductSize productSize);

    int updateProductInventoryInfo(ProductSize productSize);

    int deleteProductInventory(Long id);
}
