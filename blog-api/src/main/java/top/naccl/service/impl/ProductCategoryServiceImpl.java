package top.naccl.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.naccl.config.properties.UploadProperties;
import top.naccl.entity.Product;
import top.naccl.entity.ProductCategory;
import top.naccl.exception.NotFoundException;
import top.naccl.mapper.ProductCategoryMapper;
import top.naccl.mapper.ProductMapper;
import top.naccl.model.vo.Result;
import top.naccl.service.ProductCategoryService;
import top.naccl.util.HashUtils;
import top.naccl.util.StringUtils;
import top.naccl.util.upload.UploadUtils;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @Author wdd
 * @Date 2023/4/4 19:11
 * @PackageName:top.naccl.service.impl
 * @ClassName: ProductCategoryServiceImpl
 * @Version 1.0
 */
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private UploadProperties uploadProperties;

    @Override
    public Result getProductCategories() {
        // 递归调用
        List<ProductCategory> productCategories = productMenu(-1L);
        return Result.ok("获取成功",productCategories);
    }

    /**
     * 递归
     * @param parentId
     * @return
     */
    List<ProductCategory> productMenu(Long parentId) {
        // 通过父id查询
        List<ProductCategory> productCategories = productCategoryMapper.getProductCategoryByParentId(parentId);
        // 递归将子分类放入父分类的children属性中
        for (ProductCategory productCategory : productCategories) {
            List<ProductCategory> children = productMenu(productCategory.getId());
            productCategory.setChildren(children);
        }
        return productCategories;
    }


    @Override
    public ProductCategory getProductCategoryById(Long id) {
        ProductCategory productCategory = productCategoryMapper.getProductCategoryById(id);
        if (productCategory == null){
            throw new NotFoundException("查询的类目未找到");
        }
        return productCategory;
    }

    @Override
    public List<ProductCategory> getProductCategoryByParentId(Long id) {
        try {
            List<ProductCategory> productCategoryList = productCategoryMapper.getProductCategoryByParentId(id);
            if (productCategoryList == null){
                throw new NotFoundException("查询的类目未找到");
            }
            return productCategoryList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Result getAllProductCategories(Long parentId, String name,Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<ProductCategory> productCategories = productCategoryMapper.getAllProductCategories(parentId,name);
        PageInfo<ProductCategory> pageResult = new PageInfo<>(productCategories);
        return Result.ok("获取成功",pageResult);
    }

    @Override
    public Result updateProductCategory(ProductCategory productCategory) {
        if (productCategoryMapper.updateProductCategory(productCategory) == 1){
            return Result.ok("更新成功");
        }
        return Result.error("更新失败");
    }

    @Override
    public Result deleteProductCategory(Long id) {
        ProductCategory productCategory = productCategoryMapper.getProductCategoryById(id);
        if (productCategory != null){
            if (productCategory.getParentId() == -1){
                List<ProductCategory> productCategoryList = productCategoryMapper.getProductCategoryByParentId(id);
                if(productCategoryList.size() == 0){
                    productCategoryMapper.deleteProductCategory(id);
                    return Result.ok("删除成功");
                }
                return Result.error("该品牌下还有商品分类，删除失败");
            }
            List<Product> productList = productMapper.getProductByProductCategoryId(id);
            if (productList.size() == 0){
                productCategoryMapper.deleteProductCategory(id);
                return Result.ok("删除成功");
            }
            return Result.error("该分类下还有商品，删除失败");
        }
        return Result.error("该分类不存在");
    }

    @Override
    public Result getProductCategory(Long id) {
        ProductCategory productCategory = productCategoryMapper.getProductCategoryById(id);
        if (productCategory != null){
            return Result.ok("获取成功",productCategory);
        }
        return Result.error("该分类不存在");
    }

    @Override
    public Result addProductCategory(ProductCategory productCategory) {
        if(!StringUtils.isEmpty(productCategory.getName(),productCategory.getDescription())){
            if (productCategoryMapper.addProductCategory(productCategory) == 1){
                return Result.ok("添加成功");
            }
            return Result.error("添加失败");
        }
        return Result.error("分类名称和描述不能为空");
    }

    @Override
    public Result uploadProductCategoryImage(MultipartFile file,String type) {
        // 判断当前环境是linux还是window
        String categoryPath = "";
        String path = "";
        String accessPath = "";
        String osName = System.getProperties().getProperty("os.name");
        if(osName.equals("Linux")){
            path = uploadProperties.getLinuxNginx();
            if (type.equals("productBrandImage")){
                accessPath = uploadProperties.getAccessProductBrandPath();
                categoryPath = uploadProperties.getLinuxProductBrandPath();
            }else{
                accessPath = uploadProperties.getAccessProductCategoryPath();
                categoryPath = uploadProperties.getLinuxProductCategoryPath();
            }
        }else{
            path = uploadProperties.getWindowNginx();
            if (type.equals("productBrandImage")){
                accessPath = uploadProperties.getAccessProductBrandPath();
                categoryPath = uploadProperties.getProductBrandPath();
            }else{
                accessPath = uploadProperties.getAccessProductCategoryPath();
                categoryPath = uploadProperties.getProductCategoryPath();
            }
        }
        // 拿到file的后戳
        String substring = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = UUID.randomUUID() + substring;
        try {
            // 储存图片
            UploadUtils.saveFile(file.getInputStream(),fileName,categoryPath);
            // 设置商品的映射路径
            // 本地：http://localhost/productBrand/0639b8e1-0978-499f-aa44-beb64b9a1d61.jpg
            // 服务器：http://43.138.9.213/image/productBrand/0639b8e1-0978-499f-aa44-beb64b9a1d61.jpg
            // 示例： http://localhost + /productBrand/ + 0639b8e1-0978-499f-aa44-beb64b9a1d61.jpg
            String backPath =  path + accessPath + fileName;
            return Result.ok("上传成功",backPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
