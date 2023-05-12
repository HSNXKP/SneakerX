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

    List<Cart> getCartByUserId(@Param("userId") Long userId,@Param("checked") Boolean checked);

    List<Cart> getCartByProductCategoryId(@Param("productCategoryId") Long productCategoryId,@Param("userId") Long userId);

    int addQuantityById(@Param("id") Long id,@Param("userId")Long userId);

    int downQuantityById(@Param("id") Long id,@Param("userId")Long userId);

    Cart getCartById(@Param("id") Long id,@Param("userId")Long userId);

    int deleteCartById(@Param("id") Long id,@Param("userId")Long userId);

    int changeChecked(@Param("productCategoryId") Long productCategoryId,@Param("cartId") Long cartId,@Param("userId") Long userId,@Param("checked") Boolean checked);

    int deleteCartByProductCategoryId(Long userId);

}
