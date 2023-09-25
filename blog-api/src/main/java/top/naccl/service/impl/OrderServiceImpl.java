package top.naccl.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.aspectj.weaver.ast.Or;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import top.naccl.constant.JwtConstants;
import top.naccl.entity.*;
import top.naccl.exception.NotFoundException;
import top.naccl.mapper.AddressMapper;
import top.naccl.mapper.CartMapper;
import top.naccl.mapper.OrderMapper;
import top.naccl.mapper.ProductSizeMapper;
import top.naccl.model.vo.OrderAminVo;
import top.naccl.model.vo.OrderListVo;
import top.naccl.model.vo.OrderVo;
import top.naccl.model.vo.Result;
import top.naccl.service.AddressService;
import top.naccl.service.CartService;
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

    @Autowired
    private CartMapper cartMapper;

    /**
     * 提交订单
     *
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
                        if (orderVo.getType().equals("order")){
                        // 判断当前的商品是否还有存货
                        // 通过订单的priceWithSize查村出来的价格
                        ProductSize productSize = productSizeMapper.getProductSizeById(orderVo.getSizeWithPrice());
                        if (productSize.getProductCount() != 0) {
                            // 查询当前的用户下单数量
                            int productOrderCount = orderMapper.getProductOrderCountByUserId(orderVo.getUserId(), orderVo.getProductId());
                            Product product = productService.getProductById(orderVo.getProductId());
                            if (productOrderCount < product.getPurchaseRestrictions()) {
                                // 判断当前下单的商品数量是否超过限购数量
                                if (orderVo.getQuantity() + productOrderCount <= product.getPurchaseRestrictions()) {
                                    if (orderVo.getIsDefaultAddress()) {
                                        addressMapper.setOtherAddressNotDefault(orderVo.getUserId());
                                        addressMapper.setAddressDefault(orderVo.getAddress());
                                    }
                                    Address address = addressMapper.getAddressById(orderVo.getAddress());
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
                                    order.setAddress(address.getName() +  address.getPhone()+  "," + address.getAddress()  + address.getAddressDetail());
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
                                    // 当前是单个商品提交
                                    order.setParentId(-1L);
                                    // 提交订单
                                    if (orderMapper.summitOrder(order) == 1) {
                                        // 提交订单成功后，减少商品的库存
                                        if (productSizeMapper.reduceProductCount(orderVo.getSizeWithPrice(),orderVo.getQuantity())) {
                                            return Result.ok("提交订单成功", orderNumber);
                                        }
                                    }
                                    return Result.error("提交订单失败");
                                }
                                return Result.error("该商品:"+product.getName()+"限购"+product.getPurchaseRestrictions()+"个");
                            }
                            return Result.error("该商品:"+product.getName()+"限购"+product.getPurchaseRestrictions()+"个");

                        }
                        return Result.error("当前商品已售罄");

                    }else {
                            // 不是商品订单  就是购物车订单
                            List<Cart> cartList = cartMapper.getCartByUserId(orderVo.getUserId(),true);
                            Long quantity = 0L;
                            Double amount = 0D;
                            // 设置主单号提交
                            Order order = new Order();
                            for (Cart cart : cartList) {
                                ProductSize productSize = productSizeMapper.getProductSizeById(cart.getProductSizeId());
                                // 商品的尺码价格不能为空
                                if (productSize.getProductCount() != 0){
                                    // 拿到当前用户下单商品的总数
                                    int productOrderCount = orderMapper.getProductOrderCountByUserId(orderVo.getUserId(), cart.getProductId());
                                        Product product = productService.getProductById(cart.getProductId());
                                        // 总数小于当前商品的限购数量
                                        if (productOrderCount < product.getPurchaseRestrictions()) {
                                            // 判断当前下单的商品数量是否超过限购数量
                                            if (cart.getQuantity() + productOrderCount <= product.getPurchaseRestrictions()){
                                                quantity = quantity + cart.getQuantity();
                                                amount = amount + cart.getAmount();
                                            }else {
                                                return Result.error("该商品:"+product.getName()+"限购"+product.getPurchaseRestrictions()+"个");
                                            }

                                        }else {
                                            return Result.error("该商品:"+product.getName()+"限购"+product.getPurchaseRestrictions()+"个");
                                        }
                                }else {
                                    return Result.error("当前商品已售罄");
                                }
                            }
                            Address address = addressMapper.getAddressById(orderVo.getAddress());
                            // 设置订单的用户id
                            order.setUserId(orderVo.getUserId());
                            // 设置订单的商品id
                            order.setProductId(null);
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
                            order.setPrice(null);
                            // 设置订单的尺码
                            order.setSize(null);
                            // 设置订单的数量
                            order.setQuantity(quantity);
                            // 设置订单的总价
                            order.setAmount(amount);
                            // 设置订单的收货地址
                            order.setAddress(address.getName() + "," +  address.getPhone()+  "," + address.getAddress() + "," + address.getAddressDetail());
                            // 设置订单的支付方式
                            order.setPayType(orderVo.getPayType());
                            // 设置支付时间
                            order.setPayTime(null);
                            // 设置订单的发货时间
                            order.setDeliveryTime(null);
                            // 设置订单的备注
                            order.setOrderRemarks(orderVo.getOrderRemarks());
                            // 设置紧急发货
                            order.setDelivery(null);
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
                            // 当前是单个商品提交
                            order.setParentId(-1L);
                            if (orderMapper.summitOrder(order) == 1){
                                // 拿出刚才插入的主订单
                                Order orderParent = orderMapper.getOrderByOrderNumberWithUserId(order.getOrderNumber(), order.getUserId(), order.getParentId());
                                // 如果都没有问题设置子订单
                                for (Cart cart : cartList) {
                                    Order oneOrder = new Order();
                                    // 设置订单的用户id
                                    oneOrder.setUserId(orderVo.getUserId());
                                    // 设置订单的商品id
                                    oneOrder.setProductId(cart.getProductId());
                                    // 设置订单的创建时间
                                    oneOrder.setCreateTime(LocalDateTime.now());
                                    // 设置订单的更新时间
                                    oneOrder.setUpdateTime(LocalDateTime.now());
                                    // 设置订单的状态
                                    oneOrder.setStatus(0L);
                                    // 设置订单的快递单号
                                    oneOrder.setExpress("");
                                    // 设置子订单的订单号固定-1
                                    oneOrder.setOrderNumber("-1");
                                    // 设置订单的价格
                                    oneOrder.setPrice(cart.getPrice());
                                    // 设置订单的尺码
                                    oneOrder.setSize(cart.getSize());
                                    // 设置订单的数量
                                    oneOrder.setQuantity(cart.getQuantity());
                                    // 设置订单的总价
                                    oneOrder.setAmount(cart.getAmount());
                                    // 设置订单的收货地址
                                    oneOrder.setAddress(address.getName() + "," +  address.getPhone()+  "," + address.getAddress() + "," + address.getAddressDetail());
                                    // 设置订单的支付方式
                                    oneOrder.setPayType(orderVo.getPayType());
                                    // 设置支付时间
                                    oneOrder.setPayTime(null);
                                    // 设置订单的发货时间
                                    oneOrder.setDeliveryTime(null);
                                    // 设置订单的备注
                                    oneOrder.setOrderRemarks(orderVo.getOrderRemarks());
                                    // 设置紧急发货
                                    oneOrder.setDelivery(null);
                                    // 设置完成时间
                                    oneOrder.setFinishTime(null);
                                    // 设置取消时间
                                    oneOrder.setCancelTime(null);
                                    // 设置退款原因
                                    oneOrder.setRefundReason(null);
                                    // 设置退款时间
                                    oneOrder.setRefundTime(null);
                                    // 设置退款状态
                                    oneOrder.setRefundStatus(0L);
                                    // 设置退款金额
                                    oneOrder.setRefundAmount(null);
                                    // 设置退款单号
                                    oneOrder.setRefundNo(null);
                                    // 设置退款时限
                                    oneOrder.setRefundTimeLimit(null);
                                    // 设置退款备注
                                    oneOrder.setRefundRemarks(null);
                                    // 当前是单个商品提交
                                    oneOrder.setParentId(orderParent.getId());
                                    // 循环插入
                                    orderMapper.summitOrder(oneOrder);
                                    cartMapper.deleteCartById(cart.getId(),cart.getUserId());
                                }
                                // 删除购物车
                                return Result.ok("提交订单成功",orderParent.getOrderNumber());
                            }
                            return Result.error("提交订单失败");
                        }

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
    public Result getOrderByOrderNumber(String jwt, String orderNumber,Long userId) {
        try {
            if (JwtUtils.judgeTokenIsExist(jwt)) {
                // 比对当前的token和id是否一致
                String subject = JwtUtils.getTokenBody(jwt).getSubject();
                String username = subject.replace(JwtConstants.ADMIN_PREFIX, "");
                //判断token是否为blogToken
                User userDetails = (User) userService.loadUserByUsername(username);
                if (userDetails != null) {
                    Order order = orderMapper.getOrderByOrderNumberWithUserId(orderNumber, userDetails.getId(), -1L);
                    if (order != null) {
                        if (order.getUserId().equals(userDetails.getId())) {
                            List<Order> orderList = orderMapper.getOrderListByOrderNumberWithUserId("-1",userId, order.getId());
                            // orderList空的话就是单个商品
                            if (orderList.size() == 0) {
                                order.setChildren(null);
                                // 设置订单的商品
                                order.setProduct(productService.getProductById(order.getProductId()));
                                return Result.ok("查询成功", order);
                            }
                            // 设置商品名称
                            for (Order one : orderList) {
                                one.setProduct(productService.getProductById(one.getProductId()));
                            }
                            order.setChildren(orderList);
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
    public int setOrderPayed(Order order) {
        order.setPayTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        order.setStatus(1L);
        if (orderMapper.updateOrder(order) == 1) {
            return 1;
        }
        return 0;
    }

    @Override
    public Result cancelOrder(String jwt, String orderNumber,Long userId) {
        try {
            if (JwtUtils.judgeTokenIsExist(jwt)) {
                // 比对当前的token和id是否一致
                String subject = JwtUtils.getTokenBody(jwt).getSubject();
                String username = subject.replace(JwtConstants.ADMIN_PREFIX, "");
                //判断token是否为blogToken
                User userDetails = (User) userService.loadUserByUsername(username);
                if (userDetails != null) {
                    Order order = orderMapper.getOrderByOrderNumberWithUserId(orderNumber, userId, -1L);
                    if (order != null) {
                        if (order.getUserId().equals(userDetails.getId())) {
                            List<Order> orderList = orderMapper.getOrderListByOrderNumberWithUserId("-1", userId, order.getId());
                            if(orderList.size() == 0){
                                if (order.getStatus() == 0L) {
                                    order.setUpdateTime(LocalDateTime.now());
                                    order.setCancelTime(LocalDateTime.now());
                                    order.setStatus(4L);
                                    if (orderMapper.updateOrder(order) == 1) {
                                        return Result.ok("取消订单成功");
                                    }
                                    return Result.error("取消订单失败");
                                }
                            }else {
                                if (order.getStatus() == 0L) {
                                    order.setUpdateTime(LocalDateTime.now());
                                    order.setCancelTime(LocalDateTime.now());
                                    order.setStatus(4L);
                                    if (orderMapper.updateOrder(order) == 1) {
                                        for (Order one : orderList) {
                                            one.setUpdateTime(LocalDateTime.now());
                                            one.setCancelTime(LocalDateTime.now());
                                            one.setStatus(4L);
                                            orderMapper.updateOrder(one);
                                        }
                                        return Result.ok("取消订单成功");
                                    }
                                    return Result.error("取消订单失败");
                                }
                                return Result.error("取消订单失败");
                            }
                            return Result.error("订单异常");
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
    public Result getOrderListByUserId(Long userId,Long status){
        try {
            List<OrderListVo> orderList = getOrderListByUserId(userId, -1L,status);
            return Result.ok("获取成功", orderList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Result deleteOrderByOrderNumber(String orderNumber, Long userId) {
        Order order = orderMapper.getOrderByOrderNumberWithUserId(orderNumber, userId, -1L);
        if (order != null) {
            if (order.getStatus() == 0L || order.getStatus() == 3L || order.getStatus() == 4L) {
                if (orderMapper.deleteOrderByOrderNumber(orderNumber,userId,-1L) == 1) {
                    // 关联的也全部删除
                    orderMapper.deleteOrderByOrderNumber("-1",userId,order.getId());
                    return Result.ok("删除成功");
                }
                return Result.error("删除失败");
            }
            return Result.error("订单正在进行中无法删除");
        }
        return Result.error("没有该订单");
    }

    @Override
    public Result getAllOrder(String startDate, String endDate,String orderNumber,Long status, Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<OrderAminVo> orderList = orderMapper.getAllOrderList(startDate,endDate,orderNumber,status,-1L);
        for (OrderAminVo orderListVo : orderList) {
            List<OrderAminVo> orderListByUser = orderMapper.getAllOrderList(startDate,endDate,null,status,orderListVo.getId());
            if (orderListByUser == null) {
                orderListVo.setChildren(null);
            }
            orderListVo.setChildren(orderListByUser);
        }
        PageInfo<OrderAminVo> orderListVoPageInfo = new PageInfo<>(orderList);
        return Result.ok("获取成功",orderListVoPageInfo);
    }

    @Override
    public Result deleteOrder(Long id) {
        List<Order> order = orderMapper.getOrderById(id,null);
        if (order.get(0).getParentId() == -1L){
            if(order.get(0).getStatus() == 4L){
                List<Order> orderList = orderMapper.getOrderById(null, order.get(0).getId());
                if (orderList.size() == 0){
                    if (orderMapper.deleteOrderById(id,-1L) == 1) {
                        return Result.ok("删除成功");
                    }
                    return Result.error("删除失败");
                }
                if (orderMapper.deleteOrderById(id,null) == 1 && orderMapper.deleteOrderById(null,order.get(0).getId()) == 1) {
                    return Result.ok("删除成功");
                }
                return Result.error("删除失败");
            }
            return Result.error("订单正在进行中无法删除");
        }
        return Result.error("选择的订单不是父订单无法删除");

    }

    @Override
    public Result updateExpress(Long id, String express) {
        List<Order> order = orderMapper.getOrderById(id,null);
        if (order.get(0).getParentId() == -1L){
            if(order.get(0).getStatus() == 1L){
                    orderMapper.updateExpress(id,express,null);
                    // 修改状态
                    orderMapper.updateStatus(id,2L,null);
                    // 关联的也全部修改
                    orderMapper.updateExpress(null,express,id);
                    orderMapper.updateStatus(null,2L,id);
                    return Result.ok("修改成功");
            }else if (order.get(0).getStatus() == 2L){
                if (orderMapper.updateExpress(id,express,null) == 1) {
                    return Result.ok("修改成功");
                }
                return Result.error("修改失败");
            }
            return Result.error("订单状态无法修改");
        }else {
            if(order.get(0).getStatus() == 2L){
                if (orderMapper.updateExpress(id,express,null) == 1) {
                    // 关联的也全部修改
                    return Result.ok("修改成功");
                }
                return Result.error("修改失败");
            }
            return Result.error("请先将主订单的状态修改，再修改子订单");
        }

    }

    @Override
    public Result requestRefund(Order order) {
        Order selectOrder = orderMapper.getOrderByOrderNumberWithUserId(order.getOrderNumber(), order.getUserId(), -1L);
        if (selectOrder != null) {
            if (selectOrder.getStatus() == 1L || selectOrder.getStatus() == 2L || selectOrder.getStatus() == 3L) {
                selectOrder.setStatus(5L);
                selectOrder.setRefundRemarks(order.getRefundRemarks());
                selectOrder.setRefundReason(order.getRefundReason());
                selectOrder.setRefundAmount(selectOrder.getAmount());
                selectOrder.setRefundNo(UUID.randomUUID().toString().replace("-", ""));
                orderMapper.updateOrder(selectOrder);
                List<Order> orderList = orderMapper.getOrderListByOrderNumberWithUserId(order.getOrderNumber(), order.getUserId(), order.getId());
                for (Order oneOrder : orderList) {
                    oneOrder.setStatus(5L);
                    orderMapper.updateOrder(oneOrder);
                }
                return Result.ok("退款审核中，审核通过后48小时内完成退款");
            }
            return Result.error("订单无法申请退款");
        }
        return Result.error("没有该订单");
    }

    @Override
    public Result confirmReceipt(String orderNumber, Long userId) {
        Order order = orderMapper.getOrderByOrderNumberWithUserId(orderNumber, userId, -1L);
        if (order != null) {
            if (order.getStatus() == 2L) {
                order.setStatus(3L);
                orderMapper.updateOrder(order);
                List<Order> orderList = orderMapper.getOrderListByOrderNumberWithUserId(orderNumber, userId, order.getId());
                for (Order oneOrder : orderList) {
                    oneOrder.setStatus(3L);
                    orderMapper.updateOrder(oneOrder);
                }
                return Result.ok("确认收货成功");
            }
            return Result.error("订单无法确认收货");
        }
        return Result.error("没有该订单");
    }

    @Override
    public Result cancelRefund(String orderNumber, Long userId) {
        Order order = orderMapper.getOrderByOrderNumberWithUserId(orderNumber, userId, -1L);
        if (order != null) {
            if (order.getStatus() == 5L) {
                Long status = null;
                if (order.getExpress() == null || order.getExpress().equals(" ")) {
                    status = 1L;
                } else {
                    status = 2L;
                }
                order.setStatus(status);
                orderMapper.updateOrder(order);
                List<Order> orderList = orderMapper.getOrderListByOrderNumberWithUserId(orderNumber, userId, order.getId());
                for (Order oneOrder : orderList) {
                    oneOrder.setStatus(status);
                    orderMapper.updateOrder(oneOrder);
                }
                return Result.ok("取消退款成功");
            }
            return Result.error("订单无法取消退款");
        }
        return Result.error("没有该订单");
    }

    @Override
    public Result refuseRefund(String orderNumber, Long userId) {
        Order order = orderMapper.getOrderByOrderNumberWithUserId(orderNumber, userId, -1L);
        if (order != null) {
            if (order.getStatus() == 5L) {
                Long status = null;
                if (order.getExpress() == null || order.getExpress().equals("")) {
                    status = 1L;
                } else {
                    status = 2L;
                }
                order.setStatus(status);
                orderMapper.updateOrder(order);
                List<Order> orderList = orderMapper.getOrderListByOrderNumberWithUserId(orderNumber, userId, order.getId());
                for (Order oneOrder : orderList) {
                    oneOrder.setStatus(status);
                    orderMapper.updateOrder(oneOrder);
                }
                return Result.ok("拒绝退款成功");
            }
            return Result.error("订单无法拒绝退款");
        }
        return Result.error("没有该订单");
    }


    public List<OrderListVo> getOrderListByUserId(Long userId, Long parentId,Long status) {
        List<OrderListVo> orderList = orderMapper.getOrderListByUserId(userId, parentId,status);
        for (OrderListVo orderListVo : orderList) {
            List<OrderListVo> orderListByUser = orderMapper.getOrderListByUserId(userId, orderListVo.getId(),status);
            if (orderListByUser == null) {
                orderListVo.setChildren(null);
            }
            orderListVo.setChildren(orderListByUser);
        }
        return orderList;
    }

}
