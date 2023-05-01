package top.naccl.controller;

import com.github.pagehelper.PageInfo;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.naccl.annotation.AccessLimit;
import top.naccl.annotation.VisitLogger;
import top.naccl.constant.JwtConstants;
import top.naccl.entity.Blog;
import top.naccl.entity.Moment;
import top.naccl.entity.User;
import top.naccl.enums.VisitBehavior;
import top.naccl.model.vo.BlogWithMomentView;
import top.naccl.model.vo.PageResult;
import top.naccl.model.vo.Result;
import top.naccl.service.BlogService;
import top.naccl.service.MomentService;
import top.naccl.service.impl.UserServiceImpl;
import top.naccl.util.JwtUtils;

import java.util.List;

/**
 * @Description: 动态
 * @Author: wdd
 * @Date: 2020-08-25
 */
@RestController
public class MomentController {
	@Autowired
	MomentService momentService;
	@Autowired
	UserServiceImpl userService;


	/**
	 * 分页查询动态List
	 *
	 * @param pageNum 页码
	 * @param jwt     博主访问Token
	 * @return
	 */
//	@VisitLogger(VisitBehavior.MOMENT)
//	@GetMapping("/moments")
//	public Result moments(@RequestParam(defaultValue = "1") Integer pageNum,
//	                      @RequestHeader(value = "Authorization", defaultValue = "") String jwt) {
//		// 定义当前用户的登录状态 true登录 false未登录
//		boolean adminIdentity = false;
//		if (JwtUtils.judgeTokenIsExist(jwt)) {
//			try {
//				String subject = JwtUtils.getTokenBody(jwt).getSubject();
//				if (subject.startsWith(JwtConstants.ADMIN_PREFIX)) {
//					//博主身份Token
//					String username = subject.replace(JwtConstants.ADMIN_PREFIX, "");
//					User admin = (User) userService.loadUserByUsername(username);
//					if (admin != null) {
//						adminIdentity = true;
//					}
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		PageInfo<Moment> pageInfo = new PageInfo<>(momentService.getMomentVOList(pageNum, adminIdentity));
//		PageResult<Moment> pageResult = new PageResult<>(pageInfo.getPages(), pageInfo.getList());
//		return Result.ok("获取成功", pageResult);
//	}


	/**
	 * 点赞动态
	 * @param id
	 * @return
	 */
	@AccessLimit(seconds = 10, maxCount = 6, msg = "10秒之内可以点赞哦")
	@VisitLogger(VisitBehavior.LIKE_MOMENT)
	@PostMapping("/moment/likeMoment/{id}")
	public Result likeMoment(@PathVariable Long id){
		return momentService.addLikeByBlogId(id);
	}

	/**
	 * 通过userId获得动态信息
	 * @return
	 */
	@GetMapping("/user/bolgTitleById")
	public Result getBolgListById(@RequestParam Long id,
						   @RequestParam(defaultValue = "1") Integer pageNum){
		List<BlogWithMomentView> blogs = momentService.getBolgTitleById(id,pageNum);
		for (BlogWithMomentView blog : blogs) {
			if (blog.getPassword().equals("")){
				blog.setPrivacy(false);
			}else {
				blog.setPrivacy(true);
			}
		}
		PageInfo<BlogWithMomentView> blogPageInfo = new PageInfo<>(blogs);
		PageResult<BlogWithMomentView> blogPageResult = new PageResult<>(blogPageInfo.getPages(), blogPageInfo.getList());
		return Result.ok("获取成功",blogPageResult);
	}

	/**
	 * 查看博主的公开动态信息
	 * @return
	 */
	@GetMapping("/bolgTitleById")
	public Result getBolgListAnonymous(@RequestParam Long id,
						   @RequestParam(defaultValue = "1") Integer pageNum){
		List<BlogWithMomentView> blogs = momentService.getBolgListAnonymous(id,pageNum);
		PageInfo<BlogWithMomentView> blogPageInfo = new PageInfo<>(blogs);
		PageResult<BlogWithMomentView> blogPageResult = new PageResult<>(blogPageInfo.getPages(), blogPageInfo.getList());
		return Result.ok("获取成功",blogPageResult);
	}

	/**
	 * 获得博主的简要信息
	 * @param bloggerId
	 * @return
	 */
	@GetMapping("/getBlogger")
	public Result getBlogger(@RequestParam("bloggerId") Long bloggerId){
		return userService.getBlogger(bloggerId);
	}

	/**
	 * 删除Blog
	 * @param id
	 * @return
	 */
	@GetMapping("/user/deleteBlog")
	public Result deleteBlogById(@RequestParam Long id,@RequestHeader(value = "Authorization", defaultValue = "") String jwt,@RequestParam("userId")Long userId){
		return momentService.deleteBlogById(id,jwt,userId);
	}

	/**
	 * 添加动态Blog
	 * @param blog
	 * @return
	 */
	@PostMapping("/user/blog")
	public Result saveBlog(@RequestBody top.naccl.model.dto.Blog blog,@RequestHeader(value = "Authorization", defaultValue = "") String jwt,@RequestParam("userId") Long userId){
		return momentService.editBlog(blog,"save",jwt,userId);
	}

	/**
	 * 查询当前Id的Blog
	 * @param id
	 * @return
	 */
	@GetMapping("/user/blog")
	public Result getBlogById(@RequestParam Long id,@RequestHeader(value = "Authorization", defaultValue = "") String jwt,@RequestParam("userId")Long userId){
		//TODO 很多地方都没有使用userID是错的 后面统一修改
		return momentService.getBlogById(id,jwt,userId);
	}


	/**
	 * 更新Blog
	 * @param blog
	 * @return
	 */
	@PutMapping("/user/blog")
	public Result updateBlog(@RequestBody top.naccl.model.dto.Blog blog,@RequestHeader(value = "Authorization", defaultValue = "") String jwt,@RequestParam("userId") Long userId){
		return momentService.editBlog(blog,"update",jwt,userId);
	}



}
