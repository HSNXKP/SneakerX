package top.naccl.service.impl;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.naccl.entity.UserFans;
import top.naccl.exception.NotFoundException;
import top.naccl.mapper.UserMapper;
import top.naccl.entity.User;
import top.naccl.model.vo.NewPasswordVo;
import top.naccl.model.vo.Result;
import top.naccl.service.UserService;
import top.naccl.util.HashUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

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
	private UserServiceImpl userService;

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
	public Result collectProduct(Long userId, Long productId) {
		if (userMapper.isCollectProductByUserIdAndProductId(userId,productId) == 0){
			userMapper.addCollectProduct(userId,productId,LocalDateTime.now());
			return Result.ok("收藏成功");
		}
		return Result.error("已收藏");
	}

	@Override
	public Result isCollectProduct(Long userId, Long productId) {
		if (userMapper.isCollectProductByUserIdAndProductId(userId,productId) == 0){
			return Result.ok("未收藏",false);
		}
		return Result.ok("已收藏",true);
	}

	@Override
	public Result cancelCollectProduct(Long userId, Long productId) {
		if (userMapper.isCollectProductByUserIdAndProductId(userId,productId) == 1){
			userMapper.deleteCollectProduct(userId,productId);
			return Result.ok("取消收藏成功",false);
		}
		return Result.error("未收藏");
	}


}
