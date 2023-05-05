package top.naccl.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.naccl.entity.Product;
import top.naccl.entity.ProductSize;
import top.naccl.exception.NotFoundException;
import top.naccl.mapper.ProductMapper;
import top.naccl.mapper.ProductSizeMapper;
import top.naccl.model.vo.ProductInventory;
import top.naccl.model.vo.ProductInventoryInfo;
import top.naccl.model.vo.Result;
import top.naccl.service.ProductSizeService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author wdd
 * @Date 2023/4/6 15:06
 * @PackageName:top.naccl.service.impl
 * @ClassName: ProductSizeServiceImpl
 * @Version 1.0
 */
@Service

public class ProductSizeServiceImpl implements ProductSizeService {

    @Autowired
    private ProductSizeMapper productSizeMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public Result getProductSizeWithPriceByProductId(Long id) {
        List<ProductSize> productSizeWithPriceList = productSizeMapper.getProductInventoryInfoByProductId(id);
        if (productSizeWithPriceList.size() == 0){
            return Result.error("该商品未上架,暂时不能购买");
            }
            return Result.ok("获取成功",productSizeWithPriceList);
    }

    @Override
    public Result getAllProductSize(String query,Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<ProductInventory> allProductSize = productSizeMapper.getAllProductSize(query);
        PageInfo<ProductInventory> pageInfo = new PageInfo<>(allProductSize);
        return Result.ok("获取成功",pageInfo);
    }

    @Override
    public Result getProductInventoryInfoByProductId(Long productId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<ProductSize> allProductSize = productSizeMapper.getProductInventoryInfoByProductId(productId);
        PageInfo<ProductSize> pageInfo = new PageInfo<>(allProductSize);
        return Result.ok("获取成功",pageInfo);
    }

    @Override
    public Result addProductInventoryInfo(ProductSize productSize) {
        Product product = productMapper.getProductById(productSize.getProductId());
        List<ProductSize> productInventoryInfo = productSizeMapper.getProductInventoryInfoByProductId(productSize.getProductId());
        // 修改
        productSizeMapper.addProductInventoryInfo(productSize);
        if (productInventoryInfo.size() != 0){
            if (product.getPrice() != null){
                if (productSize.getProductPrice() < product.getPrice()){
                    // 设置最低价格
                    product.setPrice(productSize.getProductPrice());
                    productMapper.updateProduct(product);
                    return Result.ok("添加成功");
                }
                return Result.ok("添加成功");
            }
            // 设置最低价格
            product.setPrice(productSize.getProductPrice());
            productMapper.updateProduct(product);
            return Result.ok("添加成功");
        }
        // 设置最低价格
        product.setPrice(productSize.getProductPrice());
        productMapper.updateProduct(product);
        return Result.ok("添加成功");
    }

    @Override
    public Result updateProductInventoryInfo(ProductSize productSize) {
        Product product = productMapper.getProductById(productSize.getProductId());
        // 修改
        productSizeMapper.updateProductInventoryInfo(productSize);
        if (productSize.getProductPrice() < product.getPrice()){
            // 设置最低价格
            product.setPrice(productSize.getProductPrice());
            productMapper.updateProduct(product);
            return Result.ok("修改成功");
        }
        return Result.ok("修改成功");
    }

    @Override
    public Result getProductInventory(Long id) {
        ProductSize productSize = productSizeMapper.getProductSizeById(id);
        return Result.ok("获取成功",productSize);
    }

    @Override
    public Result deleteProductInventory(Long id) {
        ProductSize productSize = productSizeMapper.getProductSizeById(id);
        Product product = productMapper.getProductById(productSize.getProductId());
        // 先删除
        productSizeMapper.deleteProductInventory(id);
        List<ProductSize> productInventory = productSizeMapper.getProductInventoryInfoByProductId(productSize.getProductId());
        // 后比较
        if (product.getPrice().equals(productSize.getProductPrice())){
            // 求productInventory.getProductPrice()的最小值
            Long minProductPrice = productInventory.stream().min(Comparator.comparingLong(ProductSize::getProductPrice)).get().getProductPrice();
            // 设置最低价格
            product.setPrice(minProductPrice);
            productMapper.updateProduct(product);
            return Result.ok("删除成功");
        }
        return Result.ok("删除成功");
    }
}
