package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StemaldishMapper {

    @Select("select count(setmeal_id) from setmeal_dish where dish_id=#{id} ")
    int getsetmealid(Long id);
}
