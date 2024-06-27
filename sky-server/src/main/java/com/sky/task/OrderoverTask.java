package com.sky.task;

import com.sky.constant.MessageConstant;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderoverTask {
    @Autowired
    private OrderMapper orderMapper;
    @Scheduled(cron = "0 * * * * ?")
    public void overorder(){
        //找到状态为未支付并且下单时间超时的订单
        log.info("定时检查超时订单");
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        List<Orders> ordersList=orderMapper.overorderlt(Orders.PENDING_PAYMENT,time);
        for (Orders orders : ordersList) {
            orders.setStatus(Orders.CANCELLED);
            orders.setCancelTime(LocalDateTime.now());
            orderMapper.setstatus(orders);
        }
    }
    @Scheduled(cron = "0 0 2 * * ?")
    public void completeorder(){
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        List<Orders> ordersList= orderMapper.findstatus(Orders.DELIVERY_IN_PROGRESS,time);
        for (Orders orders : ordersList) {
            orders.setStatus(Orders.COMPLETED);
            orderMapper.update(orders);
        }

    }
}
