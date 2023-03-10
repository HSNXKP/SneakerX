package top.naccl.model.dto;

import lombok.*;
import top.naccl.entity.User;

import java.util.Date;

/**
 * @author: wdd
 * @date: 2023/2/21 21:33
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BlogTitleInfo {

    private Long id;
    private String title; //内容标题
    private Date createTime;//创建时间
    private Boolean published;//公开或私密
}
