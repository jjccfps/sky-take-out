package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderStatisticsVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {


     Page<Orders> selectbyuserid(OrdersPageQueryDTO ordersPageQueryDTO) ;

    void insertorder(Orders orders);
    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);
@Select("select *from orders where id=#{id}")
    Orders selectbyid(Long id);

@Select("select count(*) from orders  where status=#{status}  ")
    Integer selectstataus(Integer status);
@Update("update orders set status=#{status} where id=#{id}")
    void putconfirm(OrdersConfirmDTO ordersConfirmDTO);

@Update("update orders set status=#{status} where id=#{id} ")
    void delivery(Orders orders);
@Select("select *from orders where status=#{status} and order_time< #{time}")
    List<Orders> overorderlt(Integer status, LocalDateTime time);
@Update("update orders set status=#{status} , cancel_time=#{cancelTime} where id=#{id}")
    void setstatus( Orders orders);
@Select("select *from orders where status=#{status} and order_time<#{time}")
    List<Orders> findstatus(Integer status , LocalDateTime time);
}
