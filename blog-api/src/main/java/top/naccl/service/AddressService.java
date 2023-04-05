package top.naccl.service;

import top.naccl.entity.Address;
import top.naccl.model.vo.Result;

public interface AddressService {
    Result saveAddress(Address address, Long id);

    Result deleteAddress(Long id);

    Result updateAddress(Address address);

    Result getAddressList(Long id);
}
