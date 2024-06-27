package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    OrderSubmitVO postsubmit(OrdersSubmitDTO ordersSubmitDTO);
    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    OrderVO selectorder(Long id);

    PageResult gethistoryorders(OrdersPageQueryDTO ordersPageQueryDTO);
    void userCancelById(Long id) throws Exception;
    void repetition(Long id);

    PageResult searchorder(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderStatisticsVO selectstataus();

    void putconfirm(OrdersConfirmDTO ordersConfirmDTO);

    void putrejection(OrdersRejectionDTO ordersRejectionDTO);

    void cancle(OrdersCancelDTO ordersCancelDTO);

    void delivery(Long id);

    void complete(Long id);
}
