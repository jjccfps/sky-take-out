package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
//import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;

import com.sky.vo.OrderVO;
import com.sky.websocket.WebSocketServer;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketClient;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceimpl implements OrderService {
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private ShoppingcartMapper shoppingcartMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderdetailMapper orderdetailMapper;
//    @Autowired
//    private WeChatPayUtil weChatPayUtil;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WebSocketServer webSocketServer;


    @Override
    public OrderSubmitVO postsubmit(OrdersSubmitDTO ordersSubmitDTO) {
        Long userId = BaseContext.getCurrentId();
        //判断对应的地址簿是否有值
        Long addressBookId = ordersSubmitDTO.getAddressBookId();
        AddressBook addressBook= addressMapper.getbyid(addressBookId);
        if (addressBook ==null  ){
            throw new ShoppingCartBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        //判断购物车是否有值,通过userid
        List<ShoppingCart> getcarts = shoppingcartMapper.getcart(userId);
        if (getcarts ==null || getcarts.size()==0){
            throw  new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }
        //在orders表中添加一条数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO,orders);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setNumber(String.valueOf(System.currentTimeMillis()) );
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        orders.setUserId(userId);
        orderMapper.insertorder(orders);
        //将购物车里的信息封装到详细表中
        List<OrderDetail> list= new ArrayList<>();
        for (ShoppingCart getcart : getcarts) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(getcart,orderDetail);
            orderDetail.setOrderId(orders.getId());
            list.add(orderDetail);
        }
        for (OrderDetail orderDetail : list) {
            orderdetailMapper.insertorderdetail(orderDetail);
        }
        //清空购物车
        shoppingcartMapper.cleanall(userId);
        //将数据封装到ordersubmitvo中
        OrderSubmitVO orderSubmitVO = new OrderSubmitVO();
        orderSubmitVO.setOrderTime(orders.getOrderTime());
        orderSubmitVO.setOrderAmount(orders.getAmount());
        orderSubmitVO.setOrderNumber(orders.getNumber());
        orderSubmitVO.setId(orders.getId());
        return orderSubmitVO;
    }
    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

       /* //调用微信支付接口，生成预支付交易单
        JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal(0.01), //支付金额，单位 元
                "苍穹外卖订单", //商品描述
                user.getOpenid() //微信用户的openid
        );*/
        JSONObject jsonObject = new JSONObject();

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();
        orderMapper.update(orders);
        Map map=new HashMap();
        map.put("type",1);
        map.put("orderId",ordersDB.getId());
        map.put("content","订单号:"+outTradeNo);
        String jsonString = JSON.toJSONString(map);
        webSocketServer.sendToAllClient(jsonString);



    }

    @Override
    public OrderVO selectorder(Long id) {
        Orders orders=orderMapper.selectbyid(id);
        //查询该订单下的菜品详细
        List<OrderDetail> list= orderdetailMapper.selectbyid(orders.getId());
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderDetailList(list);
        BeanUtils.copyProperties(orders,orderVO);
        return orderVO;
    }

    @Override
    public PageResult gethistoryorders(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(),ordersPageQueryDTO.getPageSize());
        Long userId = BaseContext.getCurrentId();
        ordersPageQueryDTO.setUserId(userId);
        Page<Orders> ordersList = orderMapper.selectbyuserid(ordersPageQueryDTO);
       List<OrderVO> list=new ArrayList<>();
        for (Orders orders : ordersList) {
            List<OrderDetail> details= orderdetailMapper.selectbyid(orders.getId());
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(orders,orderVO);
            orderVO.setOrderDetailList(details);
            list.add(orderVO);
        }

    return new PageResult(ordersList.getTotal(),list);

    }

    @Override
    public void userCancelById(Long id) throws Exception {

            // 根据id查询订单
            Orders ordersDB = orderMapper.selectbyid(id);

            // 校验订单是否存在
            if (ordersDB == null) {
                throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
            }

            //订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
            if (ordersDB.getStatus() > 2) {
                throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
            }

            Orders orders = new Orders();
            orders.setId(ordersDB.getId());

            // 订单处于待接单状态下取消，需要进行退款
            if (ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
                //调用微信支付退款接口
                /*weChatPayUtil.refund(
                        ordersDB.getNumber(), //商户订单号
                        ordersDB.getNumber(), //商户退款单号
                        new BigDecimal(0.01),//退款金额，单位 元
                        new BigDecimal(0.01));//原订单金额*/

                //支付状态修改为 退款
                orders.setPayStatus(Orders.REFUND);
            }

            // 更新订单状态、取消原因、取消时间
            orders.setStatus(Orders.CANCELLED);
            orders.setCancelReason("用户取消");
            orders.setCancelTime(LocalDateTime.now());
            orderMapper.update(orders);
        }
    @Override
    public void repetition(Long id) {
        // 查询当前用户id
        Long userId = BaseContext.getCurrentId();

        // 根据订单id查询当前订单详情
        List<OrderDetail> orderDetailList = orderdetailMapper.selectbyid(id);

        // 将订单详情对象转换为购物车对象
        List<ShoppingCart> shoppingCartList = orderDetailList.stream().map(x -> {
            ShoppingCart shoppingCart = new ShoppingCart();

            // 将原订单详情里面的菜品信息重新复制到购物车对象中
            BeanUtils.copyProperties(x, shoppingCart, "id");
            shoppingCart.setUserId(userId);
            shoppingCart.setCreateTime(LocalDateTime.now());

            return shoppingCart;
        }).collect(Collectors.toList());

        // 将购物车对象批量添加到数据库
        shoppingcartMapper.insertBatch(shoppingCartList);
    }

    @Override
    public PageResult searchorder(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());

        Page<Orders> page = orderMapper.selectbyuserid(ordersPageQueryDTO);


        // 需要返回订单菜品信息，自定义OrderVO响应结果
        List<OrderVO> orderVOList = new ArrayList<>();

        List<Orders> ordersList = page.getResult();
        if (!Collections.isEmpty(ordersList)) {
            for (Orders orders : ordersList) {
                // 将共同字段复制到OrderVO
                OrderVO orderVO = new OrderVO();
                AddressBook addressBook= addressMapper.getbyid(orders.getAddressBookId());
                String address=addressBook.getProvinceName()+"-"+addressBook.getCityName()+"-"+addressBook.getDistrictName()+"-"+addressBook.getDetail();
                orders.setAddress(address);
                BeanUtils.copyProperties(orders, orderVO);
                String orderDishes = getOrderDishesStr(orders);
                // 将订单菜品信息封装到orderVO中，并添加到orderVOList
                orderVO.setOrderDishes(orderDishes);
                orderVOList.add(orderVO);
            }
        }

        return new PageResult(page.getTotal(), orderVOList);
    }

    @Override
    public OrderStatisticsVO selectstataus() {
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        Integer toBeConfirmed=orderMapper.selectstataus(Orders.TO_BE_CONFIRMED);
        Integer confirmed=orderMapper.selectstataus(Orders.CONFIRMED);
        Integer deliveryInProgress=orderMapper.selectstataus(Orders.DELIVERY_IN_PROGRESS);
        orderStatisticsVO.setToBeConfirmed(toBeConfirmed);
        orderStatisticsVO.setDeliveryInProgress(deliveryInProgress);
        orderStatisticsVO.setConfirmed(confirmed);
        return orderStatisticsVO;
    }

    @Override
    public void putconfirm(OrdersConfirmDTO ordersConfirmDTO) {
        //根据id将订单状态修改为待配送
        ordersConfirmDTO.setStatus(Orders.CONFIRMED);
        orderMapper.putconfirm(ordersConfirmDTO);

    }

    @Override
    public void putrejection(OrdersRejectionDTO ordersRejectionDTO) {
        // 根据id查询订单
        Orders ordersDB = orderMapper.selectbyid(ordersRejectionDTO.getId());

        // 订单只有存在且状态为2（待接单）才可以拒单
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        //支付状态
      /*  Integer payStatus = ordersDB.getPayStatus();
        if (payStatus == Orders.PAID) {
            //用户已支付，需要退款
            String refund = weChatPayUtil.refund(
                    ordersDB.getNumber(),
                    ordersDB.getNumber(),
                    new BigDecimal(0.01),
                    new BigDecimal(0.01));
            log.info("申请退款：{}", refund);
        }*/

        // 拒单需要退款，根据订单id更新订单状态、拒单原因、取消时间
        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setRejectionReason(ordersRejectionDTO.getRejectionReason());
        orders.setCancelTime(LocalDateTime.now());

        orderMapper.update(orders);
    }

    @Override
    public void cancle(OrdersCancelDTO ordersCancelDTO) {
        Orders orders = orderMapper.selectbyid(ordersCancelDTO.getId());
        //只要状态为待派送(3)才能取消订单
        Integer status = orders.getStatus();
        if (status !=Orders.CONFIRMED){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        //退款给用户
      /*  String refund = weChatPayUtil.refund(
                ordersDB.getNumber(),
                ordersDB.getNumber(),
                new BigDecimal(0.01),
                new BigDecimal(0.01));
        log.info("申请退款：{}", refund);*/
        //修改状态为已取消，修改取消原因
        Orders order = new Orders();
        order.setId(ordersCancelDTO.getId());
        order.setStatus(Orders.CANCELLED);
        order.setCancelReason(ordersCancelDTO.getCancelReason());
        order.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);

    }

    @Override
    public void delivery(Long id) {
        //判断只有为待派送才能派送
        Orders orders = orderMapper.selectbyid(id);
        Integer status = orders.getStatus();
        if (!status.equals(Orders.CONFIRMED)){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        //修改状态
        Integer stus=Orders.DELIVERY_IN_PROGRESS;
        Orders order = new Orders();
        order.setStatus(stus);
        order.setId(id);
        orderMapper.delivery(order);
    }

    @Override
    public void complete(Long id) {
        //判断只有为派送中的才能完成
        Orders orders = orderMapper.selectbyid(id);
        Integer status = orders.getStatus();
        if (status !=Orders.DELIVERY_IN_PROGRESS){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        //修改状态
        Integer stus=Orders.COMPLETED;
        Orders order = new Orders();
        order.setStatus(stus);
        order.setId(id);
        orderMapper.delivery(order);
    }

    private String getOrderDishesStr(Orders orders) {
        // 查询订单菜品详情信息（订单中的菜品和数量）
        List<OrderDetail> orderDetailList = orderdetailMapper.selectbyid(orders.getId());

        // 将每一条订单菜品信息拼接为字符串（格式：宫保鸡丁*3；）
        List<String> orderDishList = orderDetailList.stream().map(x -> {
            String orderDish = x.getName() + "*" + x.getNumber() + ";";
            return orderDish;
        }).collect(Collectors.toList());

        // 将该订单对应的所有菜品信息拼接在一起
        return String.join("", orderDishList);
    }
}


