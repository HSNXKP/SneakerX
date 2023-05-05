package top.naccl.service;

import org.springframework.web.multipart.MultipartFile;
import top.naccl.entity.ProductCategory;
import top.naccl.model.vo.Result;

import java.util.List;

public interface ProductCategoryService {
    Result getProductCategories();


    ProductCategory getProductCategoryById(Long id);

    List<ProductCategory> getProductCategoryByParentId(Long id);

    Result getAllProductCategories(Long parentId,String name,Integer pageNum,Integer pageSize);


    Result updateProductCategory(ProductCategory productCategory);

    Result deleteProductCategory(Long id);

    Result getProductCategory(Long id);

    Result addProductCategory(ProductCategory productCategory);

    Result uploadProductCategoryImage(MultipartFile file,String type);


}
