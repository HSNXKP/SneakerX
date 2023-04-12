package top.naccl.service;

import org.springframework.web.bind.annotation.RequestParam;
import top.naccl.entity.Cart;
import top.naccl.model.vo.OrderVo;
import top.naccl.model.vo.Result;

public interface CartService {
    Result addCart(String jwt,OrderVo orderVo);

    Result getCartByUserId(String jwt, Long id);

    Result addQuantityById(Long id);

    Result downQuantityById(Long id);

    Result changeChecked(Long id, String type,Boolean checked, Long userId);
}
