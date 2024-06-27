package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingcartMapper;
import com.sky.service.ShoppingcartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingcartServiceimpl implements ShoppingcartService {
    @Autowired
    private ShoppingcartMapper shoppingcartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;


    @Override
    public List<ShoppingCart> getcart() {
        Long userId = BaseContext.getCurrentId();
        return shoppingcartMapper.getcart(userId);
    }

    @Override
    public void addcart(ShoppingCartDTO shoppingCartDTO) {
        //添加前先判断该商品在购物车是否已经存在，存在只需要修改原数据number加1就行，不存在再添加
        //动态查询
        ShoppingCart shoppingCart = new ShoppingCart();
        Long userId = BaseContext.getCurrentId();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(userId);

        List<ShoppingCart> list = shoppingcartMapper.select(shoppingCart);
        if (list != null && list.size() > 0) {
            list.get(0).setNumber(list.get(0).getNumber() + 1);
            shoppingcartMapper.update(list.get(0));

        } else {

            //因为每次添加套餐和菜品只能添加一个，所以进行判断
            Long dishId = shoppingCartDTO.getDishId();
            Long setmealId = shoppingCartDTO.getSetmealId();
            if (dishId != null) {
                String dishFlavor = shoppingCartDTO.getDishFlavor();
                Dish dish = dishMapper.selectbyid(dishId);
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setName(dish.getName());
                shoppingCart.setAmount(dish.getPrice());
                if (dishFlavor != null) {
                    shoppingCart.setDishFlavor(dishFlavor);
                }

            } else if (setmealId != null) {
                Setmeal setmeal = setmealMapper.setmealbyid(setmealId);
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setAmount(setmeal.getPrice());

            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingcartMapper.addcart(shoppingCart);

        }
    }

    @Override
    public void cleanall() {
        Long userId = BaseContext.getCurrentId();
        shoppingcartMapper.cleanall(userId);
    }

    @Override
    public void delete(ShoppingCartDTO shoppingCartDTO) {
        //添加前先判断该商品在购物车number是否大于1，大于只需要修改原数据number减1就行，等于再删除
        //动态查询
        ShoppingCart shoppingCart = new ShoppingCart();
        Long userId = BaseContext.getCurrentId();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(userId);

        List<ShoppingCart> list = shoppingcartMapper.select(shoppingCart);
        if (list.get(0).getNumber() > 1) {
            list.get(0).setNumber(list.get(0).getNumber() - 1);
            shoppingcartMapper.update(list.get(0));

        } else {

            //因为每次添加套餐和菜品只能删除一个，所以进行判断
            Long dishId = shoppingCartDTO.getDishId();
            Long setmealId = shoppingCartDTO.getSetmealId();

                shoppingcartMapper.delete(dishId, setmealId);



        }
    }
}