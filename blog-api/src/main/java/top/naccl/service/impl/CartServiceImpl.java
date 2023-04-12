package top.naccl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.naccl.constant.JwtConstants;
import top.naccl.entity.Order;
import top.naccl.entity.User;
import top.naccl.mapper.CartMapper;
import top.naccl.model.vo.Result;
import top.naccl.service.CartService;
import top.naccl.util.JwtUtils;

import java.util.List;

/**
 * @author: wdd
 * @date: 2023/4/11 21:15
 */
@Service
public class CartServiceImpl implements CartService {



    @Autowired
    private UserServiceImpl userService;;

    @Autowired
    private CartMapper cartMapper;

    @Override
    public Result addCart(String jwt,String productId, String userId) {
        try {
            if (JwtUtils.judgeTokenIsExist(jwt)) {
                // 比对当前的token和id是否一致
                String subject = JwtUtils.getTokenBody(jwt).getSubject();
                String username = subject.replace(JwtConstants.ADMIN_PREFIX, "");
                //判断token是否为blogToken
                User userDetails = (User) userService.loadUserByUsername(username);
                if (userDetails != null) {
                        if (userDetails.getId().equals(userId)) {
                            return cartMapper.addCart(productId,userId);
                        }
                        return Result.error("您没有此订单的权限");
                }
                return Result.error("token无效，请重新登录");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Result.error("token无效，请重新登录");

    }
}
