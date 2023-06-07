package top.naccl.service;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import top.naccl.entity.User;
import top.naccl.model.vo.NewPasswordVo;
import top.naccl.model.vo.Result;

public interface UserService {
	User findUserByUsernameAndPassword(String username, String password);

	User findUserById(Long id);

    Result register(User user);

    Result getPasswordByUserId(NewPasswordVo newPasswordVo);

    Result updateUser(User user);

    Result addFans(Long userId, Long bloggerId);

    Result isFans(Long userId, Long bloggerId);


    Result uploadAvatarImage(MultipartFile file, Long userId);

    Result getBlogger(Long bloggerId);

    Result cancelFollow(Long userId, Long bloggerId);

    Result getAllUser(String name,Integer pageNum,Integer pageSize);

    Result editUser(User user);

    Result deleteUser(Long id);

    Result getUser(Long id);

    Result sendCode(User user);
}
