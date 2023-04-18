package top.naccl.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import top.naccl.entity.Category;
import top.naccl.entity.Tag;
import top.naccl.entity.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 博客简要信息
 * @Author: wdd
 * @Date: 2020-08-08
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BlogInfo {
	//首页展示的博客信息getBlogList用到的
	private Long id;
	private String title;//文章标题
	private String description;//描述
	private Date createTime;//创建时间
	private Integer views;//浏览次数
	private Integer words;//文章字数
	private Integer readTime;//阅读时长(分钟)
	private Boolean top;//是否置顶
	private String password;//文章密码
	private Boolean privacy;//是否私密文章
	private Integer likes;//点赞数
	private Integer comments;//评论数

	private User user;
	private Category category;//文章分类
	private List<Tag> tags = new ArrayList<>();//文章标签
}
