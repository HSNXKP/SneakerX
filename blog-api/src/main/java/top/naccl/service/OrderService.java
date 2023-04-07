package top.naccl.service;

import top.naccl.entity.Order;
import top.naccl.model.vo.OrderVo;
import top.naccl.model.vo.Result;

public interface OrderService {
    Result summitOrder(OrderVo order, String jwt);
}
