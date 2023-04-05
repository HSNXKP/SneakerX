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
public class ProductInfo {
    private Long id;// 商品详情ID
    private Long productCategoryId;// 商品分类ID
    private String productId;// 商品名称
    private String name;// 商品属性名称
    private Long productCount;// 商品数量

}
