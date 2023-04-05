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

    int deleteAddress(@Param("id") Long id);

    int updateAddress(Address address);

    int isExistAddress(Address address);

    int setOtherAddressNotDefault(@Param("id") Long id);

    List<Address> getAddressList(@Param("id") Long id);
}
