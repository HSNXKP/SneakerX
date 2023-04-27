package top.naccl.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.naccl.entity.ProductCategory;

import java.util.List;
@Mapper
@Repository
public interface ProductCategoryMapper {

    List<ProductCategory> getProductCategoryByParentId(@Param("parentId") Long parentId);

    ProductCategory getProductCategoryById(@Param("id") Long id);

    List<ProductCategory> getAllProductCategories(@Param("parentId")Long parentId,@Param("name") String name);

    int updateProductCategory(ProductCategory productCategory);

    int deleteProductCategory(Long id);

    int addProductCategory(ProductCategory productCategory);
}
