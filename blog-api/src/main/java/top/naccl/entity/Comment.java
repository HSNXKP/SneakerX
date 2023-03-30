package top.naccl.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import top.naccl.model.vo.BlogIdAndTitle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 博客评论
 * @Author: wdd
 * @Date: 2020-07-27
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Comment {
	private Long id;
	private String nickname;//昵称
	private String email;//邮箱
	private String content;//评论内容
	private String avatar;//头像(图片路径)
	private Date createTime;//评论时间
	private String website;//个人网站
	private String ip;//评论者ip地址
	private Boolean published;//公开或回收站
	private Boolean adminComment;//博主回复
	private Integer page;//0普通文章，1关于我页面
	private Boolean notice;//接收邮件提醒
	private Long parentCommentId;//父评论id
	private String qq;// 未登录qq标识
	private Long userId;// 登陆了就是userId标识
	private String userFlag;//userFlag前戳


	private BlogIdAndTitle blog;//所属的动态
	private List<Comment> replyComments = new ArrayList<>();//回复该评论的评论
}
