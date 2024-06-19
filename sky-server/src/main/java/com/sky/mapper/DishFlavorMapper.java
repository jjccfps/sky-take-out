package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    void insertflavors(List<DishFlavor> flavors);
    @Select("select *from dish_flavor where dish_id=#{dishId} ")
    List<DishFlavor> selectbyid(Long dishId);


    void deletedishflavor(Long[] ids);

    void updata(DishFlavor dishFlavor);
}
