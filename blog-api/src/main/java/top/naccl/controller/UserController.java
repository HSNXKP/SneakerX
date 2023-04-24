package top.naccl.controller;

import com.alibaba.fastjson.JSONObject;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.naccl.entity.Category;
import top.naccl.entity.Tag;
import top.naccl.entity.User;
import top.naccl.model.vo.NewPasswordVo;
import top.naccl.model.vo.Result;
import top.naccl.service.CategoryService;
import top.naccl.service.TagService;
import top.naccl.service.UserService;
import top.naccl.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author wdd
 * @Date 2023/3/22 10:12
 * @PackageName:top.naccl.controller
 * @ClassName: UserController
 * @Version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;


    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody NewPasswordVo newPasswordVo) {
        return userService.getPasswordByUserId(newPasswordVo);
    }



    /**
     * 获得所有分类和标签
     * @return
     */
    @GetMapping("/categoryAndTag")
    public Result categoryAndTag() {
        List<Category> categories = categoryService.getCategoryList();
        List<Tag> tags = tagService.getTagList();
        Map<String, Object> map = new HashMap<>(4);
        map.put("categories", categories);
        map.put("tags", tags);
        return Result.ok("请求成功", map);
    }


    @PostMapping("/updateUser")
    public Result updateUser(@RequestBody User user){
        return userService.updateUser(user);
    }

    @GetMapping("/getUser")
    public Result getUser(@RequestParam("userId") Long userId){
        User user = userService.findUserById(userId);
        user.setPassword(null);
        user.setRole(null);
        return Result.ok("获取成功",user);
    }



    /**
     * userId关注blogId bloggerId是userId userId是粉丝
     * @param userId
     * @param bloggerId
     * @return
     */
    @GetMapping("/addFans")
    public Result addFans(@RequestParam("userId") Long userId,@RequestParam("bloggerId") Long bloggerId){
        return userService.addFans(userId,bloggerId);
    }

    @GetMapping("/cancelFollow")
    public Result cancelFollow(@RequestParam("userId") Long userId,@RequestParam("bloggerId") Long bloggerId){
        return userService.cancelFollow(userId,bloggerId);
    }


    /**
     * 判断userId是否为bloggerId的粉丝
     * @param userId
     * @param bloggerId
     * @return
     */
    @GetMapping("/isFans")
    public Result isFans(@RequestParam("userId") Long userId,@RequestParam("bloggerId") Long bloggerId){
        return userService.isFans(userId,bloggerId);
    }

    @PostMapping("/uploadAvatarImage")
    public Result uploadTongueImage(@RequestParam("file") MultipartFile file, @RequestParam("userId")Long userId){
        return userService.uploadAvatarImage(file,userId);
    }


}
