package top.naccl.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.entity.User;
import top.naccl.mapper.UserMapper;
import top.naccl.model.vo.Result;
import top.naccl.service.UserService;
import top.naccl.service.impl.UserServiceImpl;

/**
 * @author: wdd
 * @date: 2023/5/10 16:39
 */
@RestController
@RequestMapping("/admin")
public class AccountAdminController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/account")
    public Result account(User user){
        User admin = userMapper.findByUsername(user.getUsername());
        if (admin.getRole().equals("ROLE_admin")){
            admin.setPassword(passwordEncoder.encode(user.getPassword()));
            userMapper.updateUser(user);
            return Result.ok("修改成功");
        }
        return Result.error("修改失败，您没有权限");
    }
}
