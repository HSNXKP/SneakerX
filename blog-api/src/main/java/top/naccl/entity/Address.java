package top.naccl.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author: wdd
 * @date: 2023/3/25 20:31
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Address {
    private Long id;// 地址id
    private Long userId;// 用户id
    private String name;// 收货人姓名
    private String phone;// 收货人电话
    private String address;// 收货人地址
    private String addressDetail;// 收货人地址
    private Boolean isDefaultAddress;// 是否默认地址
    private LocalDateTime createTime;// 创建时间
    private LocalDateTime updateTime;// 更新时间

}
