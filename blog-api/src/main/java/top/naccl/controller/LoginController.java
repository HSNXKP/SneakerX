package top.naccl.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import top.naccl.annotation.AccessLimit;
import top.naccl.constant.JwtConstants;
import top.naccl.entity.User;
import top.naccl.mapper.UserMapper;
import top.naccl.model.dto.LoginInfo;
import top.naccl.model.vo.Result;
import top.naccl.service.LoginService;
import top.naccl.service.UserService;
import top.naccl.util.JwtUtils;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 前台登录+后台登录
 * @Author: wdd
 * @Date: 2023-02-25
 */
@RestController
public class LoginController {
	@Autowired
	UserService userService;

	@Autowired
	private LoginService loginService;

	@Autowired
	private UserMapper userMapper;




	/**
	 * 登录成功后，签发博主身份Token
	 *
	 * @param loginInfo
	 * @return
	 */
	@PostMapping("/login")
	public Result login(@RequestBody LoginInfo loginInfo) {
		return	loginService.login(loginInfo.getUsername(), loginInfo.getPassword());
	}

	/**
	 * 获取当前登录的用户信息
	 * @param principal
	 * @return
	 */
	@GetMapping("/loginInfo")
	public User getLoginInfo(Principal principal){
		if (principal==null){
			return null;
		}
		String username = principal.getName();
		User user = userMapper.findByUsername(username);
		user.setPassword(null);
		//通过admin的id查询角色  给我们的用户添加角色
		return user;
	}



	@PostMapping("/register")
	public Result register(@RequestBody User user){
		return userService.register(user);
	}

	@AccessLimit(seconds = 60, maxCount = 1,msg = "请勿频繁发送验证码")
	@PostMapping("/sendCode")
	public Result sendCode(@RequestBody User user){
		return userService.sendCode(user);
	}



	/**
	 * 退出登录
	 *
	 * @return
	 */
	@PostMapping("/logOut")
	public Result logOut() {
		SecurityContextHolder.clearContext();
		return Result.ok("退出成功");
	}


}
