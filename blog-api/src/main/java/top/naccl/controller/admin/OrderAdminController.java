package top.naccl.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.config.properties.PaymentConstants;
import top.naccl.entity.Order;
import top.naccl.mapper.OrderMapper;
import top.naccl.model.vo.Result;
import top.naccl.service.OrderService;
import top.naccl.service.impl.OrderServiceImpl;

import java.util.List;

/**
 * @Author wdd
 * @Date 2023/4/26 17:21
 * @PackageName:top.naccl.controller.admin
 * @ClassName: OrderAdminController
 * @Version 1.0
 */
@RestController
@RequestMapping("/admin")
public class OrderAdminController {

    @Autowired
    private OrderService orderService;


    @Autowired
    private OrderMapper orderMapper;

    @GetMapping("/getAllOrder")
    public Result getAllOrder(@RequestParam(defaultValue = "") String[] date,
                              @RequestParam String orderNumber,
                              @RequestParam Long status,
                              @RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize){
        String startDate = null;
        String endDate = null;
        if (date.length == 2) {
            startDate = date[0];
            endDate = date[1];
        }
        return orderService.getAllOrder(startDate,endDate,orderNumber,status,pageNum,pageSize);
    }

    @GetMapping("/deleteOrder")
    public Result deleteOrder(@RequestParam Long id){
        return orderService.deleteOrder(id);
    }

    @GetMapping("/updateExpress")
    public Result updateOrder(@RequestParam("id") Long id,@RequestParam("express") String express){
        return orderService.updateExpress(id,express);
    }


    /**
     * 支付宝退款
     * @param orderNumber
     * @param userId
     * @return
     * @throws AlipayApiException
     */
    @GetMapping("refund")
    public Result refund(@RequestParam("orderNumber")String orderNumber,@RequestParam("userId") Long userId) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(
                PaymentConstants.serverUrl,
                PaymentConstants.APP_ID,
                PaymentConstants.PRIVATE_KEY,
                "json",
                "GBK",
                PaymentConstants.PUBLIC_KEY,
                "RSA2");
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        JSONObject bizContent = new JSONObject();
        Order order = orderMapper.getOrderByOrderNumberWithUserId(orderNumber, userId, -1L);
        bizContent.put("trade_no",order.getPayTradeNo());
        bizContent.put("refund_amount",order.getRefundAmount());
        bizContent.put("out_request_no", order.getOrderNumber());
        List<Order> orderList = orderMapper.getOrderListByOrderNumberWithUserId(orderNumber, userId, order.getId());
//// 返回参数选项，按需传入
//JSONArray queryOptions = new JSONArray();
//queryOptions.add("refund_detail_item_list");
//bizContent.put("query_options", queryOptions);
        request.setBizContent(bizContent.toString());
        AlipayTradeRefundResponse response = alipayClient.execute(request);
        if(response.isSuccess()){
            order.setStatus(6L);
            orderMapper.updateOrder(order);
            if (orderList.size() > 0){
                for (Order one : orderList) {
                    one.setStatus(6L);
                    orderMapper.updateOrder(one);
                }
                return Result.ok("退款成功");
            }
            return Result.ok("退款成功");
        } else {
            return Result.error("退款失败");
        }
    }


    /**
     * 拒绝退款
     * @param orderNumber
     * @param userId
     * @return
     */
    @GetMapping("/refuseRefund")
    public Result refuseRefund(@RequestParam("orderNumber")String orderNumber,@RequestParam("userId") Long userId){
        return orderService.refuseRefund(orderNumber,userId);
    }

}
