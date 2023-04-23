package top.naccl.service.impl;

import com.github.pagehelper.PageHelper;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.naccl.constant.JwtConstants;
import top.naccl.constant.RedisKeyConstants;
import top.naccl.entity.Blog;
import top.naccl.entity.Moment;
import top.naccl.entity.User;
import top.naccl.exception.NotFoundException;
import top.naccl.exception.PersistenceException;
import top.naccl.mapper.BlogMapper;
import top.naccl.mapper.CommentMapper;
import top.naccl.mapper.MomentMapper;
import top.naccl.mapper.UserMapper;
import top.naccl.model.vo.BlogDetail;
import top.naccl.model.vo.BlogWithMomentView;
import top.naccl.model.vo.Result;
import top.naccl.service.*;
import top.naccl.util.JwtUtils;
import top.naccl.util.markdown.MarkdownUtils;

import java.util.List;

/**
 * @Description: 博客动态业务层实现
 * @Author: wdd
 * @Date: 2020-08-24
 */
@Service
public class MomentServiceImpl implements MomentService {
	@Autowired
	MomentMapper momentMapper;

	@Autowired
	BlogMapper blogMapper;

	@Autowired
	private BlogService blogService;

	@Autowired
	private CommentMapper commentMapper;

	@Autowired
	private RedisService redisService;

	@Autowired
	private UserServiceImpl userService;

	//每页显示5条动态
	private static final int pageSize = 5;
	//动态列表排序方式
	private static final String orderBy = "create_time desc";
	//私密动态提示
	private static final String PRIVATE_MOMENT_CONTENT = "<p>此条为私密动态，仅发布者可见！</p>";

	@Override
	public List<Moment> getMomentList() {
		return momentMapper.getMomentList();
	}

	@Override
	public List<Moment> getMomentVOList(Integer pageNum, boolean adminIdentity) {
		PageHelper.startPage(pageNum, pageSize, orderBy);
		List<Moment> moments = momentMapper.getMomentList();
		for (Moment moment : moments) {
			if (adminIdentity || moment.getPublished()) {
				moment.setContent(MarkdownUtils.markdownToHtmlExtensions(moment.getContent()));
			} else {
				moment.setContent(PRIVATE_MOMENT_CONTENT);
			}
		}
		return moments;
	}


	@Transactional(rollbackFor = Exception.class)
	@Override
	public void updateMomentPublishedById(Long momentId, Boolean published) {
		if (momentMapper.updateMomentPublishedById(momentId, published) != 1) {
			throw new PersistenceException("操作失败");
		}
	}

	@Override
	public Moment getMomentById(Long id) {
		Moment moment = momentMapper.getMomentById(id);
		if (moment == null) {
			throw new NotFoundException("动态不存在");
		}
		return moment;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void deleteMomentById(Long id) {
		if (momentMapper.deleteMomentById(id) != 1) {
			throw new PersistenceException("删除失败");
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveMoment(Moment moment) {
		if (momentMapper.saveMoment(moment) != 1) {
			throw new PersistenceException("动态添加失败");
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void updateMoment(Moment moment) {
		if (momentMapper.updateMoment(moment) != 1) {
			throw new PersistenceException("动态修改失败");
		}
	}

	@Override
	public List<BlogWithMomentView> getBolgTitleById(Long id, Integer pageNum) {
		PageHelper.startPage(pageNum, pageSize);
		List<BlogWithMomentView> bolgTitleById = blogMapper.getBolgTitleById(id);
		return bolgTitleById;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Result addLikeByBlogId(Long id) {
		if (blogMapper.addLikeByBlogId(id)) {
			return Result.ok("点赞成功");
		}
		throw new PersistenceException("操作失败");
	}

	@Override
	public Result deleteBlogById(Long id,String jwt,Long userId) {
		// 多用户登陆防止错误请求
		if (JwtUtils.judgeTokenIsExist(jwt)){
			User userDetails =getUserDetails(jwt);
			if (blogMapper.getBlogById(id,userId).getUser().getId().equals(userDetails.getId())){
				commentMapper.deleteCommentsByBlogId(id);
				blogMapper.deleteBlogById(id);
				blogMapper.deleteBlogTagByBlogId(id);
				redisService.deleteCacheByKey(RedisKeyConstants.HOME_BLOG_INFO_LIST);
				redisService.deleteCacheByKey(RedisKeyConstants.NEW_BLOG_LIST);
				redisService.deleteCacheByKey(RedisKeyConstants.ARCHIVE_BLOG_MAP);
				return Result.ok("删除成功");
			}
			return Result.error("您删除的动态并不是登陆账户下的动态");
		}
		return Result.error("token无效,请检查是否登陆");

	}

	@Override
	public Result editBlog(top.naccl.model.dto.Blog blog, String type,String jwt,Long userId) {
		// 多用户登陆防止错误请求
		if (JwtUtils.judgeTokenIsExist(jwt)){
				return blogService.editBlog(blog,type,userId);
		}
		return Result.error("token无效,请检查是否登陆");

	}

	@Override
	public Result getBlogById(Long id,String jwt,Long userId) {
		if (JwtUtils.judgeTokenIsExist(jwt)){
			User userDetails =getUserDetails(jwt);
			if (blogMapper.getBlogById(id,userId).getUser().getId().equals(userDetails.getId())){
				Blog blog = blogMapper.getBlogById(id,userId);
				if (blog == null) {
					throw new NotFoundException("博客不存在");
				}
				return Result.ok("获取成功",blog) ;
			}
			return Result.error("您查看的动态并不是登陆账户下的动态");
		}
		return Result.error("token无效,请检查是否登陆");

	}

	@Override
	public List<BlogWithMomentView> getBolgListAnonymous(Long userId, Integer pageNum) {
		PageHelper.startPage(pageNum, pageSize);
		List<BlogWithMomentView> bolgListAnonymous = blogMapper.getBolgListAnonymous(userId);
		return bolgListAnonymous;
	}

	User getUserDetails(String jwt){
		String subject = JwtUtils.getTokenBody(jwt).getSubject();
		String username = subject.replace(JwtConstants.ADMIN_PREFIX, "");
		User userDetails = (User) userService.loadUserByUsername(username);
		return userDetails;
	}
}
