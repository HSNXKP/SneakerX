package top.naccl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import top.naccl.annotation.VisitLogger;
import top.naccl.config.properties.UploadProperties;
import top.naccl.constant.JwtConstants;
import top.naccl.entity.Blog;
import top.naccl.entity.User;
import top.naccl.enums.VisitBehavior;
import top.naccl.mapper.UserMapper;
import top.naccl.model.dto.BlogPassword;
import top.naccl.model.vo.BlogDetail;
import top.naccl.model.vo.BlogInfo;
import top.naccl.model.vo.PageResult;
import top.naccl.model.vo.Result;
import top.naccl.model.vo.SearchBlog;
import top.naccl.service.BlogService;
import top.naccl.service.impl.UserServiceImpl;
import top.naccl.util.JwtUtils;
import top.naccl.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Description: 博客相关
 * @Author: wdd
 * @Date: 2020-08-12
 */
@RestController
public class BlogController {
	@Autowired
	BlogService blogService;
	@Autowired
	UserServiceImpl userService;

	@Autowired
	private UserMapper userMapper;


	@Autowired
	private UploadProperties uploadProperties;

	/**
	 * 按置顶、创建时间排序 分页查询博客简要信息列表
	 *
	 * @param pageNum 页码
	 * @return
	 */
	@VisitLogger(VisitBehavior.INDEX)
	@GetMapping("/blogs")
	public Result blogs(@RequestParam(defaultValue = "1") Integer pageNum) {
		PageResult<BlogInfo> pageResult = blogService.getBlogInfoListByIsPublished(pageNum);
		return Result.ok("请求成功", pageResult);
	}

