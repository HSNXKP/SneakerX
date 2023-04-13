package top.naccl.service;

import org.springframework.web.bind.annotation.RequestParam;
import top.naccl.entity.Cart;
import top.naccl.model.vo.OrderVo;
import top.naccl.model.vo.Result;

public interface CartService {
    Result addCart(String jwt,OrderVo orderVo);

    Result getCartByUserId(String jwt, Long userId);

    Result addQuantityById(Long id,Long userId);

    Result downQuantityById(Long id,Long userId);

    Result changeChecked(Long id, String type,Boolean checked, Long userId);

    Result deleteCart(Long id, Long userId,String type);

    Result getCartListByUserIdIsChecked(Long userId);
}
