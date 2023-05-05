package top.naccl.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author wdd
 * @Date 2023/5/5 9:17
 * @PackageName:top.naccl.model.vo
 * @ClassName: ProductInventory
 * @Version 1.0
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductInventory {
    private Long id;// 商品id
    private Long quantity;// 上架总数量
    private String productName;//商品名称
    private String image;//商品图
    private String code;//商品货号
    private String color;//商品颜色
}
