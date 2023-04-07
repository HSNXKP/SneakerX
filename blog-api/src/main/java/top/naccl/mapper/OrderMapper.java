package top.naccl.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.naccl.entity.Order;
import top.naccl.entity.ProductSize;

import java.util.List;

@Mapper
@Repository
public interface OrderMapper {
    int summitOrder(Order order);


    int getProductOrderCountByUserId(@Param("userId") Long userId, @Param("productId") Long productId);
}
