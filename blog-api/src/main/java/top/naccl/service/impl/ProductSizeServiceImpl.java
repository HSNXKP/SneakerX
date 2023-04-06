package top.naccl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.naccl.entity.ProductSize;
import top.naccl.exception.NotFoundException;
import top.naccl.mapper.ProductSizeMapper;
import top.naccl.model.vo.Result;
import top.naccl.service.ProductSizeService;

import java.util.List;

/**
 * @Author wdd
 * @Date 2023/4/6 15:06
 * @PackageName:top.naccl.service.impl
 * @ClassName: ProductSizeServiceImpl
 * @Version 1.0
 */
@Service

public class ProductSizeServiceImpl implements ProductSizeService {

    @Autowired
    private ProductSizeMapper productSizeMapper;

    @Override
    public Result getProductSizeWithPriceByProductId(Long id) {
        try {
            List<ProductSize> productSizeWithPriceList = productSizeMapper.getProductSizeWithPriceByProductId(id);
            if (productSizeWithPriceList == null){
                throw new NotFoundException("该商品未出价,暂时不能购买");
            }
            return Result.ok("获取成功",productSizeWithPriceList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
