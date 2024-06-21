package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.CategoryServicce;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping("/list")
    public Result<List<DishVO>> listdish(Long categoryId){
        log.info("用户端菜品分类：{}",categoryId);
        List<DishVO> selectbycategoryid = dishService.userselectbycategoryid(categoryId);
        return Result.success(selectbycategoryid);
    }
}
