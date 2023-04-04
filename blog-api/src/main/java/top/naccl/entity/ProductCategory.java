package top.naccl.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author: wdd
 * @date: 2023/4/1 14:47
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductCategory {
    private Long id;
    private String name;// 分类名称
    private String description;// 分类描述
    private String image;// 分类图片
    private Long parentId;// 父分类ID

    private List<ProductCategory> children;// 子分类

}
