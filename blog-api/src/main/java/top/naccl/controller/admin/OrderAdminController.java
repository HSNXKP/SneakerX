package top.naccl.controller.admin;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.model.vo.Result;
import top.naccl.service.OrderService;

/**
 * @Author wdd
 * @Date 2023/4/26 17:21
 * @PackageName:top.naccl.controller.admin
 * @ClassName: OrderAdminController
 * @Version 1.0
 */
@RestController
@RequestMapping("/admin")
public class OrderAdminController {

    @Autowired
    private OrderService orderService;


    @GetMapping("/getAllOrder")
    public Result getAllOrder(@RequestParam(defaultValue = "") String[] date,
                              @RequestParam String orderNumber,
                              @RequestParam Long status,
                              @RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize){
        String startDate = null;
        String endDate = null;
        if (date.length == 2) {
            startDate = date[0];
            endDate = date[1];
        }
        return orderService.getAllOrder(startDate,endDate,orderNumber,status,pageNum,pageSize);
    }

    @GetMapping("/deleteOrder")
    public Result deleteOrder(@RequestParam Long id){
        return orderService.deleteOrder(id);
    }

    @GetMapping("/updateExpress")
    public Result updateOrder(@RequestParam Long id,@RequestParam String express){
        return orderService.updateExpress(id,express);
    }
}
