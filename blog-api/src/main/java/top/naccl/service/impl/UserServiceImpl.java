package top.naccl.service.impl;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.naccl.exception.NotFoundException;
import top.naccl.mapper.UserMapper;
import top.naccl.entity.User;
import top.naccl.model.vo.NewPasswordVo;
import top.naccl.model.vo.Result;
import top.naccl.service.UserService;
import top.naccl.util.HashUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Description: 用户业务层接口实现类
 * @Author: Naccl
 * @Date: 2020-07-19
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {
	@Autowired
	private UserMapper userMapper;

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
			userMapper.updateUser(newPasswordVo.getId(),HashUtils.getBC(newPasswordVo.getNewPassword()));
			return Result.ok("修改成功");
		}
		return Result.error("密码不正确,请重新填写");

	}


}
