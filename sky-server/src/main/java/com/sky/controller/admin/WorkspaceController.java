package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/workspace")
@Slf4j
public class WorkspaceController {
    @Autowired
    private WorkspaceService workspaceService;
    @GetMapping("/businessData")
    public Result<BusinessDataVO> businessData(){
        log.info("订单数据统计");
        BusinessDataVO businessDataVO= workspaceService.businessData();
        return  Result.success(businessDataVO);
    }
    @GetMapping("/overviewSetmeals")
    public Result<SetmealOverViewVO> overviewSetmeals(){
        log.info("套餐总览");
        SetmealOverViewVO setmeal=workspaceService.overviewSetmeals();
        return Result.success(setmeal);
    }
    @GetMapping("/overviewDishes")
    public Result<DishOverViewVO> overviewDishes(){
        log.info("菜品查询");
        DishOverViewVO dish=workspaceService.overviewDishes();
        return Result.success(dish);
    }
    @GetMapping("/overviewOrders")
    public Result<OrderOverViewVO> overviewOrders(){
        log.info("订单查询");
        OrderOverViewVO orderOverViewVO=workspaceService.overviewOrders();
        return Result.success(orderOverViewVO);
    }
}
