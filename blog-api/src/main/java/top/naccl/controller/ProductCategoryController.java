package top.naccl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.entity.Product;
import top.naccl.entity.ProductCategory;
import top.naccl.model.vo.Result;
import top.naccl.service.ProductCategoryService;
import top.naccl.service.ProductService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: wdd
 * @date: 2023/4/3 20:03
 */
@RestController
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductService productService;


    /**
     * 查询所有分类类目
     * @return
     */
    @GetMapping("/productCategories")
    public Result getProductCategories() {
        return productCategoryService.getProductCategories();
    }

    /**
     * 通过id查询商品分类和商品
     * @param id
     * @return
     */
    @GetMapping("/productCategory")
    public Result productCategory(@RequestParam("id") Long id){

        ProductCategory productCategory = productCategoryService.getProductCategoryById(id);
        Map<Object,Object> map = new HashMap<Object, Object>();
        map.put("productCategoryName",productCategory.getName());
        if (productCategory.getParentId() == -1L){
            // 为-1的时候当前用户点击的是第一菜单
            // 将子集合查询返给前端
            List<ProductCategory> productCategoryListWithProduct = productCategoryService.getProductCategoryByParentId(id);
            map.put("productCategoryListWithProduct",productCategoryListWithProduct);
            return Result.ok("获取成功",map);
        }
        // 不是-1的话就是商品列表
        List<Product> productCategoryListWithProduct = productService.getProductByProductCategoryId(id);
        map.put("productCategoryListWithProduct",productCategoryListWithProduct);
        return Result.ok("获取成功",map);

    }


}
