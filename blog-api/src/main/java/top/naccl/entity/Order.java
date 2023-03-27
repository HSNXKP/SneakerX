package top.naccl.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.tomcat.jni.Local;
import org.apache.tomcat.jni.Time;
import top.naccl.util.upload.channel.LocalChannel;

import java.time.LocalDateTime;

/**
 * @author: wdd
 * @date: 2023/3/25 14:57
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Order {
    private String id;// 订单Id
    private Long userId;// 用户Id
    private Long productId;// 商品Id
    private Long quantity;// 数量
    private Double amount;// 总价格
    private LocalDateTime createdTime;//创始时间
    private LocalDateTime updateTime;//更新时间

}
