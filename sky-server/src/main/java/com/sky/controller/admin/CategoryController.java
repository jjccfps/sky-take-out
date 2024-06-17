package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryServicce;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/category")
public class CategoryController {
    @Autowired
   private CategoryServicce categoryServicce;
    @GetMapping("/page")
    public Result<PageResult> selectclassify(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("分类分页查询:{}",categoryPageQueryDTO);
        PageResult pageResult=categoryServicce.selectclassify(categoryPageQueryDTO);
        return  Result.success(pageResult);

    }
    @PostMapping
    public Result instert(@RequestBody CategoryDTO categoryDTO){
        log.info("新增分类：{}",categoryDTO);
       categoryServicce.instert(categoryDTO);
        return Result.success();
    }
    @DeleteMapping
    public Result deletebyid(Long id){
        log.info("删除id：{}",id);
        categoryServicce.delete(id);
        return Result.success();
    }
    @GetMapping("/list")
    public Result<List<Category>> selecttype(Integer type){
            log.info("根据类型查找：{}",type);
        List<Category> list=categoryServicce.selecttype(type);
        return Result.success(list);
    }
    @PostMapping("/status/{status}")
    public Result upadtastatus(@PathVariable Integer status,Long id){
        log.info("改状态；{} {}",status,id);
        categoryServicce.updatastatus(status,id);
        return Result.success();
    }
    @PutMapping
    public Result updatatype(@RequestBody CategoryDTO categoryDTO){
        log.info("修改:{}",categoryDTO);
        categoryServicce.updatatype(categoryDTO);
        return Result.success();
    }

}
