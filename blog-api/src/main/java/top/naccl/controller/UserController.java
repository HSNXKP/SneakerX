package top.naccl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.model.vo.NewPasswordVo;
import top.naccl.model.vo.Result;
import top.naccl.service.UserService;

/**
 * @Author wdd
 * @Date 2023/3/22 10:12
 * @PackageName:top.naccl.controller
 * @ClassName: UserController
 * @Version 1.0
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody NewPasswordVo newPasswordVo){
      return  userService.getPasswordByUserId(newPasswordVo);
    }

    /**
     * 退出登录
     * @return
     */
    @PostMapping("/logOut")
    public Result logOut(){
        SecurityContextHolder.clearContext();
        return Result.ok("退出成功");
    }
}
