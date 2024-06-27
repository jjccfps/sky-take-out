package com.sky.mapper;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingcartMapper {
    @Select("select *from shopping_cart where user_id=#{userId}")
    List<ShoppingCart> getcart(Long userId);
@Insert("insert into shopping_cart (name, image, user_id, dish_id, setmeal_id, dish_flavor, amount, create_time,number) values (#{name},#{image},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{amount},#{createTime},#{number})")
    void addcart(ShoppingCart shoppingCart);

    List<ShoppingCart> select( ShoppingCart shoppingCart);
@Update("update shopping_cart set number=#{number}  where id=#{id} ")
    void update(ShoppingCart shoppingCart);
@Delete("delete from shopping_cart where user_id=#{userId} ")
    void cleanall(Long userId);

    void delete(Long dishId,Long setmealId);


    void insertBatch(List<ShoppingCart> shoppingCartList);
}
