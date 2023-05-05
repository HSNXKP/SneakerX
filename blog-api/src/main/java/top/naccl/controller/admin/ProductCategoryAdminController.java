package top.naccl.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.naccl.entity.ProductCategory;
import top.naccl.model.vo.Result;
import top.naccl.service.ProductCategoryService;

/**
 * @author: wdd
 * @date: 2023/4/26 20:16
 */
@RestController
@RequestMapping("/admin")
public class ProductCategoryAdminController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("/getAllProductCategories")
    public Result getAllProductCategories(@RequestParam("parentId") Long parentId,
                                          @RequestParam("name") String name,
                                          @RequestParam(defaultValue = "1") Integer pageNum,
                                          @RequestParam(defaultValue = "10") Integer pageSize) {
        return productCategoryService.getAllProductCategories(parentId,name,pageNum,pageSize);
    }

    @PostMapping("/updateProductCategory")
    public Result updateProductCategory(ProductCategory productCategory) {
        return productCategoryService.updateProductCategory(productCategory);
    }


    @GetMapping("/deleteProductCategory")
    public Result deleteProductCategory(Long id) {
        return productCategoryService.deleteProductCategory(id);
    }


    @GetMapping("/getProductCategory")
    public Result getProductCategory(Long id) {
        return productCategoryService.getProductCategory(id);
    }


    @PostMapping("/addProductCategory")
    public Result addProductCategory(ProductCategory productCategory) {
        return productCategoryService.addProductCategory(productCategory);
    }

    @PostMapping("/uploadProductCategoryImage")
    public Result uploadProductImage(@RequestParam("file") MultipartFile file,@RequestParam("type")String type){
        return productCategoryService.uploadProductCategoryImage(file,type);
    }

}
