package top.naccl.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Repository;
import top.naccl.entity.Order;
import top.naccl.entity.ProductSize;
import top.naccl.model.vo.OrderListVo;
import top.naccl.model.vo.Result;

import java.util.List;

@Mapper
@Repository
public interface OrderMapper {
    int summitOrder(Order order);


    int getProductOrderCountByUserId(@Param("userId") Long userId, @Param("productId") Long productId);

    Order getOrderByOrderNumber(String orderNumber);

    int updateOrder(Order order);

    List<OrderListVo> getOrderListByUserId(@Param("userId")Long userId,@Param("parentId")Long parentId);
}
