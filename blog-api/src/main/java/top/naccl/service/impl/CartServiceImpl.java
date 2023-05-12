package top.naccl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.naccl.constant.JwtConstants;
import top.naccl.entity.*;
import top.naccl.mapper.CartMapper;
import top.naccl.mapper.ProductCategoryMapper;
import top.naccl.mapper.ProductMapper;
import top.naccl.mapper.ProductSizeMapper;
import top.naccl.model.vo.OrderVo;
import top.naccl.model.vo.Result;
import top.naccl.service.CartService;
import top.naccl.util.JwtUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Autowired
    private ProductSizeMapper productSizeMapper;

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Autowired
    private ProductMapper productMapper;




    @Override
    public Result addCart(String jwt, OrderVo orderVo){
        try {
            if (JwtUtils.judgeTokenIsExist(jwt)) {
                // 比对当前的token和id是否一致
                String subject = JwtUtils.getTokenBody(jwt).getSubject();
                String username = subject.replace(JwtConstants.ADMIN_PREFIX, "");
                //判断token是否为blogToken
                User userDetails = (User) userService.loadUserByUsername(username);
                if (userDetails != null) {
                        if (userDetails.getId().equals(orderVo.getUserId())) {
                            Cart cartByProductSizeId = null;
                            cartByProductSizeId = cartMapper.getCartByProductSizeId(orderVo.getSizeWithPrice(), orderVo.getUserId());
                            // 如果当前的尺码查询出来是有这个的尺码的就可以直接更新购物车 而不是添加新购物车
                            if (cartByProductSizeId != null){
                                if (cartByProductSizeId.getProductId().equals(orderVo.getProductId())){
                                    // 设置新的数量
                                    // 设置单个的总价格
                                    cartByProductSizeId.setUpdateTime(LocalDateTime.now());
                                    cartByProductSizeId.setQuantity(cartByProductSizeId.getQuantity() + orderVo.getQuantity());
                                    Long addCartAmount = cartByProductSizeId.getPrice() * orderVo.getQuantity();
                                    cartByProductSizeId.setAmount(cartByProductSizeId.getAmount() + addCartAmount );
                                    if (cartMapper.updateCart(cartByProductSizeId) == 1){
                                        return Result.ok("成功加入购物车");
                                    }
                                    return Result.error("加入购物车失败");
                                }
                                return Result.error("加入购物车失败");
                            }
                            Cart cart = new Cart();
                            cart.setQuantity(orderVo.getQuantity());
                            cart.setChecked(true);
                            // 设置单价 前端计算总价格
                            ProductSize productSize = productSizeMapper.getProductSizeById(orderVo.getSizeWithPrice());
                            // 设置尺码价格id
                            cart.setProductSizeId(orderVo.getSizeWithPrice());
                            // 单价
                            cart.setPrice(productSize.getProductPrice());

                            // 设置尺码
                            cart.setSize(productSize.getName());
                            // 单个总价格
                            cart.setAmount((double) (productSize.getProductPrice() * orderVo.getQuantity()));
                            Product product = productMapper.getProductById(orderVo.getProductId());
                            // 通过商品Id找到分类ID 拿到父类Id 父类Id就是当前品牌查询出来即可
                                ProductCategory productCategory = productCategoryMapper.getProductCategoryById(productCategoryMapper.getProductCategoryById(product.getProductCategoryId()).getParentId());
                            // 设置商品分类id
                            cart.setProductCategoryId(productCategory.getId());
                            cart.setProductCategoryName(productCategory.getName());
                            // 设置商品名称
                            cart.setName(product.getName());
                            // 设置货号
                            cart.setCode(product.getCode());
                            // 设置商品图片
                            cart.setImage(product.getImage());
                            // 商品id
                            cart.setProductId(orderVo.getProductId());
                            // 用户id
                            cart.setUserId(orderVo.getUserId());
                            cart.setCreateTime(LocalDateTime.now());
                            cart.setUpdateTime(LocalDateTime.now());
                            if (cartMapper.addCart(cart) == 1){
                                return Result.ok("成功加入购物车");
                            }
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

    @Override
    public Result getCartByUserId(String jwt, Long userId) {
        try {
            if (JwtUtils.judgeTokenIsExist(jwt)) {
                // 比对当前的token和id是否一致
                String subject = JwtUtils.getTokenBody(jwt).getSubject();
                String username = subject.replace(JwtConstants.ADMIN_PREFIX, "");
                //判断token是否为blogToken
                User userDetails = (User) userService.loadUserByUsername(username);
                if (userDetails != null) {
                    if (userDetails.getId().equals(userId)) {
                        List<Cart> cartList = cartMapper.getCartByUserId(userId,null);
                        if (cartList.size() > 0){
                            Cart cartCategory = new Cart();
                            List<Cart> carts = new ArrayList<>();
                            // 通过商品分类id来判断是否是同一个分类
                            // 创建一个数组
                            List<Long> productCategoryList = new ArrayList<>();
                            // 将商品中所有的分类id拿出来进行去重
                            cartList.stream().forEach(cart -> {
                                if (!productCategoryList.contains(cart.getProductCategoryId())){
                                    productCategoryList.add(cart.getProductCategoryId());
                                }
                            });
                            for (Long categoryId : productCategoryList) {
                                List<Cart> cartProductCategoryList = cartMapper.getCartByProductCategoryId(categoryId,userId);
                                cartCategory.setProductCategoryName(cartProductCategoryList.get(0).getProductCategoryName());
                                cartCategory.setProductCategoryId(cartProductCategoryList.get(0).getProductCategoryId());
                                cartCategory.setCartList(cartProductCategoryList);
                                carts.add(cartCategory);
                                // 清空cartCategory
                                cartCategory = new Cart();
                            }
                            // 判断当前分类下的所有商品是否被选中
                            carts.forEach(cart ->{
                                Boolean checkedTrue = true;
                                // 设置当前分类下的所有商品是否被选中
                                for (Cart oneCart : cart.getCartList()) {
                                    if (!oneCart.getChecked()){
                                        checkedTrue = false;
                                    }
                                }
                                cart.setChecked(checkedTrue);
                            });
                            return Result.ok("获取成功",carts);
                        }
                        return Result.ok("购物车没有商品",cartList);
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

    @Override
    public Result addQuantityById(Long id,Long userId) {
        if (cartMapper.addQuantityById(id,userId) == 1){
            return Result.ok("成功+1");
        }
        return Result.error("失败");
    }

    @Override
    public Result downQuantityById(Long id,Long userId) {
        Cart cart = cartMapper.getCartById(id,userId);
        if (cart.getQuantity() == 1){
            if (cartMapper.deleteCartById(id,userId) == 1){
                return Result.ok("成功删除商品");
            }
        }
        if (cartMapper.downQuantityById(id,userId) == 1){
            return Result.ok("成功-1");
        }
        return Result.error("失败");
    }

    @Override
    public Result changeChecked(Long id, String type,Boolean checked, Long userId) {
        if(type.equals("productCategoryId")){
            // 当前端点击全部按钮后 需要改变当前分类下的所有商品的选中状态
            //TODO 有问题 加上if语句会执行成功但是会报错
                cartMapper.changeChecked(id,null,userId,checked);
                return Result.ok("成功");
        }else {
            if (cartMapper.changeChecked(null,id,userId,checked) == 1){
                return Result.ok("成功");
            }
        }
        return Result.error("失败");
    }

    @Override
    public Result deleteCart(Long id, Long userId,String type) {
        if (type.equals("deleteCartByCartId")){
            if (cartMapper.deleteCartById(id,userId) == 1){
                return Result.ok("删除成功");
            }
        }else {
            List<Cart> cartList = cartMapper.getCartByUserId(userId,true);
            if (cartList != null){
                    cartMapper.deleteCartByProductCategoryId(userId);
                    return Result.ok("删除成功");
            }
            return Result.error("未选中商品");
        }
        return Result.error("删除失败");
    }

    @Override
    public Result getCartListByUserIdIsChecked(Long userId) {
        List<Cart> cartList = cartMapper.getCartByUserId(userId, true);
        return Result.ok("获取成功",cartList);
    }
}
