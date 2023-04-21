package top.naccl.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.naccl.entity.User;
import top.naccl.entity.UserFans;
import top.naccl.model.vo.Result;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @Description: 用户持久层接口
 * @Author: wdd
 * @Date: 2020-07-19
 */
@Mapper
@Repository
public interface UserMapper {
	User findByUsername(String username);

	Integer findByUsernameIsNull(String username);

	User findById(Long id);

	boolean registerUser(User user);

    User getPasswordByUserId(Long id);

	boolean updateUserPassword(@Param("id") Long id,@Param("password") String password);

	int updateUser(User user);

	int getUserByUserName(String username);

    int addFans(UserFans userFans);

	int getFansByUserIdAndBloggerId(@Param("userId") Long userId, @Param("bloggerId") Long bloggerId);

	Boolean addFansByUserId(Long userId);
	Boolean addFollowByUserId(Long userId);



}
