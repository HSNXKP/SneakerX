package top.naccl.service;

import top.naccl.entity.Order;
import top.naccl.model.vo.OrderVo;
import top.naccl.model.vo.Result;

import java.util.Map;

public interface OrderService {
    Result summitOrder(OrderVo order, String jwt);

    Result getOrderByOrderNumber(String jwt,String orderNumber);

    int setOrderPayed(Order order);

    Result cancelOrder(String jwt,String orderNumber);

    Result getOrderListByUserId(Long id);

    Result deleteOrderByOrderNumber(String orderNumber, Long userId);

    Result getAllOrder(String startDate, String endDate,String orderNumber,Long status, Integer pageNum, Integer pageSize);

    Result deleteOrder(Long id);

    Result updateExpress(Long id, String express);

}
