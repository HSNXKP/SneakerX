package top.naccl.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

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
    private String description;//商品描述
    private String price;// 商品价格
    private String imageUrl;// 商品链接
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//更新时间

    private Blog blog;//(非必选)关联的动态

}
