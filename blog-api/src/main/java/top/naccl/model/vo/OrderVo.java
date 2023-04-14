package top.naccl.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @Description: 前端交互订单对象
 * @author: wdd
 * @date: 2023/4/6 23:42
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderVo {
    private Long id;// Id
    private Long userId;// 用户Id
    private Long productId;// 商品Id
    private Long quantity;// 数量
    private Long payType;// 支付方式 1:微信支付 2:支付宝支付
    private Boolean delivery;// 紧急发货
    private Boolean isDefaultAddress;//是否默认地址
    private Long sizeWithPrice;//尺码和价格 对应ProductSizeId
    private Long address;//收货地址 对应addressId
    private String orderRemarks;// 订单说明（订单备注）
    private String type;// 购物车订单：cartOrder 单个订单：order
}
