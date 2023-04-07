package top.naccl.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author: wdd
 * @date: 2023/4/1 16:25
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductSize {
    private Long id;// 商品详情ID
    private String name;// 商品属性名称(尺码)
    private Long productId;// 商品id
    private Long productCount;// 商品数量
    private Long productPrice; // 商品价格

}
