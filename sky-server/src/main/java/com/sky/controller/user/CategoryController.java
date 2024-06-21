package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryServicce;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("usercategorycontroller")
@RequestMapping("/user/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryServicce categoryServicce;
    @GetMapping("/list")
    public Result<List<Category>> listtype(Integer type){
        log.info("用户端菜品分类：{}",type);
        List<Category> selecttype = categoryServicce.selecttype(type);
        return Result.success(selecttype);
    }
}
