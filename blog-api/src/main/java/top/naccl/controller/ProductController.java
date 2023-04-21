package top.naccl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.entity.Product;
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


    /**
     * 通过id查询商品
     * @param id
     * @return
     */
    @GetMapping("/getProductById")
    public Result getProductById(@RequestParam("id") Long id){
        return Result.ok("获取成功",productService.getProductById(id));
    }

    @GetMapping("/user/collectProduct")
    public Result collectProduct(@RequestParam("userId") Long userId,@RequestParam("productId") Long productId){
        return productService.collectProduct(userId,productId);
    }

    @GetMapping("/user/cancelCollectProduct")
    public Result cancelCollectProduct(@RequestParam("userId") Long userId,@RequestParam("productId") Long productId){
        return productService.cancelCollectProduct(userId,productId);
    }

    @GetMapping("/user/isCollectProduct")
    public Result isCollectProduct(@RequestParam("userId") Long userId,@RequestParam("productId") Long productId){
        return productService.isCollectProduct(userId,productId);
    }

    @GetMapping("/user/getProductCollect")
    public Result getProductCollect(@RequestParam("userId") Long userId){
        return productService.getProductCollect(userId);
    }

    @GetMapping("user/deleteProductCollectByProductId")
    public Result deleteProductCollectByProductId(@RequestParam("productId") Long productId, @RequestParam("userId") Long userId){
        return productService.deleteProductCollectByProductId(productId, userId);
    }

    //TODO 没有实现下面两个接口

    @GetMapping("user/deleteAllProductCollectByUserId")
    public Result deleteAllProductCollectByUserId(@RequestParam("userId") Long userId){
        return productService.deleteAllProductCollectByUserId(userId);
    }

    @GetMapping("user/checkedProductCollect")
    public Result checkedProductCollect(@RequestParam("userId") Long userId, @RequestParam("productId") Long productId, @RequestParam("checked") Boolean checked,String type){
        return productService.checkedProductCollect(userId, productId, checked,type);
    }

}
