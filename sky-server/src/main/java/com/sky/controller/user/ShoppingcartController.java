package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingcartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
public class ShoppingcartController {
    @Autowired
    private ShoppingcartService shoppingcartService;
    @GetMapping("/list")
    public Result<List<ShoppingCart>> getcart(){
        List<ShoppingCart> list=shoppingcartService.getcart();
        return Result.success(list);
    }
    @PostMapping("/add")
    public Result addcart(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("添加购物车:{} {} {}",shoppingCartDTO.getDishFlavor(),shoppingCartDTO.getDishId(),shoppingCartDTO.getSetmealId());
        shoppingcartService.addcart(shoppingCartDTO);
        return Result.success();
    }
    @DeleteMapping("/clean")
    public Result cleanall(){
        shoppingcartService.cleanall();
        return Result.success();
    }
    @PostMapping("/sub")
    public Result delete(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("删除购物车：{}",shoppingCartDTO);
        shoppingcartService.delete(shoppingCartDTO);
        return Result.success();
    }
}
