package top.naccl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.naccl.annotation.AccessLimit;
import top.naccl.entity.Order;
import top.naccl.model.vo.OrderVo;
import top.naccl.model.vo.Result;
import top.naccl.service.OrderService;

/**
 * @author: wdd
 * @date: 2023/4/6 22:17
 */
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 提交订单 需要比对当前的token和id是否一致
     * @param order
     * @param token
     * @param id
     * @return
     */
    @AccessLimit(seconds = 10, maxCount = 1, msg = "10秒内只能提交一次订单")
    @PostMapping("/order")
    public Result summitOrder(@RequestBody OrderVo order,
                              @RequestHeader(value = "Authorization", defaultValue = "") String jwt){
        try {
            return orderService.summitOrder(order, jwt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
