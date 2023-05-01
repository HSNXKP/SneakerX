package top.naccl.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import top.naccl.entity.Order;
import top.naccl.entity.Product;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: wdd
 * @date: 2023/5/1 17:50
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderAminVo {
    private Long id;// Id
    private Long userId;// 用户Id
    private Long productId;// 商品Id
    private Long price;// 商品价格
    private String size;// 商品尺码
    private Long quantity;// 数量
    private Double amount;// 总价格
    private String address;// 收货地址id
    private Long status;// 订单状态 0:未支付 1:已支付 2:已发货 3:已完成 4:已取消
    private Long payType;// 支付方式 1:微信支付 2:支付宝支付
    private LocalDateTime payTime;// 支付时间
    private LocalDateTime deliveryTime;// 发货时间
    private Boolean delivery;// 紧急发货
    private String orderRemarks;// 订单说明（订单备注）
    private String express;// 物流单号
    private LocalDateTime finishTime;// 完成时间
    private LocalDateTime cancelTime;// 取消时间
    private LocalDateTime refundTime;// 退款时间
    private String refundReason;// 退款原因
    private Long refundStatus;// 退款状态 0:未退款 1:已退款
    private Long refundAmount;// 退款金额
    private String refundNo;// 退款单号
    private String refundTimeLimit;// 退款时限
    private String refundRemarks;// 退款说明
    private String orderNumber;// 订单编号
    private LocalDateTime createTime;//创始时间
    private LocalDateTime updateTime;//更新时间
    private Long parentId;// 多个商品 否-1

    private String name;// 商品
    private String image;// 商品图片
    private String code;// 商品货号

    private List<OrderAminVo> children;// 多商品下单
}
