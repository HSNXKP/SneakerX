package top.naccl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.model.vo.Result;
import top.naccl.service.ProductService;


/**
 * @Author wdd
 * @Date 2023/4/4 19:12
 * @PackageName:top.naccl.controller
 * @ClassName: ProductController
 * @Version 1.0
 */
@RestController
public class ProductController {


    @Autowired
    private ProductService productService;

    /**
     * 获得推荐的球鞋
     * @return
     */
    @GetMapping("/getProductsIsRecommend")
    public Result getProductsIsRecommend(){
        return Result.ok("获取成功",productService.getProductsIsRecommend());
    }

}
