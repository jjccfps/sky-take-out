package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ReportMapper;
import com.sky.mapper.WorkspaceMapper;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class WorkspaceServiceimpl implements WorkspaceService {
    @Autowired
    private WorkspaceMapper workspaceMapper;
    @Autowired
    private ReportMapper reportMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Override
    public BusinessDataVO businessData() {
        BusinessDataVO businessDataVO = new BusinessDataVO();
        //查询的日期今天
        LocalDate date = LocalDate.now();
        LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
        Map map=new HashMap();
        map.put("beginTime",beginTime);
        map.put("endTime",endTime);
        //新增用户
        Integer newuser = reportMapper.newuser(map);
        businessDataVO.setNewUsers(newuser);
        //订单完成率
        map.put("status", Orders.COMPLETED);
        Integer  toatls=reportMapper.dateordertoatl(map);
        Integer completes = reportMapper.dateordercomplete(map);
        int total = toatls.intValue();
        int complete = completes.intValue();
        double totalcomplete=complete / total;
        Double aDouble = new Double(totalcomplete);
        businessDataVO.setOrderCompletionRate(aDouble);
        //营业额
        Double amount = reportMapper.turnoverStatistics(map);
        businessDataVO.setTurnover(amount);
        //完成订单数
        Integer completeorder = reportMapper.dateordercomplete(map);
        businessDataVO.setValidOrderCount(completeorder);
        //平均客单价
        //查找今日总共点单成功的用户
        Integer user=workspaceMapper.getuser(map);
        double count = amount.doubleValue();
        int users = user.intValue();
        double pate=count / users;
        Double aDouble1 = new Double(pate);
        businessDataVO.setUnitPrice(aDouble1);
        return businessDataVO;


    }

    @Override
    public SetmealOverViewVO overviewSetmeals() {
        SetmealOverViewVO setmeal = new SetmealOverViewVO();
        //查询停售套餐
       Integer stopsetmeal= workspaceMapper.getsetmeal(StatusConstant.DISABLE);
       //查询起售套餐
        Integer saldsetmeal=workspaceMapper.getsetmeal(StatusConstant.ENABLE);
        setmeal.setDiscontinued(stopsetmeal);
        setmeal.setSold(saldsetmeal);
        return setmeal;
    }

    @Override
    public DishOverViewVO overviewDishes() {
        DishOverViewVO dish = new DishOverViewVO();
        Integer stopdish=workspaceMapper.getdish(StatusConstant.DISABLE);
        Integer solddish=workspaceMapper.getdish(StatusConstant.ENABLE);
        dish.setDiscontinued(stopdish);
        dish.setSold(solddish);
        return dish;
    }

    @Override
    public OrderOverViewVO overviewOrders() {
        OrderOverViewVO orderOverViewVO = new OrderOverViewVO();
        //待接单数量
        Integer beconfirmed = orderMapper.selectstataus(Orders.TO_BE_CONFIRMED);
        orderOverViewVO.setWaitingOrders(beconfirmed);
        //待派送数量
        Integer confirmed = orderMapper.selectstataus(Orders.CONFIRMED);
        orderOverViewVO.setDeliveredOrders(confirmed);
        //已完成数量
        Integer complete = orderMapper.selectstataus(Orders.COMPLETED);
        orderOverViewVO.setCompletedOrders(complete);
        //已取消数量
        Integer cancel = orderMapper.selectstataus(Orders.CANCELLED);
        orderOverViewVO.setCancelledOrders(cancel);
        //全部订单
        Integer tatol = reportMapper.tatalorders();
        orderOverViewVO.setAllOrders(tatol);
        return orderOverViewVO;
    }

}
