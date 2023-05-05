package top.naccl.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.naccl.config.properties.UploadProperties;
import top.naccl.entity.Product;
import top.naccl.entity.ProductSize;
import top.naccl.entity.User;
import top.naccl.exception.NotFoundException;
import top.naccl.mapper.ProductMapper;
import top.naccl.mapper.ProductSizeMapper;
import top.naccl.model.vo.Result;
import top.naccl.service.ProductService;
import top.naccl.service.ProductSizeService;
import top.naccl.util.StringUtils;
import top.naccl.util.upload.UploadUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @Author wdd
 * @Date 2023/4/4 19:10
 * @PackageName:top.naccl.service.impl
 * @ClassName: ProductServiceImpl
 * @Version 1.0
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private UploadProperties uploadProperties;

    @Autowired
    private ProductSizeMapper productSizeMapper;

    @Override
    public List<Product> getProductByProductCategoryId(Long id) {

        try {
            List<Product> productList = productMapper.getProductByProductCategoryId(id);
            if (productList == null){
                throw new NotFoundException("该类目下没有商品");
            }
            return productList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Product> getProductsIsRecommend() {
        try {
            List<Product> productsIsRecommend = productMapper.getProductsIsRecommend();
            if (productsIsRecommend == null){
                throw new NotFoundException("没有推荐的商品");
            }
            return productsIsRecommend;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Product getProductById(Long id) {
        try {
            Product product = productMapper.getProductById(id);
            if (product == null){
                throw new NotFoundException("该商品未上架");
            }
            return product;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




    @Override
    public Result collectProduct(Long userId, Long productId) {
        if (productMapper.isCollectProductByUserIdAndProductId(userId,productId) == 0){
            productMapper.addCollectProduct(userId,productId, LocalDateTime.now());
            return Result.ok("收藏成功");
        }
        return Result.error("已收藏");
    }

    @Override
    public Result isCollectProduct(Long userId, Long productId) {
        if (productMapper.isCollectProductByUserIdAndProductId(userId,productId) == 0){
            return Result.ok("未收藏",false);
        }
        return Result.ok("已收藏",true);
    }

    @Override
    public Result cancelCollectProduct(Long userId, Long productId) {
        if (productMapper.isCollectProductByUserIdAndProductId(userId,productId) == 1){
            productMapper.deleteCollectProduct(userId,productId);
            return Result.ok("取消收藏成功",false);
        }
        return Result.error("未收藏");
    }

    @Override
    public Result getProductCollect(Long userId) {
        List<Product> productCollectList = productMapper.getProductCollect(userId);
        if (productCollectList != null){
            return Result.ok("查询成功",productCollectList);
        }
        return Result.error("暂无商品收藏");
    }

    @Override
    public Result deleteProductCollectByProductId(Long productId, Long userId) {
        if (productMapper.isCollectProductByUserIdAndProductId(userId,productId) == 1){
            productMapper.deleteCollectProduct(userId,productId);
            return Result.ok("取消收藏成功");
        }
        return Result.error("收藏失败");
    }

    @Override
    public Result deleteAllProductCollectByUserId(Long userId) {
        if (productMapper.deleteAllProductCollectByUserId(userId) == 1){
            return Result.ok("取消收藏成功");
        }
        return Result.error("收藏失败");
    }

    @Override
    public Result checkedProductCollect(Long userId, Long productId, Boolean checked,String type) {
        if (type.equals("all")){
            productMapper.checkedProductCollect(userId,null,checked);
            return Result.ok("操作成功");
        }
        if (productMapper.checkedProductCollect(userId,productId,checked) == 1){
            return Result.ok("操作成功");
        }
        return Result.error("操作失败");
    }

    @Override
    public Result getAllProduct(Long productCategoryId, String name, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productMapper.getAllProduct(productCategoryId,name);
        PageInfo<Product> productPageInfo = new PageInfo<>(productList);
        return Result.ok("查询成功",productPageInfo);
    }

    @Override
    public Result addProduct(Product product) {
        if (productMapper.addProduct(product) == 1){
            return Result.ok("添加成功");
        }
        return Result.error("添加失败");
    }

    @Override
    public Result uploadProductImage(MultipartFile file) {
        // 判断当前环境是linux还是window
        String productPath = "";
        String path = "";
        String osName = System.getProperties().getProperty("os.name");
        if(osName.equals("Linux")){
            productPath = uploadProperties.getLinuxProductPath();
            path = uploadProperties.getLinuxNginx();

        }else{
            productPath = uploadProperties.getProductPath();
            path = uploadProperties.getWindowNginx();
        }
        String accessProductPath = uploadProperties.getAccessProductPath();
        // 拿到file的后戳
        String substring = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = UUID.randomUUID() + substring;
        try {
            // 储存图片
            UploadUtils.saveFile(file.getInputStream(),fileName,productPath);
            // 设置商品的映射路径
            // 本地：http://localhost/product/0639b8e1-0978-499f-aa44-beb64b9a1d61.jpg
            // 服务器：http://43.138.9.213/image/product/0639b8e1-0978-499f-aa44-beb64b9a1d61.jpg
            // 示例： http://localhost + /product/ + 0639b8e1-0978-499f-aa44-beb64b9a1d61.jpg
            String backPath =  path + accessProductPath + fileName;
            return Result.ok("上传成功",backPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Result deleteProduct(Long productId) {
        List<ProductSize> productSizeWithPriceByProductId = productSizeMapper.getProductInventoryInfoByProductId(productId);
        if (productSizeWithPriceByProductId.size() == 0){
            if (productMapper.deleteProduct(productId) == 1){
                return Result.ok("删除成功");
            }
            return Result.error("删除失败");
        }
        return Result.error("该商品正在上架，不能删除");
    }

    @Override
    public Result updateProduct(Product product) {
        product.setUpdateTime(product.getCreateTime());
        if (productMapper.updateProduct(product) == 1){
            return Result.ok("修改成功");
        }
        return Result.error("修改失败");
    }

    @Override
    public Result changeRecommend(Long id, Boolean checked) {
        if (productMapper.changeRecommend(id,checked) == 1){
            return Result.ok("修改成功");
        }
        return Result.error("修改失败");
    }

    @Override
    public Result getAllProductByCodeOrName(String query, Integer pageNum, Integer pageSize) {
        if (!StringUtils.isEmpty(query)){
            PageHelper.startPage(pageNum,pageSize);
            List<Product> productList = productMapper.getAllProductByCodeOrName(query);
            PageInfo<Product> productPageInfo = new PageInfo<>(productList);
            return Result.ok("查询成功",productPageInfo);
        }
        return Result.error("请填写查询条件");
    }

}
