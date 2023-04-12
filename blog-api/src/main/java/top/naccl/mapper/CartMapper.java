package top.naccl.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.naccl.model.vo.Result;

@Mapper
@Repository
public interface CartMapper {
    Result addCart(@Param("productId") String productId,@Param("userId") String userId);

}
