package top.naccl.service;

import top.naccl.model.vo.OrderVo;
import top.naccl.model.vo.Result;

public interface CartService {
    Result addCart(String jwt,OrderVo orderVo);

    Result getCartByUserId(String jwt, Long id);
}
