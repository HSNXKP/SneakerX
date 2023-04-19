package top.naccl.service;

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
}
