package top.naccl.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @Author wdd
 * @Date 2023/3/28 10:27
 * @PackageName:top.naccl.model.vo
 * @ClassName: BlogWithMomentView
 * @Version 1.0
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BlogWithMomentView {
    private Long id;
    private String title;//动态标题
    private Date createTime;//创建时间
    private Boolean isPublished;//公开或私密
    private String password;//密码保护
    private Boolean privacy;//密码羁绊
    private Integer likes;//点赞数
}
