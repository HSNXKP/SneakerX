package top.naccl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.naccl.entity.Product;
import top.naccl.exception.NotFoundException;
import top.naccl.mapper.ProductMapper;
import top.naccl.model.vo.Result;
import top.naccl.service.ProductService;

import java.time.LocalDateTime;
import java.util.List;

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

}
