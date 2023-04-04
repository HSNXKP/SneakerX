package top.naccl.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.naccl.entity.ProductCategory;

import java.util.List;
@Mapper
@Repository
public interface ProductCategoryMapper {

    List<ProductCategory> getProductCategories(@Param("parentId") Long parentId);
}
