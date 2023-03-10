package top.naccl.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author: wdd
 * @date: 2023/3/2 16:37
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Url {
    private Long id;
    private String url; // 对后端访问路径进行限制
}
