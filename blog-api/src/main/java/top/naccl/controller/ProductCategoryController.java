package top.naccl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.model.vo.Result;
import top.naccl.service.ProductCategoryService;

/**
 * @author: wdd
 * @date: 2023/4/3 20:03
 */
@RestController
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("/productCategories")
    public Result getProductCategories() {
        return productCategoryService.getProductCategories();
    }
}
