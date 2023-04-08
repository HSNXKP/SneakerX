package top.naccl.service;

import top.naccl.entity.Order;
import top.naccl.model.vo.OrderVo;
import top.naccl.model.vo.Result;

import java.util.Map;

public interface OrderService {
    Result summitOrder(OrderVo order, String jwt);

    Result getOrderByOrderNumber(String jwt,String orderNumber);

    int updateOrder(Order order);
}
