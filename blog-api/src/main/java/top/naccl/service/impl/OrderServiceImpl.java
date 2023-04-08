package top.naccl.service.impl;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import top.naccl.constant.JwtConstants;
import top.naccl.entity.*;
import top.naccl.exception.NotFoundException;
import top.naccl.mapper.AddressMapper;
import top.naccl.mapper.OrderMapper;
import top.naccl.mapper.ProductSizeMapper;
import top.naccl.model.vo.OrderVo;
import top.naccl.model.vo.Result;
import top.naccl.service.AddressService;
import top.naccl.service.OrderService;
import top.naccl.service.ProductService;
import top.naccl.util.HashUtils;
import top.naccl.util.JwtUtils;
import top.naccl.util.StringUtils;

import javax.tools.Tool;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author: wdd
 * @date: 2023/4/6 22:18
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private ProductSizeMapper productSizeMapper;

    @Autowired
    private ProductService productService;

    /**
     * 提交订单
     * @param orderVo
     * @param jwt
     * @return
     */
    @Override
    public Result summitOrder(OrderVo orderVo, String jwt) {
        try {
            if (JwtUtils.judgeTokenIsExist(jwt)) {
                // 比对当前的token和id是否一致
                String subject = JwtUtils.getTokenBody(jwt).getSubject();
                String username = subject.replace(JwtConstants.ADMIN_PREFIX, "");
                //判断token是否为blogToken
                User userDetails = (User) userService.loadUserByUsername(username);
                if (userDetails != null) {
                    if (userDetails.getId().equals(orderVo.getUserId())) {
                        // 判断当前的商品是否还有存货
                        // 通过订单的priceWithSize查村出来的价格
                        ProductSize productSize = productSizeMapper.getProductSizeById(orderVo.getSizeWithPrice());
                        if (productSize.getProductCount() != 0) {
                            // 查询当前的用户下单数量
                            int productOrderCount = orderMapper.getProductOrderCountByUserId(orderVo.getUserId(), orderVo.getProductId());
                            Product product = productService.getProductById(orderVo.getProductId());
                            if (productOrderCount < product.getPurchaseRestrictions()) {
                                if (orderVo.getIsDefaultAddress()) {
                                    addressMapper.setOtherAddressNotDefault(orderVo.getUserId());
                                    addressMapper.setAddressDefault(orderVo.getAddress());
                                }
                                Order order = new Order();
                                // 设置订单的用户id
                                order.setUserId(orderVo.getUserId());
                                // 设置订单的商品id
                                order.setProductId(orderVo.getProductId());
                                // 设置订单的创建时间
                                order.setCreateTime(LocalDateTime.now());
                                // 设置订单的更新时间
                                order.setUpdateTime(LocalDateTime.now());
                                // 设置订单的状态
                                order.setStatus(0L);
                                // 设置订单的快递单号
                                order.setExpress("");
                                String orderNumber = UUID.randomUUID().toString().replace("-", "");
                                // 设置订单的订单号
                                order.setOrderNumber(orderNumber);
                                // 设置订单的价格
                                order.setPrice(productSize.getProductPrice());
                                // 设置订单的尺码
                                order.setSize(productSize.getName());
                                // 设置订单的数量
                                order.setQuantity(orderVo.getQuantity());
                                // 设置订单的总价
                                order.setAmount((double) (productSize.getProductPrice() * (orderVo.getQuantity())));
                                // 设置订单的收货地址
                                order.setAddressId(orderVo.getAddress());
                                // 设置订单的支付方式
                                order.setPayType(orderVo.getPayType());
                                // 设置支付时间
                                order.setPayTime(null);
                                // 设置订单的发货时间
                                order.setDeliveryTime(null);
                                // 设置订单的备注
                                order.setOrderRemarks(orderVo.getOrderRemarks());
                                // 设置紧急发货
                                order.setDelivery(orderVo.getDelivery());
                                // 设置完成时间
                                order.setFinishTime(null);
                                // 设置取消时间
                                order.setCancelTime(null);
                                // 设置退款原因
                                order.setRefundReason(null);
                                // 设置退款时间
                                order.setRefundTime(null);
                                // 设置退款状态
                                order.setRefundStatus(0L);
                                // 设置退款金额
                                order.setRefundAmount(null);
                                // 设置退款单号
                                order.setRefundNo(null);
                                // 设置退款时限
                                order.setRefundTimeLimit(null);
                                // 设置退款备注
                                order.setRefundRemarks(null);
                                // 提交订单
                                if (orderMapper.summitOrder(order) == 1) {
                                    // 提交订单成功后，减少商品的库存
                                    if (productSizeMapper.reduceProductCount(orderVo.getSizeWithPrice())) {
                                        return Result.ok("提交订单成功", orderNumber);
                                    }
                                }
                                return Result.error("提交订单失败");
                            }
                            return Result.error("当前商品已达到购买上限");

                        }
                        return Result.error("当前商品已售罄");

                    }
                    return Result.error("登录当前的账号与信息不符，请重新登录");
                }
                return Result.error("token无效，请重新登录");
            }

            return Result.error("token无效，请重新登录");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Result getOrderByOrderNumber(String jwt,String orderNumber) {
        try {
            if (JwtUtils.judgeTokenIsExist(jwt)) {
                // 比对当前的token和id是否一致
                String subject = JwtUtils.getTokenBody(jwt).getSubject();
                String username = subject.replace(JwtConstants.ADMIN_PREFIX, "");
                //判断token是否为blogToken
                User userDetails = (User) userService.loadUserByUsername(username);
                if (userDetails != null) {
                    Order order = orderMapper.getOrderByOrderNumber(orderNumber);
                    if (order != null) {
                        if (order.getUserId().equals(userDetails.getId())){
                            // 设置订单的收货地址
                            order.setAddress(addressMapper.getAddressByID(order.getAddressId()));
                            // 设置商品名称
                            order.setProduct(productService.getProductById(order.getProductId()));
                            return Result.ok("查询成功", order);
                        }
                        return Result.error("您没有此订单的权限");
                    }
                    return Result.error("没有该订单");
                }
                return Result.error("token无效，请重新登录");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Result.error("token无效，请重新登录");
    }

    @Override
    public int updateOrder(Order order) {
        order.setPayTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        order.setStatus(1L);
        if (orderMapper.updateOrder(order) == 1){
            return 1;
        }
        return 0;
    }
}
