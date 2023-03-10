package top.naccl.service.impl;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import top.naccl.constant.JwtConstants;
import top.naccl.entity.User;
import top.naccl.model.vo.Result;
import top.naccl.service.LoginService;
import top.naccl.service.UserService;
import top.naccl.util.JwtUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: wdd
 * @date: 2023/2/25 22:58
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserService userService;



    @Override
    public Result login(String username, String password) {
        User user = userService.findUserByUsernameAndPassword(username, password);
        if (!"ROLE_common".equals(user.getRole())  && !"ROLE_admin".equals(user.getRole())) {
            return Result.create(403, "无权限");
        }
        // 更新SecurityContext中的Authentication对象
        user.setPassword(null);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 获得token
        String jwt = JwtUtils.generateToken(JwtConstants.ADMIN_PREFIX + user.getUsername(),user.getAuthorities());
        Map<String, Object> map = new HashMap<>(4);
        map.put("user", user);
        map.put("token", jwt);
        return Result.ok("登录成功", map);
    }


}
