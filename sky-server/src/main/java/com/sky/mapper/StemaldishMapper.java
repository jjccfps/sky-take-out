package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StemaldishMapper {

    @Select("select count(setmeal_id) from setmeal_dish where dish_id=#{id} ")
    int getsetmealid(Long id);
    @Select("select *from setmeal_dish where setmeal_id=#{setmealId}")
   List<SetmealDish>  setmealbyid(Long setmealId);
@Insert("insert into  setmeal_dish (setmeal_id, dish_id, name, price, copies) " +
        "values (#{setmealId},#{ dishId},#{name},#{price},#{copies})")
    void insertsetmeal(SetmealDish setmealDish);

    void updatesetmeal(SetmealDish setmealDish);

    void delete(String[] ids);
}
