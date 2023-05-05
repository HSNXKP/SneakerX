package top.naccl.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author wdd
 * @Date 2023/5/5 13:24
 * @PackageName:top.naccl.model.vo
 * @ClassName: ProductInventoryInfo
 * @Version 1.0
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductInventoryInfo {
    private Long id;// 规格id
    private Long productId;// 商品id
    private String productSizeName;//规格名称
    private Long productPrice;// 商品价格
    private Long quantity;// 上架数量
    private String productName;//商品名称
    private String productImage;//商品图
    private String productCode;//商品货号
}
