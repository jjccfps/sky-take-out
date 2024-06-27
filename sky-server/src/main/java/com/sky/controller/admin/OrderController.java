package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminordercontroller")
@RequestMapping("/admin/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;
    @GetMapping("/conditionSearch")
    public Result<PageResult>  searchorder(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("订单查询：{}",ordersPageQueryDTO);
        PageResult gethistoryorders = orderService.searchorder(ordersPageQueryDTO);
        return Result.success(gethistoryorders);
    }
    @GetMapping("/details/{id}")
    public Result<OrderVO>  selectorder(@PathVariable Long id){
        log.info("查看订单详情：{}",id);
        OrderVO orderVO= orderService.selectorder(id);
        return Result.success(orderVO);
    }
    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> selectstataus(){
        log.info("查数量");
        OrderStatisticsVO orderStatisticsVO= orderService.selectstataus();
        return Result.success(orderStatisticsVO);
    }
    @PutMapping("/confirm")
    public Result  putconfirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO){
        log.info("接单:{}",ordersConfirmDTO);
        orderService.putconfirm(ordersConfirmDTO);
        return Result.success();
    }
    @PutMapping("/rejection")
    public Result putrejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO){
        log.info("拒单:{}",ordersRejectionDTO);
        orderService.putrejection(ordersRejectionDTO);
        return Result.success();
    }
    @PutMapping("/cancel")
    public Result cancle(@RequestBody OrdersCancelDTO ordersCancelDTO){
        log.info("取消订单:{}",ordersCancelDTO);
        orderService.cancle(ordersCancelDTO);
        return Result.success();
    }
    @PutMapping("/delivery/{id}")
    public Result delivery(@PathVariable Long id ){
        log.info("开始派送：{}",id);
        orderService.delivery(id);
        return Result.success();
    }
    @PutMapping("/complete/{id}")
    public Result complteorder(@PathVariable Long id ){
        log.info("完成订单：{}",id);
        orderService.complete(id);
        return Result.success();
    }

}
