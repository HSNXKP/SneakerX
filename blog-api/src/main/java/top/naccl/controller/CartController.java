package top.naccl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.naccl.entity.Cart;
import top.naccl.model.vo.OrderVo;
import top.naccl.model.vo.Result;
import top.naccl.service.CartService;

import java.util.List;

/**
 * @author: wdd
 * @date: 2023/4/11 21:14
 */
@RestController
public class CartController {


    @Autowired
    private CartService cartService;

    @PostMapping("user/addCart")
    public Result addCart(@RequestHeader(value = "Authorization", defaultValue = "") String jwt,
                          @RequestBody OrderVo orderVo) {
        return cartService.addCart(jwt,orderVo);
    }

    /**
     * 通过userId查询购物车 渲染到前端的对象list
     * @param jwt
     * @param userId
     * @return
     */
    @GetMapping("user/cart")
    public Result getCartByUserId(@RequestHeader(value = "Authorization", defaultValue = "") String jwt,
                                  @RequestParam Long userId){
        return cartService.getCartByUserId(jwt,userId);
    }

    @GetMapping("/user/addQuantity")
    public Result addQuantityById(@RequestParam("id") Long id,@RequestParam("userId")Long userId){
        return cartService.addQuantityById(id,userId);
    }

    @GetMapping("/user/downQuantity")
    public Result downQuantityById(@RequestParam("id") Long id,@RequestParam("userId")Long userId){
        return cartService.downQuantityById(id,userId);
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

    /**
     * 通过购物车id和userId删除购物车商品
     * @param id
     * @param userId
     * @return
     */
    @GetMapping("/user/deleteCart")
    public Result deleteCart(@RequestParam("id") Long id,@RequestParam("userId")Long userId,@RequestParam("type")String type){
       return cartService.deleteCart(id,userId,type);
    }

    /**
     * 通过是否选中状态获取购物车详情
     * @param userId
     * @return
     */
    @GetMapping("/user/getCartListByUserIdIsChecked")
    public Result getCartListByUserIdIsChecked(@RequestParam("userId")Long userId){
        return cartService.getCartListByUserIdIsChecked(userId);
    }
}
