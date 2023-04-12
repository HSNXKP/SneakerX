package top.naccl.service;

import top.naccl.model.vo.Result;

public interface CartService {
    Result addCart(String jwt,String productId, String userId);
}
