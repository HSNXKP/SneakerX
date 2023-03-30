package top.naccl.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 动态
 * @Author: wdd
 * @Date: 2020-07-26
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Blog {
	private Long id;
	private String title;//动态标题
	private String firstPicture;//动态首图，用于随机文章展示
	private String content;//动态正文
	private String description;//描述
	private Boolean published;//公开或私密
	private Boolean recommend;//推荐开关
	private Boolean appreciation;//赞赏开关
	private Boolean commentEnabled;//评论开关
	private Boolean top;//是否置顶
	private Date createTime;//创建时间
	private Date updateTime;//更新时间
	private Integer views;//浏览次数
	private Integer words;//动态字数
	private Integer readTime;//阅读时长(分钟)
	private String password;//密码保护
	private Integer likes;//点赞数

	private User user;//动态作者
	private Category category;//动态分类
	private List<Tag> tags = new ArrayList<>();//动态标签
}
