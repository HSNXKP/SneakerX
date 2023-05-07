package top.naccl.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.aspectj.weaver.ast.Or;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import top.naccl.entity.Order;
import top.naccl.entity.ProductSize;
import top.naccl.model.vo.OrderAminVo;
import top.naccl.model.vo.OrderListVo;
import top.naccl.model.vo.Result;

import java.util.List;

@Mapper
@Repository
public interface OrderMapper {
    int summitOrder(Order order);


    int getProductOrderCountByUserId(@Param("userId") Long userId, @Param("productId") Long productId);

    Order getOrderByOrderNumberWithUserId(@Param("orderNumber") String orderNumber,@Param("userId") Long userId,@Param("parentId")Long parentId);

    int updateOrder(Order order);

    List<OrderListVo> getOrderListByUserId(@Param("userId")Long userId,@Param("parentId")Long parentId,@RequestParam("status") Long status);

    List<Order> getOrderListByOrderNumberWithUserId(@Param("orderNumber")String orderNumber,@Param("userId")Long userId,@Param("parentId")Long parentId);

    int deleteOrderByOrderNumber(@Param("orderNumber")String orderNumber,@Param("userId") Long userId,@Param("parentId")Long parentId);


    List<OrderAminVo> getAllOrderList(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("orderNumber") String orderNumber, @Param("status") Long status, @Param("parentId") Long parentId);

    List<Order> getOrderById(@Param("id") Long id,@Param("parentId") Long parentId);

    int deleteOrderById(@Param("id") Long id,@Param("parentId") Long parentId);

    int updateExpress(Long id, String express,Long parentId);

    void updateStatus(Long id, Long status,Long parentId);

}
