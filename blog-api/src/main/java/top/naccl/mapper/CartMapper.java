package top.naccl.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.naccl.entity.Cart;
import top.naccl.model.vo.OrderVo;
import top.naccl.model.vo.Result;

import java.util.List;

@Mapper
@Repository
public interface CartMapper {
    int addCart(Cart cart);

    Cart getCartByProductSizeId(@Param("productSizeId") Long productSizeId,@Param("userId") Long userId );

    int updateCart(Cart cart);

    List<Cart> getCartByUserId(@Param("userId") Long userId);

    List<Cart> getCartByProductCategoryId(@Param("productCategoryId") Long productCategoryId);
}
