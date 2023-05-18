package top.naccl.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

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
    private String name;// 商品名称
    private String productCategoryName;// 商品分类名称
    private String code;// 商品货号
    private String image;// 商品图片
    private Long userId;// 用户ID
    private Long productId;// 商品ID
    private Long productCategoryId;// 商品分类ID
    private Long productSizeId;// 商品尺码价格ID
    private Long quantity;// 数量
    private Boolean checked;// 是否选中
    private Long price;// 商品价格
    private String size;// 尺码
    private Double amount;// 单个总价格
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//更新时间

    private List<Cart> cartList;
}
