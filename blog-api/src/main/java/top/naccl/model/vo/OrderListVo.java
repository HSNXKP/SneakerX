package top.naccl.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author wdd
 * @Date 2023/4/10 14:25
 * @PackageName:top.naccl.model.vo
 * @ClassName: OrderListVo
 * @Version 1.0
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderListVo {
    private Long id;
    private String orderNumber;
    private String name;
    private String image;
    private String code;
    private Long price;
    private Long quantity;
    private String size;
    private Double amount;
    private Long parentId;
    private Long status;
    private String express;
    private LocalDateTime createTime;

    private List<OrderListVo> children;


}
