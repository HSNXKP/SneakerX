package top.naccl.controller;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import top.naccl.entity.Tag;
import top.naccl.model.vo.NewPasswordVo;
import top.naccl.model.vo.Result;
import top.naccl.service.TagService;
import top.naccl.service.UserService;
import top.naccl.util.StringUtils;

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



    @PostMapping("/user/updatePassword")
    public Result updatePassword(@RequestBody NewPasswordVo newPasswordVo) {
        return userService.getPasswordByUserId(newPasswordVo);
    }

    /**
     * 退出登录
     *
     * @return
     */
    @PostMapping("/user/logOut")
    public Result logOut() {
        SecurityContextHolder.clearContext();
        return Result.ok("退出成功");
    }


}
