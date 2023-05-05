package top.naccl.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.naccl.entity.Address;
import top.naccl.model.vo.Result;

import java.util.List;

@Mapper
@Repository
public interface AddressMapper {
    int saveAddress(Address address);

    int deleteAddress(@Param("id") Long id,@Param("userId") Long userId);

    int updateAddress(Address address);

    int isExistAddress(Address address);

    /**
     * 通过userId设置其他地址为非默认地址
     * @param id
     * @return
     */
    int setOtherAddressNotDefault(@Param("id") Long id);

    /**
     * 通过userId获取地址列表
     * @param id
     * @return
     */
    List<Address> getAddressList(@Param("id") Long id);

    /**
     * 通过收获地址id设置单个默认地址
     * @param id
     * @return
     */
    int setAddressDefault(@Param("id") Long id);

    /**
     * 通过收获地址id获取地址信息
     * @param addressId
     * @return
     */
    Address getAddressById(@Param("addressId") Long addressId);
}
