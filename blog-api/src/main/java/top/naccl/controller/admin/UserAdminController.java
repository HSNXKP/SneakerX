package top.naccl.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.naccl.entity.User;
import top.naccl.model.vo.Result;
import top.naccl.service.UserService;

/**
 * @Author wdd
 * @Date 2023/4/26 10:48
 * @PackageName:top.naccl.controller.admin
 * @ClassName: UserAdminController
 * @Version 1.0
 */
@RestController
@RequestMapping("/admin")
public class UserAdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/getAllUser")
    public Result getAllUser(@RequestParam("name") String name,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize){
        return userService.getAllUser(name,pageNum,pageSize);
    }

    @PostMapping("/editUser")
    public Result editUser(@RequestBody User user){
        return userService.editUser(user);
    }

    @GetMapping("/deleteUser")
    public Result deleteUser(@RequestParam("id") Long id){
        return userService.deleteUser(id);
    }

    @GetMapping("/getUser")
    public Result getUser(@RequestParam("id") Long id){
        return userService.getUser(id);
    }

    @PostMapping("/uploadAvatarImage")
    public Result uploadTongueImage(@RequestParam("file") MultipartFile file, @RequestParam("userId")Long userId){
        return userService.uploadAvatarImage(file,userId);
    }

}
