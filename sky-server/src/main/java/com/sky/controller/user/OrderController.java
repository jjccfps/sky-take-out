package com.sky.controller.user;

import com.alibaba.fastjson.JSON;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.websocket.WebSocketServer;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController("userordercontroller")
@RequestMapping("/user/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    WebSocketServer webSocketServer;
    @PostMapping("submit")
    public Result<OrderSubmitVO> postsubmit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        log.info("提交订单:{}",ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO=orderService.postsubmit(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }
    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        orderService.paySuccess(ordersPaymentDTO.getOrderNumber());
        return Result.success(orderPaymentVO);
    }
    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO>  selectorder(@PathVariable Long id){
        log.info("查看订单详情：{}",id);
        OrderVO orderVO= orderService.selectorder(id);
        return Result.success(orderVO);
    }
    @GetMapping("/historyOrders")
    public Result<PageResult> gethistoryorders(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("分页查询历史订单:{}",ordersPageQueryDTO);
        PageResult pageResult= orderService.gethistoryorders(ordersPageQueryDTO);
        return Result.success(pageResult);
    }
    @PutMapping("/cancel/{id}")
    @ApiOperation("取消订单")
    public Result cancel(@PathVariable("id") Long id) throws Exception {
        orderService.userCancelById(id);
        return Result.success();
    }
    @PostMapping("/repetition/{id}")
    @ApiOperation("再来一单")
    public Result repetition(@PathVariable Long id) {
        orderService.repetition(id);
        return Result.success();
    }
    @GetMapping("/reminder/{id}")
    public Result reminder(@PathVariable Long id){
        log.info("催单:{}",id);
        Map map=new HashMap();
        map.put("type",2);
        map.put("orderId",id);
        map.put("content","用户催单了");
        String jsonString = JSON.toJSONString(map);
        webSocketServer.sendToAllClient(jsonString);

        return Result.success();
    }


}
