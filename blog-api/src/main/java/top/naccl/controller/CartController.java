package top.naccl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.naccl.model.vo.OrderVo;
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

    @PostMapping("/addCart")
    public Result addCart(@RequestHeader(value = "Authorization", defaultValue = "") String jwt,
                          @RequestBody OrderVo orderVo) {
        return cartService.addCart(jwt,orderVo);
    }

    @GetMapping("/cart")
    public Result getCartByUserId(@RequestHeader(value = "Authorization", defaultValue = "") String jwt,
                                  @RequestParam Long id){
        return cartService.getCartByUserId(jwt,id);
    }
}
