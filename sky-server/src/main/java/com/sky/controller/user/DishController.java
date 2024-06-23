package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.CategoryServicce;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userdishcontroller")
@RequestMapping("/user/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;
    @GetMapping("/list")
    public Result<List<DishVO>> listdish(Long categoryId){
        //为了缓解数据库压力,将用户端菜品显示放到redis缓存当中

        //构造key  格式:dish_id
        String key="dish_"+categoryId;
        //判断redis中是否存在, 存在直接查询,不存在查询数据库再存储到redis中
        ValueOperations valueOperations = redisTemplate.opsForValue();
        List<DishVO> list =(List<DishVO>) valueOperations.get(key);
        //存在
        if (list !=null && list.size()>0){
            return Result.success(list);

        }

        //不存在
        log.info("用户端菜品分类：{}",categoryId);
        List<DishVO> selectbycategoryid = dishService.userselectbycategoryid(categoryId);
        valueOperations.set(key,selectbycategoryid);

        return Result.success(selectbycategoryid);
    }
}
