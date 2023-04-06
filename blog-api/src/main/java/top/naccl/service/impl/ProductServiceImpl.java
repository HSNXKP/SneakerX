package top.naccl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.naccl.entity.Product;
import top.naccl.exception.NotFoundException;
import top.naccl.mapper.ProductMapper;
import top.naccl.model.vo.Result;
import top.naccl.service.ProductService;

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

}
