package top.naccl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.model.vo.Result;
import top.naccl.service.CartService;

/**
 * @author: wdd
 * @date: 2023/4/11 21:14
 */
@RestController
public class CartController {


    @Autowired
    private CartService cartService;

    @GetMapping("/addCart")
    public Result addCart(@RequestHeader(value = "Authorization", defaultValue = "") String jwt,
                          String productId, String userId) {
        return cartService.addCart(jwt,productId, userId);
    }
}
