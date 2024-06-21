package com.sky.controller.user;

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/user/shop")
@RestController("usershopcontroller")
public class ShopController {
    public static final String KEY="Shop_Status";
    @Autowired
    private RedisTemplate redisTemplate;
    @GetMapping("/status")
    public Result<Integer> getshopstatus(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Integer status = (Integer) valueOperations.get(KEY);
       log.info("获取的status:{}",status);
        return Result.success(status);
    }
}
