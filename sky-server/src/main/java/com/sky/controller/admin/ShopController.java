package com.sky.controller.admin;

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/admin/shop")
@RestController("adminshopcontroller")
public class ShopController {
    public static final String KEY="Shop_Status";
    @Autowired
    private RedisTemplate redisTemplate;
    @PutMapping("/{status}")
    public Result shopstatus(@PathVariable Integer status){
        log.info("店铺营业状态:{}",status);
        //在redis中存对应的状态，用于回显
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(KEY,status);
        return Result.success();
    }
    @GetMapping("/status")
    public Result<Integer> getshopstatus(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Integer status = (Integer) valueOperations.get(KEY);
       log.info("获取的status:{}",status);
        return Result.success(status);
    }
}
