package com.sky.mapper;

import com.sky.vo.OrderOverViewVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface WorkspaceMapper {
    @Select("select count(user_id) from  orders where order_time between #{beginTime} and #{endTime} and status=#{status} group by user_id ")
    Integer getuser(Map map);
@Select("select count(*) from setmeal where status=#{status}")
    Integer getsetmeal(Integer status);
@Select("select count(*) from dish where status=#{status}")
    Integer getdish(Integer disable);


}
