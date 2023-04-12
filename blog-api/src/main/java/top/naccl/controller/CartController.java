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

    @GetMapping("/user/addQuantity")
    public Result addQuantityById(@RequestParam("id") Long id){
        return cartService.addQuantityById(id);
    }

    @GetMapping("/user/downQuantity")
    public Result downQuantityById(@RequestParam("id") Long id){
        return cartService.downQuantityById(id);
    }

    /**
     * 修改购物车商品选中状态 当前是productCategory时，修改所有productCategory下的商品选中状态 当前是product时，修改所有product下的商品选中状态
     * @param id
     * @param type
     * @param checked
     * @param userId
     * @return
     */
    @GetMapping("/user/changeChecked")
    public Result changeChecked(@RequestParam("id") Long id, @RequestParam("type") String type,@RequestParam("checked")Boolean checked,@RequestParam("userId") Long userId){
            return cartService.changeChecked(id,type,checked,userId);
    }
}
