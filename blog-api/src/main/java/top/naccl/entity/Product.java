package top.naccl.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author: wdd
 * @date: 2023/3/25 14:54
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Product {
    private Long id; //商品ID
    private String name;//商品名称
    private String image;//商品图片
    private String description;//商品描述
    private String code;//商品货号
    private String color;//商品配色
    private String retail;//零售价
    private Long   price;// 商品价格
    private String imageUrl;// 商品链接
    private Boolean recommend;// 是否上推荐列表
    private Boolean published;// 是否上架
    private Integer purchaseRestrictions;// 限购数量
    private Long productCategoryId;// 商品分类id
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//更新时间

    private Blog blog;//(非必选)关联的动态

}
