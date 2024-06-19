package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @GetMapping("/page")
    public Result<PageResult> selectpage(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询：{}",dishPageQueryDTO);
        PageResult pageResult=dishService.selectpage(dishPageQueryDTO);
        return Result.success(pageResult);
    }
    @PostMapping
    public Result insertdish(@RequestBody DishDTO dishDTO){
        log.info("新增菜品:{}",dishDTO);
        dishService.insertdish(dishDTO);
        return Result.success();
    }
    @GetMapping("/{id}")
    public Result<DishVO> selectbyid(@PathVariable Long id){
        log.info("根据id查询：{}",id);
        DishVO dishVO=dishService.insertbyid(id);
        return Result.success(dishVO);
    }
    @GetMapping("/list")
    public Result<List<Dish>> selectbycategoryid(Long categoryId){
        log.info("分类id：{}",categoryId);
       List<Dish> dishs=dishService.selectbycategoryid(categoryId);
        return Result.success(dishs);
    }
    @DeleteMapping
    public Result deletedish(@RequestParam Long[] ids){
        log.info("批量删除id:{}",ids);
        dishService.deletedish(ids);
        return Result.success();
    }
    @PutMapping
    public Result updatadish(@RequestBody DishDTO dishDTO){
        log.info("修改员工:{}",dishDTO);
        dishService.updatadish(dishDTO);
        return Result.success();
    }
    @PostMapping("/status/{status}")
    public Result updatestatus(@PathVariable Integer status,Long id){
        log.info("修改菜品状态：{} {}",status,id);
        dishService.updatestatus(status,id);
        return Result.success();
    }
}
