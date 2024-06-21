package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("usersetmealcontroller")
@RequestMapping("/user/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @GetMapping("/list")
    public Result<List<Setmeal>>  selectsetmeal(Long categoryId){
        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(StatusConstant.ENABLE);
        setmeal.setCategoryId(categoryId);
        log.info("分类id:{}",categoryId);
       List<Setmeal> list= setmealService.selectsetmeal(setmeal);
       return Result.success(list);

    }
    @GetMapping("/dish/{id}")
    public Result<List<DishItemVO>> userselectsermealdish(@PathVariable Long id){
        log.info("套餐id: {}",id);
        List<DishItemVO> dishItemVOS= setmealService.userselectsermealdish(id);
        return Result.success(dishItemVOS);
    }

}
