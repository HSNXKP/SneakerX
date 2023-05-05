package top.naccl.service.impl;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.naccl.entity.Address;
import top.naccl.mapper.AddressMapper;
import top.naccl.model.vo.Result;
import top.naccl.service.AddressService;
import top.naccl.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author: wdd
 * @date: 2023/4/5 23:31
 */
@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public Result saveAddress(Address address, Long id) {
        //四个必要信息不能为空
        if (StringUtils.isEmpty(address.getName(),address.getPhone(),address.getAddress(),address.getAddressDetail())){
            return Result.error("详细信息不能为空");
        }
        // 设置用户id
        address.setUserId(id);
        // 判断当前的地址是否存在
        int existAddress = addressMapper.isExistAddress(address);
        if (existAddress > 1) {
            return Result.error("该地址已存在");
        }
        // 判断是否设置为默认地址
        if (address.getIsDefaultAddress()) {
            // 设置其他地址为非默认地址
            addressMapper.setOtherAddressNotDefault(id);
        }
        // 设置创建时间
        address.setCreateTime(LocalDateTime.now());
        // 设置更新时间
        address.setUpdateTime(LocalDateTime.now());
        try {
            if (addressMapper.saveAddress(address) != 1) {
                throw new RuntimeException("保存地址失败");
            }
            return Result.ok("保存地址成功");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Result deleteAddress(Long id,Long userId) {
        try {
            if (addressMapper.deleteAddress(id,userId) != 1) {
                throw new RuntimeException("删除地址成功");
            }
            return Result.ok("删除地址成功");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Result updateAddress(Address address) {
        try {
            if (addressMapper.updateAddress(address) != 1) {
                throw new RuntimeException("更新地址成功");
            }
            return Result.ok("更新地址成功");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Result getAddressList(Long id) {
        return Result.ok("获取成功",addressMapper.getAddressList(id));
    }

    @Override
    public Result changeDefaultAddress(Long id,Long userId) {
        try {
            // 设置其他地址为非默认地址
            addressMapper.setOtherAddressNotDefault(userId);
            // 设置当前地址为默认地址
            addressMapper.setAddressDefault(id);
            return Result.ok("设置默认地址成功");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
