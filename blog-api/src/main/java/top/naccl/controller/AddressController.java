package top.naccl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.naccl.entity.Address;
import top.naccl.model.vo.Result;
import top.naccl.service.AddressService;

/**
 * @author: wdd
 * @date: 2023/4/5 23:30
 */
@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    /**
     * 保存地址需要登录
     * @param address
     * @return
     */
    @PostMapping("/user/saveAddress")
    public Result address(@RequestBody Address address,@RequestParam("id") Long id) {
        // TODO: 可能存在登录其他账号 请求该接口仍然被保存的问题 因为没有校验当前的用户token和传入的id是否一致
        return addressService.saveAddress(address,id);
    }

    /**
     * 删除地址需要登录
     */
    @GetMapping("/user/deleteAddress")
    public Result deleteAddress(@RequestParam("id") Long id,@RequestParam("userId") Long userId) {
        return addressService.deleteAddress(id,userId);
    }

    /**
     * 修改地址需要登录
     */
    @PostMapping("/user/updateAddress")
    public Result updateAddress(@RequestBody Address address) {
        return addressService.updateAddress(address);
    }

    /**
     * 通过userId获取地址列表需要登录
     */
    @GetMapping("/user/getAddressList")
    public Result getAddressList(@RequestParam("id") Long id) {
        return addressService.getAddressList(id);
    }

    @GetMapping("/user/changeDefaultAddress")
    public Result changeDefaultAddress(@RequestParam("id") Long id,@RequestParam("userId") Long userId) {
        return addressService.changeDefaultAddress(id,userId);
    }


}