	/**
	 * 按id获取公开博客详情
	 *
	 * @param id  博客id
	 * @param jwt 密码保护文章的访问Token
	 * @return
	 */
	@VisitLogger(VisitBehavior.BLOG)
	@GetMapping("/blog")
	public Result getBlog(@RequestParam Long id,
	                      @RequestHeader(value = "Authorization", defaultValue = "") String jwt) {
		// 当用户登陆以后就不用校验当前用户下动态的密码校验 先进行私密作品的校验
		// blogDetail前端显示页面 blog为校验数据
		BlogDetail blogDetail = blogService.getBlogByIdAndIsPublished(id,"notPublished");
		if (!blogDetail.getIsPublished()){
			if (JwtUtils.judgeTokenIsExist(jwt)){
				String subject = JwtUtils.getTokenBody(jwt).getSubject();
				String username = subject.replace(JwtConstants.ADMIN_PREFIX, "");
				//判断token是否为blogToken
				User userDetails = (User) userService.loadUserByUsername(username);
				if (userDetails != null) {
					Blog blogWithUser = blogService.getBlogById(id);
					if (!blogWithUser.getPublished()) {
						if (blogWithUser.getUser().getId().equals(userDetails.getId())) {
							return Result.ok("获取成功", blogDetail);
						}
						return Result.error("当前账号未发布该动态");
					}
				}else {
					//经密码验证后的Token
					return Result.error("当前账号登录的信息未有该动态");
				}
			}
			return Result.error("Token已失效,请重新登录");
		}

		// 用户没有登陆的访问,或者用户登陆了 访问的当前的blogId并不是该用户的blog，需要校验信息的正确性 密码作品的校验
		BlogDetail blog = blogService.getBlogByIdAndIsPublished(id,"isPublished");
		//对密码保护的文章校验Token
		if (!"".equals(blog.getPassword())) {
			if (JwtUtils.judgeTokenIsExist(jwt)) {
				try {
					String subject = JwtUtils.getTokenBody(jwt).getSubject();
					String username = subject.replace(JwtConstants.ADMIN_PREFIX, "");
					if (userMapper.findByUsernameIsNull(username) != 0){
						if (userService.loadUserByUsername(username) != null){
							User userDetails = (User) userService.loadUserByUsername(username);
							Blog blogWithUser = blogService.getBlogById(id);
							if (userDetails.getId().equals(blogWithUser.getUser().getId())){
								return Result.ok("获取成功", blog);
							}
							return Result.create(403, "Token不匹配，请输入验证密码！");
					    }
					}else {
						//经密码验证后的Token
						Long tokenBlogId = Long.parseLong(subject);
						//博客id不匹配，验证不通过，可能博客id改变或客户端传递了其它密码保护文章的Token
						if (!tokenBlogId.equals(id)) {
							return Result.create(403, "Token不匹配，请重新验证密码！");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					return Result.create(403, "Token已失效，请重新验证密码！");
				}
			} else {
				return Result.create(403, "此文章受密码保护，请验证密码！");
			}
			blog.setPassword("");
		}
		blogService.updateViewsToRedis(id);
		return Result.ok("获取成功", blog);
	}

	/**
	 * 校验受保护文章密码是否正确，正确则返回jwt
	 *
	 * @param blogPassword 博客id、密码
	 * @return
	 */
	@VisitLogger(VisitBehavior.CHECK_PASSWORD)
	@PostMapping("/checkBlogPassword")
	public Result checkBlogPassword(@RequestBody BlogPassword blogPassword) {
		String password = blogService.getBlogPassword(blogPassword.getBlogId());
		if (password.equals(blogPassword.getPassword())) {
			//生成有效时间一个月的Token
			// 未登录访问密码
			String jwt = JwtUtils.generateToken(blogPassword.getBlogId().toString(), 1000 * 3600 * 24 * 30L);
			return Result.ok("密码正确", jwt);
		} else {
			return Result.create(403, "密码错误");
		}
	}

	/**
	 * 按关键字根据文章内容搜索公开且无密码保护的博客文章
	 *
	 * @param query 关键字字符串
	 * @return
	 */
	@VisitLogger(VisitBehavior.SEARCH)
	@GetMapping("/searchBlog")
	public Result searchBlog(@RequestParam String query) {
		//校验关键字字符串合法性
		if (StringUtils.isEmpty(query) || StringUtils.hasSpecialChar(query) || query.trim().length() > 20) {
			return Result.error("参数错误");
		}
		List<SearchBlog> searchBlogs = blogService.getSearchBlogListByQueryAndIsPublished(query.trim());
		return Result.ok("获取成功", searchBlogs);
	}

	@PostMapping("user/blog/upload")
	public Result blog(MultipartHttpServletRequest multiRequest) {
		String blogPath = "";
		String path = "";
		String osName = System.getProperties().getProperty("os.name");
		if(osName.equals("Linux")){
			blogPath = uploadProperties.getLinuxBlogPath();
			path = uploadProperties.getLinuxNginx();
		}else{
			blogPath = uploadProperties.getBlogPath();
			path = uploadProperties.getWindowNginx();
		}
		String accessBlogPath = uploadProperties.getAccessBlogPath();
		// blog储存地址
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); //生成日期格式
		String datePrefix = dateFormat.format(new Date()); //生成当前日期作为前缀
		String savePath = blogPath; // 存储路径
		File folder = new File(savePath + datePrefix); //生成带当前日期的文件路径
		if(!folder.isDirectory()){
			folder.mkdirs();
		}
		String randomName = multiRequest.getFile("image").getOriginalFilename(); //获取图片名
		//生成随机数确保唯一性，并加上图片后缀
		String saveName = UUID.randomUUID() + randomName.substring(randomName.lastIndexOf("."),randomName.length());
		String absolutePath = folder.getAbsolutePath(); //转换成绝对路径
		try {
			File fileToSave = new File(absolutePath + File.separator + saveName);
			multiRequest.getFile("image").transferTo(fileToSave); //图片存储到服务端
			// 本地：http://localhost/blog/2023-04-24/0639b8e1-0978-499f-aa44-beb64b9a1d61.jpg
			// 服务器：http://43.138.9.213/image/blog/2023-04-24/0639b8e1-0978-499f-aa44-beb64b9a1d61.jpg
			// 示例： http://localhost + /blog/ + 2023-04-24 + / + 0639b8e1-0978-499f-aa44-beb64b9a1d61.jpg
			String returnPath = path +  accessBlogPath +  datePrefix + "/" + saveName;
			return Result.ok("上传成功",returnPath);

		}catch (Exception e){
			e.printStackTrace();
		}
		return Result.error("上传失败");
	}







}
