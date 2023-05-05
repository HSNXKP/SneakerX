package top.naccl.controller.admin;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.naccl.entity.Product;
import top.naccl.model.vo.Result;
import top.naccl.service.ProductService;

import java.time.LocalDateTime;

/**
 * @Author wdd
 * @Date 2023/4/26 17:08
 * @PackageName:top.naccl.controller.admin
 * @ClassName: ProductAdminController
 * @Version 1.0
 */
@RestController
@RequestMapping("/admin")
public class ProductAdminController {

    @Autowired
    private ProductService productService;

    @GetMapping("/getAllProduct")
    public Result getAllProductCategories(@RequestParam("productCategoryId") Long productCategoryId,
                                          @RequestParam("name") String name,
                                          @RequestParam(defaultValue = "1") Integer pageNum,
                                          @RequestParam(defaultValue = "10") Integer pageSize) {
        return productService.getAllProduct(productCategoryId,name,pageNum,pageSize);
    }


    @PostMapping("/addProduct")
    public Result addProduct(@RequestBody Product product) {
        product.setUpdateTime(product.getCreateTime());
        product.setPublished(true);
        product.setRecommend(false);
        product.setImage(product.getImage());
        return productService.addProduct(product);
    }

    @PostMapping("/uploadProductImage")
    public Result uploadProductImage(@RequestParam("file") MultipartFile file){
        return productService.uploadProductImage(file);
    }


    @GetMapping("/deleteProduct")
    public Result deleteProduct(@RequestParam("productId")Long productId) {
        return productService.deleteProduct(productId);
    }

    @PostMapping("/updateProduct")
    public Result updateProduct(@RequestBody Product product) {
        return productService.updateProduct(product);
    }

    @GetMapping("/getProduct")
    public Result getProduct(@RequestParam("productId") Long productId) {
        Product product = productService.getProductById(productId);
        return Result.ok("获取成功",product);
    }

    @GetMapping("/changeRecommend")
    public Result changeRecommend(@RequestParam Long id,@RequestParam Boolean checked) {
       return productService.changeRecommend(id,checked);
    }


    @GetMapping("/getAllProductByCodeOrName")
    public Result getAllProductByCodeOrName(@RequestParam("query") String query,
                                      @RequestParam(defaultValue = "1") Integer pageNum,
                                      @RequestParam(defaultValue = "10") Integer pageSize) {
        return productService.getAllProductByCodeOrName(query,pageNum,pageSize);
    }






}
