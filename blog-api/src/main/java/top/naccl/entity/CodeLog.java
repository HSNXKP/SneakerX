package top.naccl.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author: wdd
 * @date: 2023/5/19 14:59
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CodeLog {
    private String msgId;//消息ID
    private String email;//接收邮箱
    private Integer status;//状态0发送中，1发送成功，2发送失败
    private String routeKey;//路由键
    private String exchange;//交换机
    private Integer count;//重试次数
    private LocalDateTime tryTime;//重试时间
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//更新时间
}
