package top.naccl.service;

import org.springframework.web.bind.annotation.RequestParam;
import top.naccl.entity.ProductSize;
import top.naccl.model.vo.Result;

/**
 * @Author wdd
 * @Date 2023/4/6 15:06
 * @PackageName:top.naccl.service
 * @ClassName: ProductSizeService
 * @Version 1.0
 */
public interface ProductSizeService {

    Result getProductSizeWithPriceByProductId(Long id);

    Result getAllProductSize(String query,Integer pageNum,Integer pageSize);

    Result getProductInventoryInfoByProductId(Long productId, Integer pageNum, Integer pageSize);

    Result addProductInventoryInfo(ProductSize productSize);

    Result updateProductInventoryInfo(ProductSize productSize);

    Result getProductInventory(Long id);

    Result deleteProductInventory(Long id);
}
