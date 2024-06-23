package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/setmeal")
public class SetmealController {
   @Autowired
    private SetmealService setmealService;
   @GetMapping("/page")
    public Result<PageResult> selectpage( SetmealPageQueryDTO setmealPageQueryDTO){
       log.info("分页查询：{}",setmealPageQueryDTO);
       PageResult pageResult=setmealService.selectpage(setmealPageQueryDTO);
       return Result.success(pageResult);
   }
   @GetMapping("{id}")
    public Result<SetmealVO> selectbyid(@PathVariable Long id){
       log.info("id查询:{}",id);
       SetmealVO setmealVO= setmealService.selectbyid(id);
       return Result.success(setmealVO);
   }
   @PostMapping
   @CacheEvict(cacheNames = "setmeal" ,key = "#setmealDTO.categoryId")
    public Result insertsetmeal(@RequestBody SetmealDTO setmealDTO){
       log.info("新增套餐:{}",setmealDTO);
       setmealService.insertsetmeal(setmealDTO);
       return Result.success();
   }
   @PutMapping
   @CacheEvict(cacheNames = "setmeal",allEntries = true)
    public Result updatestemal(@RequestBody SetmealVO setmealVO){
       log.info("修改套餐:{}",setmealVO);
       setmealService.updatestemal(setmealVO);
       return Result.success();
   }
   @PostMapping("/status/{status}")
   @CacheEvict(cacheNames = "setmeal",allEntries = true)
   public Result updatesttus(@PathVariable Integer status,Long id){
      log.info("修改状态:{} {}",status,id);
      setmealService.updatestatus(status,id);
      return Result.success();
   }
   @DeleteMapping
   @CacheEvict(cacheNames = "setmeal",allEntries = true)
   public Result deletesetmeal(@RequestParam String[] ids){
      log.info("批量删除：{}",ids);
      setmealService.delete(ids);
      return Result.success();
   }
}
