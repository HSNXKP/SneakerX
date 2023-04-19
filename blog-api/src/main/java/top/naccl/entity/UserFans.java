package top.naccl.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author: wdd
 * @date: 2023/4/19 21:19
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserFans {
    private Long id;
    private Long userId;
    private Long fansId;
    private LocalDateTime createTime;
}
