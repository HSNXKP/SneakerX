package top.naccl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.naccl.config.properties.UploadProperties;
import top.naccl.entity.UserFans;
import top.naccl.exception.NotFoundException;
import top.naccl.mapper.UserMapper;
import top.naccl.entity.User;
import top.naccl.model.vo.NewPasswordVo;
import top.naccl.model.vo.Result;
import top.naccl.service.UserService;
import top.naccl.util.HashUtils;

import java.io.*;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Logger;

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
		// 创建账号时间
		user.setCreateTime(LocalDateTime.now());
		user.setUpdateTime(LocalDateTime.now());
		// 密码加密
		user.setPassword(HashUtils.getBC(user.getPassword()));
		user.setRole("ROLE_common");
		// 设置默认头像
		user.setAvatar("http://localhost/QQ20221014224335.jpg");
		// 设置默认登陆标识
		user.setUserFlag("潮流教父");
		// 设置默认前戳的颜色为黑色
		user.setFlagColor("black");
		// 设置默认签名
		user.setUserSign("这个人很懒，什么都没有留下");
		if (userMapper.registerUser(user)){
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
			user.setUpdateTime(LocalDateTime.now());
			if (userMapper.updateUser(user) == 1){
				return Result.ok("更新成功");
			}
			return Result.error("更新失败");
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
		String osName = System.getProperties().getProperty("os.name");
		if(osName.equals("Linux")){
			avatarPath = uploadProperties.getLinuxPath();
		}else{
			avatarPath = uploadProperties.getPath();
		}
		// 拿到头像请求地址映射路径
		String accessPath =  uploadProperties.getAccessPath();
		// 拿到file的后戳
		String substring = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		String fileName = UUID.randomUUID() + substring;
		try {
			User user = userMapper.findById(userId);
			// 储存路径需要补一个/
			String fileUploadPath =  avatarPath+user.getUsername()+"/";
			// 头像映射路径
			String avatarUploadPath = accessPath+ user.getUsername() + "/";
			// 储存头像
			saveFile(file.getInputStream(),fileName,fileUploadPath);
			// 设置头像的映射路径 用户的用户名作为文件夹命名
			user.setAvatar("http://localhost" + avatarUploadPath + fileName);
			// 更新用户
			userMapper.updateUser(user);
			return Result.ok("上传成功",user);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }

	/**
	 * 保存文件
	 * @param inputStream
	 * @param fileName
	 * @param filePath
	 */
	private void saveFile(InputStream inputStream, String fileName, String filePath) {
		OutputStream os = null;
		try {
			// 保存到临时文件
			// 1K的数据缓冲
			byte[] bs = new byte[1024];
			// 读取到的数据长度
			int len;
			// 输出的文件流保存到本地文件
			File tempFile = new File(filePath);
			if (!tempFile.exists()) {
				tempFile.mkdirs();
			}
			os = new FileOutputStream(tempFile.getPath() + File.separator + fileName);
			// 开始读取
			while ((len = inputStream.read(bs)) != -1) {
				os.write(bs, 0, len);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 完毕，关闭所有链接
			try {
				os.close();
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
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



}
