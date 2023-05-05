package top.naccl.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.naccl.entity.ProductSize;
import top.naccl.model.vo.Result;
import top.naccl.service.ProductSizeService;

/**
 * @Author wdd
 * @Date 2023/5/5 9:10
 * @PackageName:top.naccl.controller.admin
 * @ClassName: ProductSizeAdminController
 * @Version 1.0
 */
@RestController
@RequestMapping("/admin")
public class ProductSizeAdminController {

    @Autowired
    private ProductSizeService productSizeService;


    @GetMapping("/getAllProductInventory")
    public Result getAllProductSize(@RequestParam("query") String query,
                                    @RequestParam(defaultValue = "1") Integer pageNum,
                                    @RequestParam(defaultValue = "10") Integer pageSize){
        return productSizeService.getAllProductSize(query,pageNum,pageSize);
    }

    @GetMapping("/getProductInventoryInfoByProductId")
    public Result getProductInventoryInfoByProductId(@RequestParam("productId") Long productId,
                                    @RequestParam(defaultValue = "1") Integer pageNum,
                                    @RequestParam(defaultValue = "10") Integer pageSize){
        return productSizeService.getProductInventoryInfoByProductId(productId,pageNum,pageSize);
    }


    @PostMapping("/addProductInventoryInfo")
    public Result addProductInventoryInfo(ProductSize productSize){
        return productSizeService.addProductInventoryInfo(productSize);
    }

    @PostMapping("/updateProductInventoryInfo")
    public Result updateProductInventoryInfo(ProductSize productSize){
        return productSizeService.updateProductInventoryInfo(productSize);
    }


    @GetMapping("/getProductInventory")
    public Result getProductInventory(Long id){
        return productSizeService.getProductInventory(id);
    }

    @GetMapping("/deleteProductInventory")
    public Result deleteProductInventory(Long id){
        return productSizeService.deleteProductInventory(id);
    }
}
