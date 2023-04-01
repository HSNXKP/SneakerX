package top.naccl.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author: wdd
 * @date: 2023/4/1 14:40
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductStock {
    private Long id;
    private Long productId;// 商品ID
    private Long stock;//库存
}
