package top.naccl.controller;

import org.apache.ibatis.annotations.Param;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.model.vo.Result;
import top.naccl.service.ProductSizeService;

/**
 * @Author wdd
 * @Date 2023/4/6 15:05
 * @PackageName:top.naccl.controller
 * @ClassName: ProductSizeController
 * @Version 1.0
 */
@RestController
public class ProductSizeController {

    @Autowired
    private ProductSizeService productSizeService;


    /**
     * 根据商品Id 查询价格和尺码
     * @param id
     * @return
     */
    @GetMapping("/getProductSizeWithPrice")
    public Result getProductSizeWithPriceByProductId(@Param("id") Long id){
        return productSizeService.getProductSizeWithPriceByProductId(id);
    }
}
