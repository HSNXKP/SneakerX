package top.naccl.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.naccl.annotation.AccessLimit;
import top.naccl.config.properties.PaymentConstants;
import top.naccl.constant.JwtConstants;
import top.naccl.entity.Order;
import top.naccl.entity.User;
import top.naccl.mapper.OrderMapper;
import top.naccl.model.vo.OrderVo;
import top.naccl.model.vo.Result;
import top.naccl.service.OrderService;
import top.naccl.service.impl.OrderServiceImpl;
import top.naccl.service.impl.UserServiceImpl;
import top.naccl.util.JwtUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * @author: wdd
 * @date: 2023/4/6 22:17
 */
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    UserServiceImpl userService;

    /**
     * 提交订单 需要比对当前的token和id是否一致
     *
     * @param order
     * @param jwt
     * @return
     */
    @AccessLimit(seconds = 10, maxCount = 1, msg = "10秒内只能提交一次订单")
    @PostMapping("user/order")
    public Result summitOrder(@RequestBody OrderVo order,
                              @RequestHeader(value = "Authorization", defaultValue = "") String jwt) {
        try {
            return orderService.summitOrder(order, jwt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("user/cancelOrder")
    public Result cancelOrder(@RequestHeader(value = "Authorization", defaultValue = "") String jwt,
                              @RequestParam("orderNumber") String orderNumber,Long userId) throws Exception {
        return orderService.cancelOrder(jwt, orderNumber,userId);
    }


    @PostMapping("user/order/{orderNumber}")
    public Result getOrderByOrderNumber(@RequestHeader(value = "Authorization", defaultValue = "") String jwt,
                                        @PathVariable("orderNumber") String orderNumber,@RequestParam("userId") Long userId) {
        try {
            return orderService.getOrderByOrderNumber(jwt, orderNumber,userId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("user/pay")
    public Result pay(@RequestHeader(value = "Authorization", defaultValue = "") String jwt,
                      String orderNumber,String orderRemarks) throws Exception {
        //TODO 没有校验UserId和token的关系
        if (JwtUtils.judgeTokenIsExist(jwt)) {
            // 比对当前的token和id是否一致
            String subject = JwtUtils.getTokenBody(jwt).getSubject();
            String username = subject.replace(JwtConstants.ADMIN_PREFIX, "");
            //判断token是否为blogToken
            User userDetails = (User) userService.loadUserByUsername(username);
            if (userDetails != null) {
                Order order = orderMapper.getOrderByOrderNumberWithUserId(orderNumber, userDetails.getId(), -1L);
                if (order != null) {
                    if (order.getStatus() == 0) {
                        if (order.getUserId().equals(userDetails.getId())) {
                            // 创建时间后的30分钟内有效
                            if (order.getCreateTime().toEpochSecond(ZoneOffset.ofHours(8)) + 30 * 60 > System.currentTimeMillis() / 1000) {
                                AlipayClient alipayClient = new DefaultAlipayClient(
                                        PaymentConstants.serverUrl,
                                        PaymentConstants.APP_ID,
                                        PaymentConstants.PRIVATE_KEY,
                                        "json",
                                        "UTF-8",
                                        PaymentConstants.PUBLIC_KEY, "RSA2");
                                AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
                                //异步接收地址，仅支持http/https，公网可访问
                                request.setNotifyUrl(PaymentConstants.notifyUrl);
                                //同步跳转地址，仅支持http/https
                                request.setReturnUrl(PaymentConstants.returnUrl);
                                /******必传参数******/
                                JSONObject bizContent = new JSONObject();
                                //商户订单号，商家自定义，保持唯一性
                                bizContent.put("out_trade_no", order.getOrderNumber());
                                //支付金额，最小值0.01元
                                bizContent.put("total_amount", order.getAmount());
                                //订单标题，不可使用特殊符号
                                bizContent.put("subject", "SneakerX订单");
                                //电脑网站支付场景固定传值FAST_INSTANT_TRADE_PAY
                                bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");

                                /******可选参数******/
                                //bizContent.put("time_expire", "2022-08-01 22:00:00");

                                //// 商品明细信息，按需传入
                                //JSONArray goodsDetail = new JSONArray();
                                //JSONObject goods1 = new JSONObject();
                                //goods1.put("goods_id", "goodsNo1");
                                //goods1.put("goods_name", "子商品1");
                                //goods1.put("quantity", 1);
                                //goods1.put("price", 0.01);
                                //goodsDetail.add(goods1);
                                //bizContent.put("goods_detail", goodsDetail);

                                //// 扩展信息，按需传入
                                //JSONObject extendParams = new JSONObject();
                                //extendParams.put("sys_service_provider_id", "2088511833207846");
                                //bizContent.put("extend_params", extendParams);
                                request.setBizContent(bizContent.toString());
                                AlipayTradePagePayResponse response = alipayClient.pageExecute(request);
                                // 增加订单备注
                                order.setOrderRemarks(orderRemarks);
                                orderMapper.updateOrder(order);
                                if (response.isSuccess()) {
                                    System.out.println(response.getBody());
                                    System.out.println("调用成功");
                                } else {
                                    System.out.println("调用失败");
                                }
                                return Result.ok("支付订单生成", response.getBody());
                            }
                            // 订单过期
                            order.setStatus(4L);
                            orderMapper.updateOrder(order);
                            return Result.error("订单已过期");
                        }
                        return Result.error("您没有此订单的权限");
                    }
                    return Result.error("订单已支付");
                }
                return Result.error("订单不存在");

            }
            return Result.error("token无效，请重新登录");
        }
        return Result.error("token无效，请重新登录");
    }


    @RequestMapping("/alipay/notify")
    public String payReturn(HttpServletRequest request) throws Exception {
        try {
            // 拿到支付宝POST过来反馈信息
            Map<String, String[]> params = request.getParameterMap();
            Map<String, String> map = new HashMap<>();
            // 遍历参数转换成map
            for (String s : params.keySet()) {
                map.put(s, params.get(s)[0]);
            }
            System.out.println(map);
            // 验证签名  支付宝公钥
            boolean signVerified = AlipaySignature.rsaCheckV1(map, PaymentConstants.PUBLIC_KEY, "UTF-8", "RSA2");
            if (signVerified) {
                AlipayTradeQueryResponse queryResponse = queryIsSuccess(map.get("out_trade_no"));
                if (queryResponse.getTotalAmount().equals(map.get("total_amount"))) {
                    if (map.get("trade_status").equals("TRADE_SUCCESS")) {
                        String out_trade_no = map.get("out_trade_no");
                        String trade_no = map.get("trade_no");
                        Order order = orderMapper.getOrderByOrderNumberWithUserId(out_trade_no, null, -1L);
                        List<Order> orderList = orderMapper.getOrderListByOrderNumberWithUserId(out_trade_no, order.getUserId(), order.getId());
                        // 更新订单状态已支付 将支付宝交易号存入数据库
                        order.setPayTradeNo(trade_no);
                        orderService.setOrderPayed(order);
                        if (orderList.size() > 0) {
                            for (Order one : orderList) {
                                orderMapper.updateOrder(one);
                            }
                            return "success";
                        }
                        return "success";
                    }
                    return "failure";
                }
                throw new RuntimeException("支付失败");
            }
            return "failure";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    AlipayTradeQueryResponse queryIsSuccess(String out_trade_no) throws Exception {
        AlipayClient alipayClient = new DefaultAlipayClient(
                PaymentConstants.serverUrl,
                PaymentConstants.APP_ID,
                PaymentConstants.PRIVATE_KEY,
                "json",
                "UTF-8",
                PaymentConstants.PUBLIC_KEY,
                "RSA2");
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject bizContent = new JSONObject();
        //商户订单号，商家自定义，保持唯一性
        bizContent.put("out_trade_no", out_trade_no);
        request.setBizContent(bizContent.toString());
        AlipayTradeQueryResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        return response;
    }


    @GetMapping("user/getOrder")
    public Result getOrderByUserId(@RequestHeader(value = "Authorization", defaultValue = "") String jwt,
                                   @RequestParam("id") Long id,@RequestParam("status") Long status) {
        try {
            if (JwtUtils.judgeTokenIsExist(jwt)) {
                // 比对当前的token和id是否一致
                String subject = JwtUtils.getTokenBody(jwt).getSubject();
                String username = subject.replace(JwtConstants.ADMIN_PREFIX, "");
                //判断token是否为blogToken
                User userDetails = (User) userService.loadUserByUsername(username);
                if (userDetails != null) {
                    if (userDetails.getId().equals(id)) {
                        return orderService.getOrderListByUserId(id,status);
                    }
                    return Result.error("token无效，请重新登陆");
                }
                return Result.error("token无效，请重新登录");
            }
            return Result.error("token无效，请重新登录");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除订单
     * @param orderNumber
     * @param userId
     * @return
     */
    @GetMapping("user/deleteOrderByOrderNumber")
    public Result deleteOrderByOrderNumber(@RequestParam("orderNumber")String orderNumber,@RequestParam("userId") Long userId){
        return orderService.deleteOrderByOrderNumber(orderNumber,userId);
    }

    /**
     * 申请退款
     * @param order
     * @return
     */
    @PostMapping("user/requestRefund")
    public Result requestRefund(@RequestBody Order order){
        return orderService.requestRefund(order);
    }

    /**
     * 取消退款
     * @param orderNumber
     * @param userId
     * @return
     */
    @GetMapping("user/cancelRefund")
    public Result cancelRefund(@RequestParam("orderNumber")String orderNumber,@RequestParam("userId") Long userId){
        return orderService.cancelRefund(orderNumber,userId);
    }

    /**
     * 确认收货
     * @param orderNumber
     * @param userId
     * @return
     */
    @GetMapping("user/confirmReceipt")
    public Result confirmReceipt(@RequestParam("orderNumber")String orderNumber,@RequestParam("userId") Long userId){
        return orderService.confirmReceipt(orderNumber,userId);
    }



}
