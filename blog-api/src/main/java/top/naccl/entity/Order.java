package top.naccl.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.tomcat.jni.Local;
import org.apache.tomcat.jni.Time;
import top.naccl.util.upload.channel.LocalChannel;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: wdd
 * @date: 2023/3/25 14:57
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Order {
    private Long id;// Id
    private Long userId;// 用户Id
    private Long productId;// 商品Id
    private Long price;// 商品价格
    private String size;// 商品尺码
    private Long quantity;// 数量
    private Double amount;// 总价格
    private String address;// 收货地址
    private Long status;// 订单状态 0:未支付 1:已支付 2:已发货 3:已完成 4:已取消 5:退款中 6:已退款
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
    private Double refundAmount;// 退款金额
    private String refundNo;// 退款单号
    private String refundTimeLimit;// 退款时限
    private String refundRemarks;// 退款说明
    private String orderNumber;// 订单编号
    private String payTradeNo;// 支付宝流水号
    private LocalDateTime createTime;//创始时间
    private LocalDateTime updateTime;//更新时间
    private Long parentId;// 多个商品 否-1

    private Product product;//商品
    private List<Order> children;// 多商品下单

}
