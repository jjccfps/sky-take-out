package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderdetailMapper {
@Insert("insert into order_detail ( name, image, order_id, dish_id, setmeal_id, dish_flavor, number, amount)" +
        "values (#{name},#{image},#{orderId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount})")
    void insertorderdetail(OrderDetail orderDetail);
@Select("select *from order_detail where order_id=#{orderId}")
    List<OrderDetail> selectbyid(Long orderId);
}
