package top.naccl.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.naccl.config.properties.RabbitMQConstant;
import top.naccl.config.properties.UploadProperties;
import top.naccl.constant.RedisKeyConstants;
import top.naccl.entity.*;
import top.naccl.exception.NotFoundException;
import top.naccl.mapper.BlogMapper;
import top.naccl.mapper.CodeLogMapper;
import top.naccl.mapper.MailLogMapper;
import top.naccl.mapper.UserMapper;
import top.naccl.model.vo.NewPasswordVo;
import top.naccl.model.vo.Result;
import top.naccl.service.RedisService;
import top.naccl.service.SiteSettingService;
import top.naccl.service.UserService;
import top.naccl.util.HashUtils;
import top.naccl.util.upload.UploadUtils;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @Description: 用户业务层接口实现类
 * @Author: wdd
 * @Date: 2020-07-19
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UploadProperties uploadProperties;

	@Autowired
	private BlogMapper blogMapper;

	@Autowired
	private SiteSettingService siteSettingService;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private MailLogMapper mailLogMapper;

	@Autowired
	private CodeLogMapper codeLogMapper;

	@Autowired
	private RedisService redisService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userMapper.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("用户不存在");
		}
		return user;
	}

	@Override
	public User findUserByUsernameAndPassword(String username, String password) {
		User user = userMapper.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("用户不存在");
		}
		if (!HashUtils.matchBC(password, user.getPassword())) {
			throw new UsernameNotFoundException("密码错误");
		}
		return user;
	}

	@Override
	public User findUserById(Long id) {
		User user = userMapper.findById(id);
		if (user == null) {
			throw new NotFoundException("用户不存在");
		}
		return user;
	}

    @Override
    public Result register(User user) {
		if (userMapper.findByUsername(user.getUsername()) !=null){
			return Result.error("用户名已存在");
		}
		String code = (String) redisService.getValueByHashKey(RedisKeyConstants.CODE_MSG_ID_MAP, user.getEmail());
		if (!code.equals(user.getCode())){
			return Result.error("验证码错误");
		}
		// 创建账号时间
		user.setCreateTime(LocalDateTime.now());
		user.setUpdateTime(LocalDateTime.now());
		// 密码加密
		user.setPassword(HashUtils.getBC(user.getPassword()));
		user.setRole("ROLE_common");
		siteSettingService.getAnonymousAvatar();
		// 设置默认头像
		user.setAvatar(siteSettingService.getAnonymousAvatar());
		// 设置默认登陆标识 + 随机五位数
		Random random = new Random();
		String randomNumber= String.valueOf((int)(random.nextDouble() * (99999 - 10000 + 1)) + 10000);
		user.setUserFlag("幸运的卡卡"+ randomNumber);
		//
		// 设置默认前戳的颜色为黑色
		user.setFlagColor("black");
		// 设置默认签名
		user.setUserSign("这个人很懒，什么都没有留下");
		if (userMapper.registerUser(user)){
			User userMail = userMapper.findByUsername(user.getUsername());
			// 唯一标识符UUID
			String msgId = UUID.randomUUID().toString();
			MailLog mailLog = new MailLog();
			mailLog.setMsgId(msgId);
			mailLog.setUserId(userMail.getId());
			mailLog.setStatus(RabbitMQConstant.MailConstant.DELIVERING);
			mailLog.setRouteKey(RabbitMQConstant.MailConstant.MAIL_ROUTING_KEY_NAME);
			mailLog.setExchange(RabbitMQConstant.MailConstant.MAIL_EXCHANGE_NAME);
			mailLog.setCount(0);
			mailLog.setTryTime(LocalDateTime.now().plusMinutes(RabbitMQConstant.MailConstant.MSG_TIMEOUT));
			mailLog.setCreateTime(LocalDateTime.now());
			mailLog.setUpdateTime(LocalDateTime.now());
			// 消息入库
			mailLogMapper.insertMailLog(mailLog);
			//清除邮箱验证码
			redisService.deleteByHashKey(RedisKeyConstants.CODE_MSG_ID_MAP,user.getEmail());
			// 发送消息
			rabbitTemplate.convertAndSend(
					RabbitMQConstant.MailConstant.MAIL_EXCHANGE_NAME,
					RabbitMQConstant.MailConstant.MAIL_ROUTING_KEY_NAME,
					userMail,
					new CorrelationData(RabbitMQConstant.MailConstant.MAIL_ID_PREFIX + RabbitMQConstant.DOT + msgId)
			);
			return Result.ok("注册成功");
		}
		return Result.error("注册失败");
    }


    @Override
    public Result getPasswordByUserId(NewPasswordVo newPasswordVo) {
		// 拿到原始密码
		String password = (userMapper.getPasswordByUserId(newPasswordVo.getId())).getPassword();
		// HashUtil对比
		if (HashUtils.matchBC(newPasswordVo.getOldPassword(),password)) {
			// 通过userId修改密码
			userMapper.updateUserPassword(newPasswordVo.getId(),HashUtils.getBC(newPasswordVo.getNewPassword()));
			return Result.ok("修改成功");
		}
		return Result.error("密码不正确,请重新填写");

	}

	@Override
	public Result updateUser(User user) {
		int userCount = userMapper.getUserByUserName(user.getUsername());
		if (userCount == 1) {
			User userFlag = userMapper.getUserByUserFlag(user.getUserFlag());
			if(userFlag == null){
				user.setUpdateTime(LocalDateTime.now());
				if (userMapper.updateUser(user) == 1){
					return Result.ok("更新成功");
				}
			}
			if (userFlag.getId().equals(user.getId())){
				user.setUpdateTime(LocalDateTime.now());
				if (userMapper.updateUser(user) == 1){
					return Result.ok("更新成功");
				}
				return Result.error("更新失败");
			}
			return Result.error("前戳已存在");
		}
		return Result.error("用户名已存在");
	}

	/**
	 * blogger是被关注的人 加的是粉丝数 userId是登陆的账户是粉丝
	 * @param userId
	 * @param bloggerId
	 * @return
	 */
	@Override
    public Result addFans(Long userId, Long bloggerId) {
		if (userMapper.getFansByUserIdAndBloggerId(userId,bloggerId) == 0){
			if (!userId.equals(bloggerId)){
				UserFans userFans = new UserFans();
				userFans.setUserId(bloggerId);
				userFans.setFansId(userId);
				userFans.setCreateTime(LocalDateTime.now());
				// 对两边的账户进行增加粉丝处理
				userMapper.addFans(userFans);
				userMapper.addFansByUserId(bloggerId);
				userMapper.addFollowByUserId(userId);
				return Result.ok("关注成功");
			}
			return Result.error("不能关注自己");
		}
		return Result.error("已关注");
    }

	@Override
	public Result isFans(Long userId, Long bloggerId) {
		if (userMapper.getFansByUserIdAndBloggerId(userId,bloggerId) == 1){
			return Result.ok("已关注",true);
		}
		return Result.ok("获取成功",false);
	}

    @Override
    public Result uploadAvatarImage(MultipartFile file, Long userId) {
		// 判断当前环境是linux还是window
		String avatarPath = "";
		String path = "";
		String osName = System.getProperties().getProperty("os.name");
		if(osName.equals("Linux")){
			avatarPath = uploadProperties.getLinuxPath();
			path = uploadProperties.getLinuxNginx();

		}else{
			avatarPath = uploadProperties.getPath();
			path = uploadProperties.getWindowNginx();
		}
		// 拿到头像回显地址/avatar/
		String accessPath =  uploadProperties.getAccessPath();
		// 拿到file的后戳
		String substring = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		String fileName = UUID.randomUUID() + substring;
		try {
			User user = userMapper.findById(userId);
			// 储存路径需要补一个/  window：D:/Resource/avatar/ linux: /www/image/avatar/
			String fileUploadPath =  avatarPath + user.getUsername()+"/";
			// 头像回显地址 /avatar/Admin/
			String avatarUploadPath = accessPath + user.getUsername() + "/";
			// 储存头像
			UploadUtils.saveFile(file.getInputStream(),fileName,fileUploadPath);
			// 设置头像的映射路径 用户的用户名作为文件夹命名
			// 本地：http://localhost/avatar/Admin/0639b8e1-0978-499f-aa44-beb64b9a1d61.jpg
			// 服务器：http://43.138.9.213/image/avatar/Admin/0639b8e1-0978-499f-aa44-beb64b9a1d61.jpg
			// 示例： http://localhost + /avatar/Admin/ + 0639b8e1-0978-499f-aa44-beb64b9a1d61.jpg
			user.setAvatar(path + avatarUploadPath + fileName);
			// 更新用户
			userMapper.updateUser(user);
			return Result.ok("上传成功",user);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }


	@Override
	public Result getBlogger(Long bloggerId) {
		User user = userMapper.findById(bloggerId);
		user.setPassword(null);
		user.setUserFlag(null);
		user.setCreateTime(null);
		user.setUpdateTime(null);
		user.setRole(null);
		return Result.ok("获取成功",user);
	}


	@Override
	public Result cancelFollow(Long userId, Long bloggerId) {
		// 判断是不是粉丝
		if (userMapper.getFansByUserIdAndBloggerId(userId,bloggerId) == 1){
			// 删除粉丝表
			userMapper.cancelFollow(userId,bloggerId);
			// 对应的博主粉丝和用户关注都要减去1
			userMapper.subtractFans(bloggerId);
			userMapper.subtractFollow(userId);
			return Result.ok("成功取消关注");
		}
		return Result.error("未关注该用户");
	}

	@Override
	public Result getAllUser(String name,Integer pageNum,Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<User> allUser = userMapper.getAllUser(name);
		PageInfo<User> pageInfo = new PageInfo<>(allUser);
		return Result.ok("获取成功",pageInfo);
	}

	@Override
	public Result editUser(User user) {
		int i = userMapper.updateUserByAdmin(user);
		if (i == 1){
			return Result.ok("修改成功");
		}
		return Result.error("修改失败");
	}

	@Override
	public Result deleteUser(Long id) {
		List<Blog> blogByUserList = blogMapper.getBlogByUserId(id);
		if (blogByUserList.size() == 0){
			userMapper.deleteUser(id);
			return Result.ok("删除成功");
		}
		return Result.error("该用户下含有动态，不能删除");
	}

    @Override
    public Result getUser(Long id) {
		User user = userMapper.findById(id);
		if (user != null){
			return Result.ok("获取成功",user);
		}
		return Result.error("未查询到该用户");
	}

    @Override
    public Result sendCode(User user) {
		//TODO 没有做时间的校验 二次开发可以做
		if (redisService.getValueByHashKey(RedisKeyConstants.CODE_MSG_ID_MAP,user.getEmail()) != null){
			redisService.deleteByHashKey(RedisKeyConstants.CODE_MSG_ID_MAP,user.getEmail());
		}
		// 唯一标识符UUID
		String msgId = UUID.randomUUID().toString();
		CodeLog codeLog = new CodeLog();
		codeLog.setMsgId(msgId);
		codeLog.setEmail(user.getEmail());
		codeLog.setStatus(RabbitMQConstant.CodeConstant.DELIVERING);
		codeLog.setRouteKey(RabbitMQConstant.CodeConstant.CODE_ROUTING_KEY_NAME);
		codeLog.setExchange(RabbitMQConstant.CodeConstant.CODE_EXCHANGE_NAME);
		codeLog.setCount(0);
		codeLog.setTryTime(LocalDateTime.now().plusMinutes(RabbitMQConstant.CodeConstant.MSG_TIMEOUT));
		codeLog.setCreateTime(LocalDateTime.now());
		codeLog.setUpdateTime(LocalDateTime.now());
		// 消息入库
		codeLogMapper.insertCodeLog(codeLog);
		// 发送消息
		rabbitTemplate.convertAndSend(
				RabbitMQConstant.CodeConstant.CODE_EXCHANGE_NAME,
				RabbitMQConstant.CodeConstant.CODE_ROUTING_KEY_NAME,
				user.getEmail(),
				new CorrelationData(RabbitMQConstant.CodeConstant.CODE_ID_PREFIX + RabbitMQConstant.DOT + msgId)
		);
		return Result.ok("发送成功");
    }


}
