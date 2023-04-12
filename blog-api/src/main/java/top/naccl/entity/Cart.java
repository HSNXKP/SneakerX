package top.naccl.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author: wdd
 * @date: 2023/4/1 14:54
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Cart {
    private Long id;// 购物车ID
    private Long userId;// 用户ID
    private Long productId;// 商品ID
    private Long productCategoryId;// 商品分类ID
    private Long quantity;// 数量
    private Boolean checked;// 是否选中
    private String productPrice;// 商品价格
    private Double amount;// 总价格
    private LocalDateTime createdTime;//创建时间
    private LocalDateTime updateTime;//更新时间

}
