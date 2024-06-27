package com.sky.mapper;

import com.sky.dto.GoodsSalesDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface ReportMapper {
    @Select("select sum(amount)  from orders where checkout_time between #{beginTime} and #{endTime} and status=#{status} ")
    Double turnoverStatistics(Map map);
@Select("select count(*) from user where create_time<#{endTime}")
    Integer userStatistics(Map map);
@Select("select count(*) from user where create_time between #{beginTime} and #{endTime}")
    Integer newuser(Map map);
@Select("select count(*) from orders where order_time between #{beginTime} and #{endTime} ")
    Integer dateordertoatl(Map map);
    @Select("select count(*) from orders where order_time between #{beginTime} and #{endTime} and status=#{status}")
    Integer dateordercomplete(Map map);
    @Select("select count(*) from orders ")
    Integer tatalorders();
    @Select("select count(*) from orders where status=#{status}")
    Integer totalordercomplete(Integer status);
@Select("select id  from orders where order_time between #{beginTime} and #{endTime}")
    List<Integer> top10orderid(LocalDateTime beginTime, LocalDateTime endTime);



    List<GoodsSalesDTO> top10(LocalDateTime beginTime, LocalDateTime endTime);
}
