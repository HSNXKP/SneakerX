package top.naccl.service;

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
}
